package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import teste.lucasvegi.pokemongooffline.Model.Ovo;
import teste.lucasvegi.pokemongooffline.Model.Pokemon;
import teste.lucasvegi.pokemongooffline.R;
import teste.lucasvegi.pokemongooffline.Util.BancoDadosSingleton;
import teste.lucasvegi.pokemongooffline.View.AdapterOvos;

public class OvosActivity extends Activity implements AdapterView.OnItemClickListener {
    private List<Ovo> ovos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ovos);
        ListView listView = (ListView) findViewById(R.id.listaOvos);
        //Cursor c = BancoDadosSingleton.getInstance().buscar("ovo",new String[]{"idOvo","cor","foto","fotoIncubadora", "categoria"},"","");

        /*while(c.moveToNext()){
            int idO = c.getColumnIndex("idOvo");
            int cor = c.getColumnIndex("cor");
            int categoria = c.getColumnIndex("categoria");
            int foto = c.getColumnIndex("foto");
            int fotoIncubadora = c.getColumnIndex("fotoIncubadora");

            Ovo o = new Ovo(c.getInt(idO),c.getString(cor), c.getString(categoria), c.getInt(foto),c.getInt(fotoIncubadora),this);

            //verifica se lista de alguma categoria ainda não existe
            /*if(ovos.get(o.getCategoria()) == null)
                ovos.put(o.getCategoria(),new ArrayList<Pokemon>());

            //adiciona o pokemon na lista da sua categoria
            //ovos.get(o.getCategoria()).add(o);
        }
        */

        AdapterOvos adapterOvos = new AdapterOvos(ovos, this);
        listView.setAdapter(adapterOvos);
        listView.setOnItemClickListener(this);
        //c.close();
    }

    public void clickVoltar(View v){
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
