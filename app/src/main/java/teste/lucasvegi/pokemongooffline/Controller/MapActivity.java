package teste.lucasvegi.pokemongooffline.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
//import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.model.PlacesSearchResult;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import teste.lucasvegi.pokemongooffline.Model.Aparecimento;
import teste.lucasvegi.pokemongooffline.Model.ControladoraFachadaSingleton;
import teste.lucasvegi.pokemongooffline.Model.NearbySearch;
import teste.lucasvegi.pokemongooffline.Model.Pokestop;
import teste.lucasvegi.pokemongooffline.R;
import teste.lucasvegi.pokemongooffline.Util.TimeUtil;

public class MapActivity extends FragmentActivity implements LocationListener, GoogleMap.OnMarkerClickListener, Runnable, OnMapReadyCallback {
    public GoogleMap map;
    public LocationManager lm;
    public Criteria criteria;
    public String provider;

    public int TEMPO_REQUISICAO_LATLONG = 5000;
    public int DISTANCIA_MIN_METROS = 0;
    public int intervaloEntreSorteiosEmMinutos = 1;     //USADO PARA DETERMINAR INTERVALO DE TEMPO ENTRE SORTEIOS DE POKEMON
    public double distanciaMinimaParaBatalhar = 150.0;  //USADO PARA DETERMINAR A DISTÂNCIA MINIMA NECESSÁRIA PARA BATALHAR COM UM POKEMON

    public Marker eu;
    public WebView webViewLoader;
    public boolean primeiraPosicao = true;
    public boolean continuaSorteando = true;

    //usado no onActivityResult
    public final static int MENU_PERFIL = 1;
    public final static int MENU_MAPA = 2;
    public final static int MENU_POKEDEX = 3;

    public List<Aparecimento> aparecimentos;
    public Map<Marker,Aparecimento> aparecimentoMap; //dicionário para ajudar no momento de clicar em pontos
    public Map<Marker,PlacesSearchResult> aparecimentoMap2; //dicionário para ajudar no momento de clicar em pontos

    public Marker LatMin;
    public Marker LatMax;
    public Marker LongMin;
    public Marker LongMax;

    public Location posicaoAtual;

    MediaPlayer mp; //música do mapa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment frag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        frag.getMapAsync(this);
        configuraCriterioLocation();
        //aloca lista e Map
        aparecimentos = new ArrayList<Aparecimento>();
        aparecimentoMap = new HashMap<Marker, Aparecimento>();

        //Configura web view loader sorteio de pokemon
        webViewLoader = (WebView) findViewById(R.id.imgLoader);
        webViewLoader.loadUrl("file:///android_asset/loading.gif");
        webViewLoader.setBackgroundColor(Color.TRANSPARENT);
        webViewLoader.setVisibility(View.GONE);

        //Escolhe imagem do botão de perfil de acordo com o sexo do usuário
        ImageButton imgPerfil = (ImageButton) findViewById(R.id.botaoPerfil);
        if(ControladoraFachadaSingleton.getInstance().getUsuario().getSexo().equals("M"))
            imgPerfil.setImageResource(R.drawable.male_profile);
        else
            imgPerfil.setImageResource(R.drawable.female_profile);

