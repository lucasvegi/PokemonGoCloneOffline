package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import teste.lucasvegi.pokemongooffline.Model.Ovo;
import teste.lucasvegi.pokemongooffline.R;
import teste.lucasvegi.pokemongooffline.Util.BancoDadosSingleton;
import teste.lucasvegi.pokemongooffline.View.AdapterOvos;

public class OvosActivity extends Activity implements AdapterView.OnItemClickListener {
    private List<Ovo> ovos = new ArrayList<Ovo>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ovos);

        //ovos.add(1,"verde", "C", R.drawable.ovo_verde, R.drawable.incubadora_verde);

        Cursor c = BancoDadosSingleton.getInstance().buscar("ovo",new String[]{"idOvo","cor","categoria","foto","fotoIncubadora"},"","");
        ListView listView = (ListView) findViewById(R.id.listaOvos);

        while(c.moveToNext()){
            int idO = c.getColumnIndex("idOvo");
            int cor = c.getColumnIndex("cor");
            int categoria = c.getColumnIndex("categoria");
            int foto = c.getColumnIndex("foto");
            int fotoIncubadora = c.getColumnIndex("fotoIncubadora");

            ovos.add(new Ovo(c.getInt(idO),c.getString(cor), c.getString(categoria), c.getInt(foto),c.getInt(fotoIncubadora)));
        }

        AdapterOvos adapterOvos = new AdapterOvos(ovos, this);
        listView.setAdapter(adapterOvos);
        listView.setOnItemClickListener(this);
        c.close();
    }

    public void clickVoltar(View v){
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
