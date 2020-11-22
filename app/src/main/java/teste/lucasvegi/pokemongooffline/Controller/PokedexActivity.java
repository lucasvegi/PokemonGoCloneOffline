package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import teste.lucasvegi.pokemongooffline.Model.ControladoraFachadaSingleton;
import teste.lucasvegi.pokemongooffline.Model.Pokemon;
import teste.lucasvegi.pokemongooffline.R;
import teste.lucasvegi.pokemongooffline.View.AdapterPokedex;

public class PokedexActivity extends Activity implements AdapterView.OnItemClickListener{

    private List<Pokemon> pokemons;
    private MediaPlayer mediaPlayer;

    // Constantes auxiliares para o método startActivityForResult()
    private final int COD_REQUISICAO = 7;
    private final int ATUALIZAR_TELA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex);

        try {
            //Configura total de pokemons capturados
            TextView txtTotal = (TextView) findViewById(R.id.txtPokedexTotal);
            int total = ControladoraFachadaSingleton.getInstance().getUsuario().getPokemons().size();
            txtTotal.setText("Capturados: " + total + "   Faltam: " + (151-total));

            //Prepara a listview customizada da pokedex
            pokemons = ControladoraFachadaSingleton.getInstance().getPokemons();
            ListView listView = (ListView) findViewById(R.id.listaPokedex);

            AdapterPokedex adapterPokedex = new AdapterPokedex(pokemons, this);
            listView.setAdapter(adapterPokedex);
            listView.setOnItemClickListener(this);
            //Inicia a musica tema do menu
            mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.tema_menu);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

        }catch (Exception e){
            Log.e("POKEDEX", "ERRO: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            //para a música tema do menu e devolve o recurso para o sistema
            mediaPlayer.pause();
            mediaPlayer.release();
        }catch (Exception e){
            Log.e("POKEDEX", "ERRO: " + e.getMessage());
        }
    }

    public void clickVoltar(View v){
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            //Click em um item da listView customizada
            Pokemon pkmn = (Pokemon) parent.getAdapter().getItem(position);

            //verifica se pokemon selecionado já foi capturado pelo menos  uma vez
            if (ControladoraFachadaSingleton.getInstance().getUsuario().getQuantidadeCapturas(pkmn) > 0) {

                //Toast.makeText(this, "Detalhes do " + pkmn.getNome(), Toast.LENGTH_SHORT).show();

                Intent it = new Intent(this,DetalhesPokedexActivity.class);
                it.putExtra("pkmn",pkmn);

                startActivityForResult(it,COD_REQUISICAO);
            }

        }catch (Exception e){
            Log.e("POKEDEX", "ERRO no click: " + e.getMessage());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent it) {
        if(it == null){
            Log.i("ACTIVITY_RESULT", "Não foi enviado nenhum valor");
            return;
        }
        else if(requestCode == COD_REQUISICAO){
                if(resultCode == ATUALIZAR_TELA){
                    Log.i("ACTIVITY_RESULT", "Recebido código para atualizar a tela");
                    //Inicia uma nova pokedex
                    Intent itPokedex = new Intent(this, PokedexActivity.class);
                    startActivity(itPokedex);
                    //Termina a pokedex antiga
                    finish();
                }
                else{
                    Log.i("ACTIVITY_RESULT", "Código inválido");
                }
        }
    }
}