        //Configura o nome do usuário abaixo do botão de perfil
        TextView txtNomeUser = (TextView) findViewById(R.id.txtNomeUser);
        txtNomeUser.setText(ControladoraFachadaSingleton.getInstance().getUsuario().getLogin());

    }

    @Override
    protected void onStart() {
        super.onStart();

        iniciaGeolocation(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            //pausa a música
            mp.pause();
        }catch (Exception e){
            Log.e("MAPA", "ERRO: " + e.getMessage());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            //reinicia a música
            mp.start();
        }catch (Exception e){
            Log.e("MAPA", "ERRO: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        lm.removeUpdates(this);
        Log.d("PROVEDOR", "Provedor " + provider + " parado!");

        continuaSorteando = false; //para a thread de sorteio

        try {
            //entrega o recurso de música para o sistema
            mp.release();
        }catch (Exception e){
            Log.e("MAPA", "ERRO: " + e.getMessage());
        }

        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        posicaoAtual = location;

        LatLng personagem = new LatLng(location.getLatitude(),location.getLongitude());

        //Remove o personagem para atualizar a sua posição
        if(eu != null) {
            eu.remove();
        }

        //Escolhe imagem do personagem de acordo com o sexo
        if(ControladoraFachadaSingleton.getInstance().getUsuario().getSexo().equals("M"))
            eu = map.addMarker(new MarkerOptions().position(personagem).icon(BitmapDescriptorFactory.fromResource(R.drawable.male)));
        else
            eu = map.addMarker(new MarkerOptions().position(personagem).icon(BitmapDescriptorFactory.fromResource(R.drawable.female)));

        //centraliza a camera na primeira vez que obtiver a posição
        if(primeiraPosicao) {
            //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(personagem, 18);

            CameraUpdate c = CameraUpdateFactory.newCameraPosition(
                    new CameraPosition.Builder()
                            .target(personagem)
                            .tilt(60)
                            .zoom(18)
                            .build());

            map.animateCamera(c);
            primeiraPosicao = false;

            //inicia a thread de sorteio de pokemon
            new Thread(this).start();

            //inicia a música do mapa - rota 1 quando obtem localização do usuário
            mp = MediaPlayer.create(getBaseContext(), R.raw.tema_rota_1);
            mp.setLooping(true);
            mp.start();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void configuraCriterioLocation() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();

        PackageManager packageManager = getPackageManager();
        boolean hasGPS = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);

        if (hasGPS) {
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            Log.i("LOCATION", "usando GPS");
        } else {
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            Log.i("LOCATION", "usando WI-FI ou dados");
        }
    }

    public void iniciaGeolocation(Context ctx) {
        //obtem o melhor provedor habilitado com o critério
        provider = lm.getBestProvider(criteria, true);

        if (provider == null) {
            Log.e("PROVEDOR", "Nenhum provedor encontrado");
        } else {
            Log.i("PROVEDOR", "Esta sendo utilizado o provedor " + provider);

            lm.requestLocationUpdates(provider, TEMPO_REQUISICAO_LATLONG, DISTANCIA_MIN_METROS, (LocationListener) ctx);
        }
    }

    //Over da funcao de callback pra clicar nos POI
    public Pokestop criarPokestop (Marker pok){
        //calcula a distancia e ve se eh valido interagir
        // Inicializa o SDK
        //Places.initialize(getApplicationContext(), "AIzaSyAL89AOH7JqxRQG88vonwlf9vZqebXHvHw");
        Places.initialize(getApplicationContext(), "AIzaSyD_82FN8rMIJzMrZyx1l7xZbpW1SYN5pdU");
        // Instancia Placesclient
        PlacesClient placesClient = Places.createClient(this);

        // Pega Id
        String placeId = pok.getTitle(); //pega o ID do lugar

        //incializa a pokestop
        Pokestop pkStop = new Pokestop();

        // Escolhe o que quer no request
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        // Faz o request com a lista de requerimentos
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields)
                .build();

        // Add a listener to handle the response.
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            // se achou o lugar pelo id do poi, passa os dados cria um novo pokestop e passa os dados
            Log.i("TAG", "Achou: " + place.getName());
            pkStop.setID(place.getId());
            pkStop.setNome(place.getName());
            Log.d("TAG", "BBBBBBBBBBBBBBBBBBBBBcriarPokestop: "+pkStop.getNome());
            pkStop.setUltimoAcesso(TimeUtil.getHoraMinutoSegundoDiaMesAno());

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                // Handle error with given status code.
                Log.e("TAG", "Place not found: " + exception.getMessage());
            }
        });

        List<Place.Field> fields = Arrays.asList(Place.Field.PHOTO_METADATAS);

        FetchPlaceRequest placeRequest = FetchPlaceRequest.builder(placeId, fields).build();

        // faz request pra imagem, depois ve um tamanho bom pra padronizar as imagens
        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            // Get the photo metadata.
            PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);
            // Get the attribution text.
            String attributions = photoMetadata.getAttributions();
            // Create a FetchPhotoRequest.
            FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(500) // Optional.
                    .setMaxHeight(300) // Optional.
                    .build();
            placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                Bitmap bitmap = fetchPhotoResponse.getBitmap();
                // PASSAR A IMAGEM PRO POKESTOP
                pkStop.setFoto(bitmap);

            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    int statusCode = apiException.getStatusCode();
                    // Handle error with given status code.
                    Log.e("TAG", "Place not found: " + exception.getMessage());
                }
            });
        });
        return pkStop;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getAlpha()!=3) {
            if (!marker.equals(eu)) {
                double distanciaPkmn = getDistanciaPkmn(eu, marker);
                double distanciaMin = distanciaMinimaParaBatalhar;

                //TODO: diminuir a distância entre treinador e pokemon
                if (distanciaPkmn <= distanciaMin) {
                    try {
                        //pausa a música
                        mp.pause();

                        Aparecimento ap = aparecimentoMap.get(marker);
                        Intent it = new Intent(this, CapturaActivity.class);
                        it.putExtra("pkmn", ap);

                        startActivity(it);

                        marker.remove();
                    } catch (Exception e) {
                        Log.e("CliqueMarker", "Erro: " + e.getMessage());
                    }
                } else {
                    DecimalFormat df = new DecimalFormat("0.##");
                    Toast.makeText(this, "Você está a " + df.format(distanciaPkmn) + " metros do " + marker.getTitle() + ".\n" +
                            "Aproxime-se pelo menos " + df.format(distanciaPkmn - distanciaMin) + " metros!", Toast.LENGTH_LONG).show();
                }
            }
        }
        else  {
            Location Locpkstp= new Location(provider);
            Locpkstp.setLatitude(marker.getPosition().latitude);
            Locpkstp.setLongitude(marker.getPosition().longitude);
            double DistPkStop = getDistanciaPkStop(eu,Locpkstp);
            double distMin = distanciaMinimaParaBatalhar; //enquanto nao decidimos deixar a mesma da batalha
            Pokestop pkstp = criarPokestop(marker);
            if (DistPkStop > distMin) {
                DecimalFormat df = new DecimalFormat("0.##");
                Toast.makeText(this,"Você está a " + df.format(DistPkStop) + " metros do " + pkstp.getNome() + ".\n" +
                        "Aproxime-se pelo menos " + df.format(DistPkStop - distMin) + " metros!", Toast.LENGTH_LONG).show();
            } else {
                //salva e interagi
            }
        }
        return false;
    }

    public void limparMarcadores(){
        try {
            //itera no dicionário de marcadores de aparecimentos
            for (Map.Entry<Marker, Aparecimento> entry : aparecimentoMap.entrySet()) {
                Log.d("LimparMarker", "Pokemon: " + entry.getKey().getTitle());
                entry.getKey().remove();
            }
            //limpa o dicionário de marcadores de aparecimentos
            aparecimentoMap.clear();

//            for (Map.Entry<Marker, PlacesSearchResult> entry : aparecimentoMap2.entrySet()){
//                Log.d("LimparMarker", "PokeStop: " + entry.getKey().getTitle());
//                entry.getKey().remove();
//            }
//            //limpa o dicionário de marcadores de aparecimentos
//            aparecimentoMap2.clear();

        }catch (Exception e){
            Log.e("LimparMarker","ERRO: " + e.getMessage());
        }
    }

    public void plotarMarcadores(){
        //Plota Marcadores
        try {
            Aparecimento [] apVet = ControladoraFachadaSingleton.getInstance().getAparecimentos();

            for(int i = 0; i < apVet.length; i++){
                Log.d("PlotarMarker", "Pokemon: " + apVet[i].getPokemon().getNome() + " Lat: " + apVet[i].getLatitude() + " Long: " + apVet[i].getLongitude());

                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(apVet[i].getPokemon().getIcone());

                Marker pokePonto = map.addMarker(new MarkerOptions().
                        icon(icon).
                        position(new LatLng(apVet[i].getLatitude(), apVet[i].getLongitude())).
                        title(apVet[i].getPokemon().getNome()));

                //adiciona marcador no dicionário
                aparecimentoMap.put(pokePonto,apVet[i]);
            }
        }catch (Exception e){
            Log.e("PlotarMarker","ERRO: " + e.getMessage());
        }

        PlacesSearchResult[] placesSearchResults = new NearbySearch().run(new com.google.maps.model.LatLng(eu.getPosition().latitude,eu.getPosition().longitude)).results;
        for (int i=0; i< placesSearchResults.length/2;i++){
            double lat = placesSearchResults[i].geometry.location.lat;
            double lng = placesSearchResults[i].geometry.location.lng;

            Location pkstp= new Location(provider);
            pkstp.setLatitude(lat);
            pkstp.setLongitude(lng);
            double DistPkStop = getDistanciaPkStop(eu, pkstp);
            double distMin = distanciaMinimaParaBatalhar; //enquanto nao decidimos deixar a mesma da batalha
            if (DistPkStop < distMin) {
                Marker pokestop = map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.pokestop_perto)).position(new LatLng(lat, lng)).title(placesSearchResults[i].placeId) .alpha(3));
                //aparecimentoMap2.put(pokestop, placesSearchResults[i]);
            } else {
                Marker pokestop = map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.pokestop_longe)).position(new LatLng(lat, lng)).title(placesSearchResults[i].placeId) .alpha(3));
                //aparecimentoMap2.put(pokestop, placesSearchResults[i]);
            }
        }
    }

    public double getDistanciaPkmn(Marker treinador, Marker pkmn){
        //cria location do treinador para ver distância
        Location trainer = new Location(provider);
        trainer.setLatitude(treinador.getPosition().latitude);
        trainer.setLongitude(treinador.getPosition().longitude);

        //cria location do pokemon para ver distância
        Location poke = new Location(provider);
        poke.setLatitude(pkmn.getPosition().latitude);
        poke.setLongitude(pkmn.getPosition().longitude);

        return trainer.distanceTo(poke);
    }

    public double getDistanciaPkStop(Marker treinador, Location pkStop){
        //cria location do treinador para ver distância
        Location trainer = new Location(provider);
        trainer.setLatitude(treinador.getPosition().latitude);
        trainer.setLongitude(treinador.getPosition().longitude);

        return trainer.distanceTo(pkStop);
    }

    public void calcularLatLongMinMaxParaSorteio(Location location){
        double kmInLongitudeDegree = 111.320 * Math.cos( location.getLatitude() / 180.0 * Math.PI);
        double radiusInKm = 0.3;
        double deltaLat = radiusInKm / 111.1;
        double deltaLong = radiusInKm / kmInLongitudeDegree;

        double minLat = location.getLatitude() - deltaLat;
        double maxLat = location.getLatitude() + deltaLat;
        double minLong = location.getLongitude() - deltaLong;
        double maxLong = location.getLongitude() + deltaLong;

        //Soteia os aparecimentos de pokemon na controladora geral
        ControladoraFachadaSingleton.getInstance().sorteiaAparecimentos(minLat, maxLat, minLong, maxLong);

        //atualiza o mapa na thread principal
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                limparMarcadores();
                plotarMarcadores();
            }
        });


        //plotarMarcadorLatLongMinMax(location,minLat,maxLat,minLong,maxLong);
    }

    public void plotarMarcadorLatLongMinMax(Location location, double minLat, double maxLat, double minLong, double maxLong){
        if(LatMin != null && LatMax != null && LongMin != null && LongMax != null){
            LatMin.remove();
            LatMax.remove();
            LongMin.remove();
            LongMax.remove();
        }
        LatMin = map.addMarker(new MarkerOptions().position(new LatLng(minLat, location.getLongitude())).title("LatMin"));
        LatMax = map.addMarker(new MarkerOptions().position(new LatLng(maxLat, location.getLongitude())).title("LatMax"));
        LongMin = map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), minLong)).title("LongMin"));
        LongMax = map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), maxLong)).title("LongMax"));
    }

    public void clickPokedex(View v){
        //Toast.makeText(this,"Pokedex",Toast.LENGTH_SHORT).show();

        Intent it = new Intent(this, PokedexActivity.class);
        startActivityForResult(it, MENU_POKEDEX);
    }

    public void clickPerfil(View v){
        //Toast.makeText(this,"Perfil",Toast.LENGTH_SHORT).show();

        Intent it = new Intent(this, PerfilActivity.class);
        startActivityForResult(it,MENU_PERFIL);
    }

    public void clickMapaCaptura(View v){
        //Toast.makeText(this,"Mapa de Capturas",Toast.LENGTH_SHORT).show();

        Intent it = new Intent(this, MapCapturasActivity.class);
        startActivityForResult(it, MENU_MAPA);
    }

    @Override
    public void run() {
        try {
            while (continuaSorteando) {
                Log.d("SORTEIO","Acordou a Thread de sorteio!");

                //atualiza o mapa na thread principal deixando loader visível
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webViewLoader.setVisibility(View.VISIBLE);
                    }
                });

                //dorme 3 segundos para deixar o loader sendo exibido
                TimeUnit.SECONDS.sleep(3);

                //atualiza o mapa na thread principal deixando loader invisível
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webViewLoader.setVisibility(View.GONE);
                    }
                });

                //Calcula raio em relação à localização e sorteia pokemons
                calcularLatLongMinMaxParaSorteio(posicaoAtual);
                TimeUnit.MINUTES.sleep(intervaloEntreSorteiosEmMinutos);
            }
        }catch (Exception e){
            Log.e("SORTEIO","ERRO: " + e.getMessage());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MENU_PERFIL && resultCode == MENU_PERFIL){
            //Logout
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //configura o mapa
        map.setMyLocationEnabled(true);
        map.setBuildingsEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setOnMarkerClickListener(this); //marcadores clicaveis
        //map.setOnPoiClickListener(this); //label do maps clicavel
    }
}
