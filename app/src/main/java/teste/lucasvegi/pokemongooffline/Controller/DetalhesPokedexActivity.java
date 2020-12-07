package teste.lucasvegi.pokemongooffline.Controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.session.MediaSessionManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import teste.lucasvegi.pokemongooffline.Model.Aparecimento;
import teste.lucasvegi.pokemongooffline.Model.ControladoraFachadaSingleton;
import teste.lucasvegi.pokemongooffline.Model.Pokemon;
import teste.lucasvegi.pokemongooffline.Model.PokemonCapturado;
import teste.lucasvegi.pokemongooffline.R;
import teste.lucasvegi.pokemongooffline.Util.BancoDadosSingleton;
import teste.lucasvegi.pokemongooffline.View.AdapterPokedex;

public class DetalhesPokedexActivity extends Activity implements LocationListener {

    private Pokemon pkmn;
    private Pokemon pkmnEvolucao;
    public LocationManager lm;
    public Criteria criteria;
    public String provider;
    public int TEMPO = 5000;
    public int DIST = 0;
    public Location atual;

    private int docesNecessarios;
    private int docesObtidos;

    // Constante auxiliar para o método setResult()
    private final int ATUALIZAR_TELA = 1;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pokedex);

        Intent it = getIntent();
        pkmn = (Pokemon) it.getSerializableExtra("pkmn");
        pkmnEvolucao = pkmn.getEvolucao();
        docesNecessarios = pkmn.getQuantDocesNecessarios();
        docesObtidos = pkmn.getQuantDocesObtidos();

        //TODO: RESOLVIDO - procura na lista de pokemons da controladora o pokemon recebido da tela anterior.
        //pkmn = ControladoraFachadaSingleton.getInstance().convertPokemonSerializableToObject(pkmn);

        //Recupera views
        ImageView foto = (ImageView) findViewById(R.id.imgDetalhePokemon);
        TextView txtTituloDetalhes = (TextView) findViewById(R.id.txtTituloDetalhesPokedex);
        TextView txtNum = (TextView) findViewById(R.id.txtNumPkmnDetalhes);
        TextView txtNome = (TextView) findViewById(R.id.txtNomePkmnDetalhes);
        TextView txtCapturados = (TextView) findViewById(R.id.txtPkmnCapturadosDetalhes);
        TextView txtQuantDoces = (TextView) findViewById(R.id.txtQuantidadeDocesDetalhes); //linha add
        TextView txtTipo1 = (TextView) findViewById(R.id.txtTipo1PkmnDetalhes);
        TextView txtTipo2 = (TextView) findViewById(R.id.txtTipo2PkmnDetalhes);

        Button btn_evoluir = (Button) findViewById(R.id.btnEvoluirDetalhes);

        //Ajusta cor do botão evoluir
        if(docesObtidos < docesNecessarios|| pkmnEvolucao == null || !pkmn.estaDisponivel(false)){
            btn_evoluir.setBackgroundResource(R.drawable.roundshape_botao_cinza);
        }
        else
            btn_evoluir.setBackgroundResource(R.drawable.botao_style);

        //Tenta colocar valores vindos do pokemon por navegação
        try {
            foto.setImageResource(pkmn.getFoto());

            //ajusta o numero de zeros
            if(pkmn.getNumero() < 10){
                txtNum.setText("#00"+pkmn.getNumero());
            }else if(pkmn.getNumero() < 100){
                txtNum.setText("#0"+pkmn.getNumero());
            }else{
                txtNum.setText("#"+pkmn.getNumero());
            }

            txtTituloDetalhes.setText("Detalhes " + pkmn.getNome());
            txtNome.setText(pkmn.getNome());
            txtCapturados.setText("Capturados: " + ControladoraFachadaSingleton.getInstance().getUsuario().getQuantidadeCapturas(pkmn));

            txtQuantDoces.setText("Doces obtidos: " + docesObtidos);

            //Ajusta texto e cores dos tipos
            setTextViewBackground(txtTipo1,pkmn.getTipos().get(0).getNome());
            if(pkmn.getTipos().size() > 1)
                setTextViewBackground(txtTipo2,pkmn.getTipos().get(1).getNome());
            else
                txtTipo2.setVisibility(View.INVISIBLE);

        }catch (Exception e){
            Log.e("DETALHES", "ERRO: " + e.getMessage());
        }

        configuraCriterioLocation();
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("PROVEDOR", "start");

        provider = lm.getBestProvider(criteria, true);

        if (provider == null) {
            Log.e("PROVEDOR", "Nenhum provedor encontrado");
        } else {
            Log.i("PROVEDOR", "Esta sendo utilizado o provedor " + provider);

            lm.requestLocationUpdates(provider, TEMPO, DIST, this);
        }
    }

    public void clickVoltarDetalhe(View v){
        finish();
    }

    public void clickLocais(View v){
        //Toast.makeText(this, "Locais do " + pkmn.getNome(), Toast.LENGTH_SHORT).show();

        Intent it = new Intent(this,MapCapturasActivity.class);
        it.putExtra("pkmn",pkmn);
        startActivity(it);
    }

    private void evoluir(){
        Aparecimento ap = new Aparecimento();
        ap.setLatitude(atual.getLatitude());
        ap.setLongitude(atual.getLongitude());
        ap.setPokemon(pkmnEvolucao);
        Log.i("EVOLUCAO", "NOME DA EVOLUÇÃO: " + ap.getPokemon().getNome());

        // Envia captura para o servidor antes de fechar tela.
        ControladoraFachadaSingleton.getInstance().getUsuario().capturar(ap);
        Log.i("EVOLUCAO","Evolução capturada");

        // Subtrai os doces utilizados na evolução
        ControladoraFachadaSingleton.getInstance().getUsuario().somarDoces(pkmn, -docesNecessarios-3);
        Log.i("EVOLUCAO","Pokemon evoluído");

        // Exibindo mensagem de sucesso na tela
        Toast.makeText(getBaseContext(),pkmn.getNome() + " foi evoluído! \\o/",Toast.LENGTH_LONG).show();

        // Iniciando activity do pokemon evoluido
        Intent it = new Intent(this, DetalhesPokedexActivity.class);
        it.putExtra("pkmn", ap.getPokemon());
        startActivity(it);

        // Mandando requisição para atualizar a tela da Pokedex ao término desta activity
        Intent itResult = new Intent();
        setResult(ATUALIZAR_TELA,itResult);

        // Encerrando activity
        finish();
    }

    public void clickEvoluir(View v){
        int restante = docesNecessarios-docesObtidos;

        // Verificando se o pokemon possui evolução
        if(pkmnEvolucao == null){
            Toast.makeText(this,"O Pokemon não possui evolução!",Toast.LENGTH_LONG).show();
        }

        // Verificando quantidade de doces
        else if(docesNecessarios > docesObtidos){
            Toast.makeText(this,"Faltam "+ restante +" doces para evoluir!",Toast.LENGTH_LONG).show();
        }

        // Evoluindo pokemon caso exista pokemon disponível
        else if (pkmn.estaDisponivel(true)){ // Se isso ocorre, já atualizamos o atributo 'evoluido' da tabela pokemonusuario no Banco
            evoluir();
        }

        // Se não há pokemon disponível, informamos issso via Toast
        else{
            Toast.makeText(getBaseContext(),"Todos os Pokemons de nome " + pkmn.getNome() + " já estão evoluidos!",Toast.LENGTH_LONG).show();
        }
    }

    private void setTextViewBackground(TextView txt, String tipo){
        txt.setText(tipo);

        if(tipo.equals("Normal"))
            txt.setBackgroundColor(Color.parseColor("#a8a878"));
        else if(tipo.equals("Fire"))
            txt.setBackgroundColor(Color.parseColor("#f08030"));
        else if(tipo.equals("Fighting"))
            txt.setBackgroundColor(Color.parseColor("#c03028"));
        else if(tipo.equals("Water"))
            txt.setBackgroundColor(Color.parseColor("#6890f0"));
        else if(tipo.equals("Flying"))
            txt.setBackgroundColor(Color.parseColor("#a890f0"));
        else if(tipo.equals("Grass"))
            txt.setBackgroundColor(Color.parseColor("#78c850"));
        else if(tipo.equals("Poison"))
            txt.setBackgroundColor(Color.parseColor("#a040a0"));
        else if(tipo.equals("Electric"))
            txt.setBackgroundColor(Color.parseColor("#f8d030"));
        else if(tipo.equals("Ground"))
            txt.setBackgroundColor(Color.parseColor("#e0c068"));
        else if(tipo.equals("Psychic"))
            txt.setBackgroundColor(Color.parseColor("#f85888"));
        else if(tipo.equals("Rock"))
            txt.setBackgroundColor(Color.parseColor("#b8a038"));
        else if(tipo.equals("Ice"))
            txt.setBackgroundColor(Color.parseColor("#98d8d8"));
        else if(tipo.equals("Bug"))
            txt.setBackgroundColor(Color.parseColor("#a8b820"));
        else if(tipo.equals("Dragon"))
            txt.setBackgroundColor(Color.parseColor("#7038f8"));
        else if(tipo.equals("Ghost"))
            txt.setBackgroundColor(Color.parseColor("#705898"));
        else if(tipo.equals("Dark"))
            txt.setBackgroundColor(Color.parseColor("#705848"));
        else if(tipo.equals("Steel"))
            txt.setBackgroundColor(Color.parseColor("#b8b8d0"));
        else if(tipo.equals("Fairy"))
            txt.setBackgroundColor(Color.parseColor("#ee99ac"));

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null)
            atual = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("PROVEDOR", "Provedor mudou de estado");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("PROVEDOR", "Habilitou o provedor");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("PROVEDOR", "Desabilitou o provedor");
    }
}
