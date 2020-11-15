package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import teste.lucasvegi.pokemongooffline.Model.ControladoraFachadaSingleton;
import teste.lucasvegi.pokemongooffline.Model.Pokemon;
import teste.lucasvegi.pokemongooffline.R;
import teste.lucasvegi.pokemongooffline.Util.BancoDadosSingleton;

public class DetalhesPokedexActivity extends Activity {

    private Pokemon pkmn;

    private int getQuantDoces(Pokemon p){
        Cursor cDoce = BancoDadosSingleton.getInstance().buscar("pokemon p, doce d",
                new String[]{"d.quant quant"},
                "p.idDoce = d.idDoce and d.idDoce = '" + p.getIdDoce() + "'",null);
        cDoce.moveToNext(); //obs: fora do while pois deve haver apenas uma linha de resposta

        return cDoce.getInt(cDoce.getColumnIndex("quant"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_pokedex);

        Intent it = getIntent();
        pkmn = (Pokemon) it.getSerializableExtra("pkmn");

        //Logs úteis para implementação dos doces
        /*Log.i("DOCES:","idDoce: " + pkmn.getIdDoce());
        Log.i("DOCES:","idPokemonBase: " + pkmn.getIdPokemonBase());
        Log.i("DOCES:","Quantidade de doces: " + getQuantDoces(pkmn));*/

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

            txtQuantDoces.setText("Doces: " + getQuantDoces(pkmn));

            //Ajusta texto e cores dos tipos
            setTextViewBackground(txtTipo1,pkmn.getTipos().get(0).getNome());
            if(pkmn.getTipos().size() > 1)
                setTextViewBackground(txtTipo2,pkmn.getTipos().get(1).getNome());
            else
                txtTipo2.setVisibility(View.INVISIBLE);

        }catch (Exception e){
            Log.e("DETALHES", "ERRO: " + e.getMessage());
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


}
