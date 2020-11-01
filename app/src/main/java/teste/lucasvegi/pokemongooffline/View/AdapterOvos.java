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

            Ovo ovo = ovos.get(position);

            Log.i("OVOS", "Montando lista de ovos para " + ovo.getCor());

            ImageView imagem = (ImageView)
                    view.findViewById(R.id.imagemOvoOvos);

            TextView kmAndou = (TextView)
                    view.findViewById(R.id.kmAndou);

            Button botaoAndou = (Button)
                    view.findViewById(R.id.botaoIncubar);


            //Decide se vai ter informações do pokemon ou não
            if(ovo.getIdOvo() != 0) {
                kmAndou.setText("0km");
                imagem.setImageResource(ovo.getFoto());

            }else {
                kmAndou.setText("");
                imagem.setImageResource(R.drawable.help);
            }

            return view;
        }catch (Exception e){
            Log.e("OVO", "ERRO: " + e.getMessage());
            return null;
        }
    }
}