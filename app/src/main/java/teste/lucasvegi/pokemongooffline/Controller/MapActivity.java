package teste.lucasvegi.pokemongooffline.Controller;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.Manifest;
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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import teste.lucasvegi.pokemongooffline.Model.Aparecimento;
import teste.lucasvegi.pokemongooffline.Model.ControladoraFachadaSingleton;
import teste.lucasvegi.pokemongooffline.Model.InteracaoPokestop;
import teste.lucasvegi.pokemongooffline.Model.Pokestop;
import teste.lucasvegi.pokemongooffline.R;
import teste.lucasvegi.pokemongooffline.Util.BancoDadosSingleton;
import teste.lucasvegi.pokemongooffline.Util.directionshelpers.FetchURL;
import teste.lucasvegi.pokemongooffline.Util.directionshelpers.TaskLoadedCallback;

public class MapActivity extends FragmentActivity implements LocationListener, GoogleMap.OnMarkerClickListener, Runnable, TaskLoadedCallback, OnMapReadyCallback {
    public GoogleMap map;
    public LocationManager lm;
    public Criteria criteria;
    public String provider;
    private SupportMapFragment frag;

    Marker marcador_Permissao = null;
    boolean permissao_cam = false;
    boolean permissao_local = false;
    private final int CAMERA_PERMISSION = 1;
    private final int LOCATION_PERMISSION = 2;
    public Polyline currentPolyline;
    public Marker targetPkmn;

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
    public final static int MENU_OVOS = 4;

    public List<Aparecimento> aparecimentos;
    public Map<Marker,Aparecimento> aparecimentoMap; //dicionário para ajudar no momento de clicar em pontos
    public Map<Marker,Pokestop> pokestopMap; //dicionário para ajudar no momento de clicar em pontos
    public Marker LastPkstopMarker = null;

    public Marker LatMin;
    public Marker LatMax;
    public Marker LongMin;
    public Marker LongMax;

    public Location posicaoAtual, posicaoInit;

    MediaPlayer mp; //música do mapa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        frag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        frag.getMapAsync(this);
        //criando MediaPlayer ao criar a activity para evitar null pointer exception
        mp = MediaPlayer.create(getBaseContext(), R.raw.tema_rota_1);

        //aloca lista e Map
        aparecimentos = new ArrayList<Aparecimento>();
        aparecimentoMap = new HashMap<Marker, Aparecimento>();

        //aloca map de pokestops
        pokestopMap = new HashMap<Marker, Pokestop>();

