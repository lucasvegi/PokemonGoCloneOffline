package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import teste.lucasvegi.pokemongooffline.Model.ControladoraFachadaSingleton;
import teste.lucasvegi.pokemongooffline.Model.Ovo;
import teste.lucasvegi.pokemongooffline.R;
import teste.lucasvegi.pokemongooffline.View.AdapterOvos;

public class OvosActivity extends Activity implements AdapterView.OnItemClickListener {
    private List<Ovo> ovos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ovos);

        TextView txtTotal = (TextView) findViewById(R.id.txtOvosTotal);


        ovos = ControladoraFachadaSingleton.getInstance().getOvos();
        ListView listView = (ListView) findViewById(R.id.listaOvos);

        Intent it = getIntent();
        Location localizacaoAtual = it.getParcelableExtra("location");


        for(int i = 0; i < ovos.size(); i++) {
            if(ovos.get(i).getLocalizacao() == null) {
                ovos.get(i).setLocalizacao(localizacaoAtual);
            } else {
                if (ovos.get(i).getIncubado() == 1) {
                    double distancia = localizacaoAtual.distanceTo(ovos.get(i).getLocalizacao()) / 1000;
                    Log.i("OVOS", "Distância: " + distancia);
                    ovos.get(i).setKmAndado(ovos.get(i).getKmAndado() + distancia);
                    ovos.get(i).setLocalizacao(localizacaoAtual);

                    //testa se ovo chocou
                    if (ovos.get(i).getKmAndado() >= ovos.get(i).getKm()) {
                        ovos.get(i).setChocado(1);
                        ControladoraFachadaSingleton.getInstance().setChocado(ovos.get(i).getIdOvo(), 1);
                        String nome = ControladoraFachadaSingleton.getInstance().getNomePokemonOvo(ovos.get(i).getIdOvo());
                        Toast.makeText(this, "Chocou um " + nome, Toast.LENGTH_LONG).show();

                        //ovo já foi exibido
                        ControladoraFachadaSingleton.getInstance().setExibido(ovos.get(i).getIdOvo(), 1);

                        //remove ovo da lista de ovos
                        Ovo o = ovos.get(i);
                        ovos.remove(o);

                    }
                }
            }
        }

        //total de ovos na lista
        int total = ControladoraFachadaSingleton.getInstance().getOvos().size();
        txtTotal.setText("Ovos: " + total + "/9");

        AdapterOvos adapterOvos = new AdapterOvos(ovos, this, localizacaoAtual);
        listView.setAdapter(adapterOvos);
        listView.setOnItemClickListener(this);
    }

    public void clickVoltar(View v){
        finish();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}