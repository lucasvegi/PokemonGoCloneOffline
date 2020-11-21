package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import teste.lucasvegi.pokemongooffline.Model.ControladoraFachadaSingleton;
import teste.lucasvegi.pokemongooffline.Model.Ovo;
import teste.lucasvegi.pokemongooffline.R;
import teste.lucasvegi.pokemongooffline.Util.BancoDadosSingleton;
import teste.lucasvegi.pokemongooffline.View.AdapterOvos;

public class OvosActivity extends Activity implements AdapterView.OnItemClickListener {
    private List<Ovo> ovos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ovos);

        TextView txtTotal = (TextView) findViewById(R.id.txtOvosTotal);
        int total = ControladoraFachadaSingleton.getInstance().getOvos().size();
        txtTotal.setText("Ovos: " + total + "/9");

        ovos = ControladoraFachadaSingleton.getInstance().getOvos();
        ListView listView = (ListView) findViewById(R.id.listaOvos);

        AdapterOvos adapterOvos = new AdapterOvos(ovos, this);
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
