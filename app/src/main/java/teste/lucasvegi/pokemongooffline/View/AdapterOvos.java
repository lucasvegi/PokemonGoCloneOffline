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

import java.util.List;

import teste.lucasvegi.pokemongooffline.Model.Ovo;
import teste.lucasvegi.pokemongooffline.R;

public class AdapterOvos extends BaseAdapter {

    private List<Ovo> ovos;
    private Activity act;
    private Ovo ovo;
    private Location localizacaoRecebida;


    public AdapterOvos(List<Ovo> ovos, Activity act, Location localizacaoAtual) {
        try {
            this.ovos = ovos;
            this.act = act;
            this.localizacaoRecebida = localizacaoAtual;
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
                imagem.setImageResource(ovos.get(position).getFotoIncubado());

                //se a localização for nula i.e. acabou de ser incubado, setto uma localizacao pra ele
                //caso a localização não seja nula, eu att a localização e faço um delta distancia pra settar em kmAndou
                if(ovos.get(position).getLocalizacao() == null) {
                    kmAndou.setText("0" + "/" + String.valueOf(ovos.get(position).getKm()) + "km");
                    ovos.get(position).setLocalizacao(localizacaoRecebida);
                    Log.i("OVOS", "Acabou de ser incubado, localização: " + ovos.get(position).getLocalizacao());
                } else {
                    double distancia = localizacaoRecebida.distanceTo(ovos.get(position).getLocalizacao()) / 1000;
                    Log.i("OVOS", "Distância: " + distancia);
                    ovos.get(position).setKmAndado(ovos.get(position).getKmAndado() + distancia);
                    ovos.get(position).setLocalizacao(localizacaoRecebida);

                    kmAndou.setText(String.format("%.2f", ovos.get(position).getKmAndado()) + "/" + String.valueOf(ovos.get(position).getKm()) + "km");
                    Log.i("OVOS", "Ja estava incubado, localização: " + ovos.get(position).getLocalizacao());
                }
                incubar.setEnabled(false);

            }else {
                kmAndou.setText(String.valueOf(ovos.get(position).getKm()) + "km");
                //Log.i("OVOS", "Entrou no else " + ovos.get(position).getIdOvo());
                    imagem.setImageResource(ovos.get(position).getFoto());

                    incubar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            imagem.setImageResource(ovos.get(position).getFotoIncubado());
                            incubar.setEnabled(false);
                            kmAndou.setText("0" + "/" + String.valueOf(ovos.get(position).getKm()) + "km");
                            //Log.i("OVOS", "Incubar ovo: " + ovos.get(position).getIdOvo());
                            ovos.get(position).setIncubado(1);
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