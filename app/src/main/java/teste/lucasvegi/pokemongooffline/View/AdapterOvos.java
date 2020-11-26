package teste.lucasvegi.pokemongooffline.View;

import android.app.Activity;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import teste.lucasvegi.pokemongooffline.Model.ControladoraFachadaSingleton;
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
    public Ovo getItem(int position) {
        return ovos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ovos.get(position).getIdOvo();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            View view = act.getLayoutInflater().inflate(R.layout.lista_ovos_personalizada, parent, false);

            //Log.i("OVOS", "Montando lista de ovos para " + ovo.getIdOvo());

            final ImageView imagem = (ImageView)
                    view.findViewById(R.id.imagemOvoOvos);
            //Log.i("OVOS", "Montando lista de ovos para " + ovo.getIdOvo());
            final TextView kmAndou = (TextView)
                    view.findViewById(R.id.kmAndou);

            final Button incubar = (Button)
                    view.findViewById(R.id.botaoIncubar);


            if(ovos.get(position).getIncubado() == 1) {
                if(ovos.get(position).getChocado() == 0) {
                    imagem.setImageResource(ovos.get(position).getFotoIncubado());
                    kmAndou.setText(String.format("%.2f", ovos.get(position).getKmAndado()) + "/" + String.valueOf(ovos.get(position).getKm()) + "km");
                    incubar.setEnabled(false);
                }

            }else {
                kmAndou.setText(String.valueOf(ovos.get(position).getKm()) + "km");
                    imagem.setImageResource(ovos.get(position).getFoto());

                    incubar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(ControladoraFachadaSingleton.getInstance().quantidadeOvosIncubado() < 3) {
                                imagem.setImageResource(ovos.get(position).getFotoIncubado());
                                incubar.setEnabled(false);
                                kmAndou.setText("0" + "/" + String.valueOf(ovos.get(position).getKm()) + "km");

                                //Log.i("OVOS", "Incubar ovo: " + ovos.get(position).getIdOvo());
                                ovos.get(position).setIncubado(1);
                                ControladoraFachadaSingleton.getInstance().setIncubado(ovos.get(position).getIdOvo(), 1);
                            }
                            else{
                                Toast.makeText(act, "Suas incubadoras estão ocupadas. ", Toast.LENGTH_LONG).show();
                            }
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