        targetPkmn = null;

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
        PackageManager packageManager = getPackageManager();
        boolean hasCam = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (hasCam)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        } else {
            configuraCriterioLocation();
            iniciaGeolocation(this);
            if(map != null)
                map.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            //pausa a música
            if(mp != null)
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
            if(mp != null)
                mp.start();
        }catch (Exception e){
            Log.e("MAPA", "ERRO: " + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (LastPkstopMarker!=null) {
                Pokestop Pkstp = pokestopMap.get(LastPkstopMarker);

                InteracaoPokestop interc = ControladoraFachadaSingleton.getInstance().getUltimaInteracao(Pkstp);

                //atualizando ícone da pokestop que o usuário acabou de interagir
                LastPkstopMarker.setIcon(Pkstp.getIcon(true));

            }
        }catch (Exception e){
            Log.e("RESUME", "ERRO: " + e.getMessage());
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

        if(targetPkmn != null){
            double distanciaPkmn = getDistanciaPkmn(eu, targetPkmn);
            double distanciaMin = distanciaMinimaParaBatalhar;
            if(distanciaPkmn <= distanciaMin){
                Toast.makeText(this,"Você já está perto do " + targetPkmn.getTitle() + "!\n" +
                        "Tente capturá-lo agora! ", Toast.LENGTH_LONG).show();
            }
        }

        //Escolhe imagem do personagem de acordo com o sexo
        //mapa é carregado de forma assincrona
        if(map != null) {
            if (ControladoraFachadaSingleton.getInstance().getUsuario().getSexo().equals("M") && map != null)
                eu = map.addMarker(new MarkerOptions().position(personagem).icon(BitmapDescriptorFactory.fromResource(R.drawable.male)));
            else
                eu = map.addMarker(new MarkerOptions().position(personagem).icon(BitmapDescriptorFactory.fromResource(R.drawable.female)));
        }
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
            posicaoInit = location;
            //inicia a thread de sorteio de pokemon
            new Thread(this).start();

            //inicia a música do mapa - rota 1 quando obtem localização do usuário
            mp.setLooping(true);
            mp.start();

            plotarPokestops();
        }

        //se o treinador se afasta 200m do ultimo ponto onde atualizamos as pokestops
        //atualizamos as pokestops novamente e plotamos os pokemons
        double deltaDistancia = posicaoAtual.distanceTo(posicaoInit);
        if (deltaDistancia > 200.00) {
            posicaoInit = posicaoAtual;
            limparPokestops();
            plotarPokestops();
        }
        else
            atualizaPokestops();
        Log.i("GPS", "NOVA LOCALIZAÇÂO");
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

    @SuppressLint("MissingPermission")
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        String tag = "";
        if (marker.getTag() != null)
            tag = marker.getTag().toString();

        if (tag == "pokemon") {
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

                        if (marker.equals(targetPkmn)) {
                            if (currentPolyline != null)
                                currentPolyline.remove();
                            targetPkmn = null;
                        }
                        marker.remove();
                    } catch (Exception e) {
                        Log.e("CliqueMarker", "Erro: " + e.getMessage());
                    }
                } else {
                    if (marker.equals(targetPkmn)) {// caso o usuario clique novamente no pokemon alvo, a rota deve sumir
                        if (currentPolyline != null)
                            currentPolyline.remove();
                        targetPkmn = null;
                    } else {
                        targetPkmn = marker;
                        String url = getDirectionsUrl(eu.getPosition(), marker.getPosition());
                        new FetchURL(MapActivity.this).execute(url);
                        DecimalFormat df = new DecimalFormat("0.##");
                        Toast.makeText(this, "Você está a " + df.format(distanciaPkmn) + " metros do " + marker.getTitle() + ".\n" +
                                "Aproxime-se pelo menos " + df.format(distanciaPkmn - distanciaMin) + " metros!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        if (tag == "pokestop") {
            Location Locpkstp = new Location(provider);
            Locpkstp.setLatitude(marker.getPosition().latitude);
            Locpkstp.setLongitude(marker.getPosition().longitude);
            double DistPkStop = getDistanciaPkStop(eu, Locpkstp);
            double distMin = distanciaMinimaParaBatalhar; //enquanto nao decidimos uma distancia apropriada deixar a mesma da batalha
            if (DistPkStop > distMin) {
                DecimalFormat df = new DecimalFormat("0.##");
                Toast.makeText(this, "Você está a " + df.format(DistPkStop) + " metros do " + marker.getTitle() + ".\n" + "Aproxime-se pelo menos " + df.format(DistPkStop - distMin) + " metros!", Toast.LENGTH_LONG).show();
            } else {
                //Pega o pokestop equivalente ao marcado no mapa e iniciar a tela do pokestop
                Pokestop pokestop = pokestopMap.get(marker);
                Intent it = new Intent(this, PokestopActivity.class);
                //salvar a imagem e pokestop para recuperacao dos dados na outra activity
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap foto = pokestop.getFoto();
                if (foto != null)
                    pokestop.getFoto().compress(Bitmap.CompressFormat.PNG, 80, stream);
                byte[] byteArray = stream.toByteArray();

                LastPkstopMarker = marker;
                it.putExtra("foto", byteArray);
                it.putExtra("pokestop", pokestop);
                startActivity(it);
            }
        }
        return false;
    }


    public void requestLocationPermission(){
        //verifica se precisa explicar a permissão
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {

            //pede permissão
            ActivityCompat.requestPermissions(this
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}
                    , LOCATION_PERMISSION
            );

            Toast.makeText(this
                    , "Permita o acesso a localização do dispositivo para\n" +
                            "medir a distância até o local selecionado."
                    , Toast.LENGTH_LONG).show();

        } else {
            //pede permissão
            ActivityCompat.requestPermissions(this
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}
                    , LOCATION_PERMISSION
            );
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissao_cam = true;
                    Toast.makeText(this, "Permissão concedida", Toast.LENGTH_LONG).show();
                }
                else {
                    permissao_cam = true;
                    Toast.makeText(this, "Permissão Necessária para usar a camera", Toast.LENGTH_LONG).show();
                    Log.d("PERMISSAO", "NAO DEIXOUUUUUUU");
                }
            }
            case LOCATION_PERMISSION: {
                if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configuraCriterioLocation();
                    iniciaGeolocation(this);
                    if(map != null)
                        map.setMyLocationEnabled(true);
                }
            }
        }
    }

    public void limparPokestops() {
        try {
            for (Map.Entry<Marker, Pokestop> entry : pokestopMap.entrySet()) {
                Log.d("LimparMarker", "PokeStop: " + entry.getKey().getTitle());
                entry.getKey().remove();
            }
            //limpa o dicionário de marcadores de pokéstops
            pokestopMap.clear();

        } catch (Exception e) {
            Log.e("LimparMarker", "ERRO: " + e.getMessage());
        }
    }

    public void atualizaPokestops(){
        try {
            for (Map.Entry<Marker, Pokestop> entry : pokestopMap.entrySet()) {
                Log.d("AtualizarMarker", "PokeStop: " + entry.getKey().getTitle());
                Marker marker = entry.getKey();

                if(marker != null){

                    Pokestop p = entry.getValue();
                    if(!p.getDisponivel()) {
                        Date TempoAtual = Calendar.getInstance().getTime();
                        InteracaoPokestop it = ControladoraFachadaSingleton.getInstance().getUltimaInteracao(p);
                        double diff = TempoAtual.getTime() - it.getUltimoAcesso().getTime();
                        int diffSec = (int) diff / (1000);
                        if (diffSec > 300) {
                            p.setDisponivel(true);
                            ContentValues valores = new ContentValues();
                            valores.put("disponivel", true);
                            BancoDadosSingleton.getInstance().atualizar("Pokestop", valores, "idPokestop = '" + p.getID() + "'");
                        } else
                            p.setDisponivel(false);
                    }
                    Location pkstp = new Location(provider);
                    pkstp.setLongitude(p.getlongi());
                    pkstp.setLatitude(p.getlat());

                    double distancia = getDistanciaPkStop(eu,pkstp);
                    double distanciaMin = distanciaMinimaParaBatalhar;

                    marker.setIcon(p.getIcon(distancia < distanciaMin));
                }
            }
        } catch (Exception e) {
            Log.e("LimparMarker", "ERRO: " + e.getMessage());
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origem da rota
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destino da rota
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=walking";
        // Parametros para web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Formato do output
        String output = "json";
        // Url
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.directions_key);
        return url;
    }

    public void limparMarcadores(){
        try{
            //itera no dicionário de marcadores de aparecimentos
            for (Map.Entry<Marker, Aparecimento> entry : aparecimentoMap.entrySet()){
                Log.d("LimparMarker", "Pokemon: " + entry.getKey().getTitle());
                entry.getKey().remove();
            }
            //limpa o dicionário de marcadores de aparecimentos
            aparecimentoMap.clear();
            currentPolyline.remove();
            targetPkmn = null;
        }catch (Exception e){
            Log.e("LimparMarker","ERRO: " + e.getMessage());
        }
    }

    public void plotarPokestops() {
        List<Pokestop> list = ControladoraFachadaSingleton.getInstance().getPokestops(eu.getPosition().latitude, eu.getPosition().longitude);
        for (int i = 0; i < list.size(); i++) {
            Pokestop p = list.get(i);
            Location pkstp = new Location(provider);
            pkstp.setLongitude(p.getlongi());
            pkstp.setLatitude(p.getlat());
            Marker pokestopMarker;

            double distancia = getDistanciaPkStop(eu, pkstp);
            double distanciaMin = distanciaMinimaParaBatalhar;

            if (p.getUltimoAcesso() != null) {
                Date TempoAtual = Calendar.getInstance().getTime();
                double diff = TempoAtual.getTime() - p.getUltimoAcesso().getTime();
                double diffMinuto = diff / (1000);
                if (diffMinuto > 300) {
                    p.setDisponivel(true);
                }
            }
            pokestopMarker = map.addMarker(p.getMarkerOptions(distancia < distanciaMin));
            pokestopMarker.setTag("pokestop");
            pokestopMap.put(pokestopMarker, p);

        }

    }

    public void plotarMarcadores() {
        //Plota Marcadores
        try {
            Aparecimento [] apVet = ControladoraFachadaSingleton.getInstance().getAparecimentos();

            for(int i = 0; i < apVet.length; i++){
                Log.d("PlotarMarker", "Pokemon: " + apVet[i].getPokemon().getNome() + " Lat: " + apVet[i].getLatitude() + " Long: " + apVet[i].getLongitude());

                Marker pokePonto = map.addMarker(new MarkerOptions().
                        icon(BitmapDescriptorFactory.fromResource(apVet[i].getPokemon().getIcone())).
                        position(new LatLng(apVet[i].getLatitude(), apVet[i].getLongitude())).
                        title(apVet[i].getPokemon().getNome()));
                pokePonto.setTag("pokemon");

                //adiciona marcador no dicionário
                aparecimentoMap.put(pokePonto,apVet[i]);
            }
        }catch (Exception e){
            Log.e("PlotarMarker","ERRO: " + e.getMessage());
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

    public void clickOvo(View v){
        //Toast.makeText(this,"Ovo",Toast.LENGTH_SHORT).show();

        Intent it = new Intent(this, OvosActivity.class);
        it.putExtra("location", posicaoAtual);
        startActivityForResult(it, MENU_OVOS);
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
        map.clear();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            map.setMyLocationEnabled(true);

        map.setBuildingsEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setOnMarkerClickListener(this); //marcadores clicaveis
        //map.setOnPoiClickListener(this); //label do maps clicavel
    }

    @Override
    public void onTaskDone(Object... values) {
        if(currentPolyline!=null) {
            currentPolyline.remove();
        }
        currentPolyline = map.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
