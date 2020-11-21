package teste.lucasvegi.pokemongooffline.View;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import teste.lucasvegi.pokemongooffline.Model.Ovo;
import teste.lucasvegi.pokemongooffline.R;

public class AdapterOvos extends BaseAdapter {

    private List<Ovo> ovos;
    private Activity act;
    private Ovo ovo;

    public AdapterOvos(List<Ovo> ovos, Activity act) {
        try {
            this.ovos = ovos;
            this.act = act;
        } catch (Exception e) {
            Log.e("OVO", "ERRO: " + e.getMessage());
        }
    }


    @Override
    public int getCount() { return ovos.size(); }

    @Override
    public Object getItem(int position) {
        return ovos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ovos.get(position).getIdOvo();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            View view = act.getLayoutInflater().inflate(R.layout.lista_ovos_personalizada, parent, false);

            ovo = ovos.get(position);

            Log.i("OVOS", "Montando lista de ovos para " + ovo.getCor());

            final ImageView imagem = (ImageView)
                    view.findViewById(R.id.imagemOvoOvos);

            TextView kmAndou = (TextView)
                    view.findViewById(R.id.kmAndou);

            final Button incubar = (Button)
                    view.findViewById(R.id.botaoIncubar);


            if(ovo.getIncubado()) {
                //kmAndou.setText("0km");
                //imagem.setImageResource(ovo.getFotoIncubadora());
                //incubar.setEnabled(false);

            }else {
                //kmAndou.setText("");
                imagem.setImageResource(ovo.getIdTipoOvo());

                incubar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imagem.setImageResource(ovo.getIdTipoOvo());
                        incubar.setEnabled(false);
                        ovo.setIncubado(true);
                    }
                });
            }


            return view;
        }catch (Exception e){
            Log.e("OVO", "ERRO: " + e.getMessage());
            return null;
        }
    }

}