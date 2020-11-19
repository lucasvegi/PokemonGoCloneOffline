package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import teste.lucasvegi.pokemongooffline.Model.ControladoraFachadaSingleton;
import teste.lucasvegi.pokemongooffline.Model.Pokemon;
import teste.lucasvegi.pokemongooffline.Model.PokemonCapturado;
import teste.lucasvegi.pokemongooffline.Model.Usuario;
import teste.lucasvegi.pokemongooffline.R;
import teste.lucasvegi.pokemongooffline.Util.BancoDadosSingleton;


public class PerfilActivity extends Activity {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private int xpMaxBar = 0;
    private String xpNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);     //Referência da barra de xp
        TextView txtXp = (TextView) findViewById(R.id.txtXp);           //Referência da textView de xp
        TextView txtNivel = (TextView) findViewById(R.id.txtNivel);     //Referência da textView de nível

        Log.d("usuario", "XP: " + ControladoraFachadaSingleton.getInstance().getUsuario().getXp());
        Log.d("usuario", "Nivel: " + ControladoraFachadaSingleton.getInstance().getUsuario().getNivel());

        ControladoraFachadaSingleton.getInstance().aumentaXp("evolui");
        Toast.makeText(getBaseContext(),"Você ganhou " +
                ControladoraFachadaSingleton.getInstance().getXpEvento("evolui") + " de XP",Toast.LENGTH_SHORT).show();

        Cursor user = BancoDadosSingleton.getInstance().buscar("usuario", new String[]{"nivel", "xp"},
                "login= '" + ControladoraFachadaSingleton.getInstance().getUsuario().getLogin()+"'", "");

        while(user.moveToNext()) {
            int idxp = user.getColumnIndex("xp");
            int idnivel = user.getColumnIndex("nivel");

            progressStatus = user.getInt(idxp);                         //Armazena o xp atual do usuário após a captura
            xpMaxBar = ControladoraFachadaSingleton.getInstance().xpMaximo(ControladoraFachadaSingleton.getInstance().getUsuario().getNivel());      //Calcula o valor máximo da barra (importante para que ela termine quando o usuário upar)
            progressBar.setMax(xpMaxBar);                               //Setando o valor máximo da progressBar
            progressBar.setProgress(progressStatus);                    //Setando o progresso da progressBar de acordo com o xp atual + xp de captura
            xpNumber = Integer.toString(progressStatus) + "/" + Integer.toString(xpMaxBar);   // Criando a string da textView do xp

            txtXp.setText(xpNumber);                                                                                //Setando a textView do xp para xpAtual/xpMáximo
            txtNivel.setText("Nível " + ControladoraFachadaSingleton.getInstance().getUsuario().getNivel());        //Setando a textView do nível para o nível atual do usuário

            Log.d("usuario","XP banco: " + user.getInt(idxp));
            Log.d("usuario","Nivel banco: " + user.getInt(idnivel));
        }

        // Obtem referências das views
        ImageView imageView = (ImageView) findViewById(R.id.imgTreinadorPerfil);
        TextView txtInicioAventura = (TextView) findViewById(R.id.txtInicioAventuraPerfil);
        TextView txtNumCapturas = (TextView) findViewById(R.id.txtNumCapturasPerfil);
        TextView txtNomeTreinador = (TextView) findViewById(R.id.txtNomeTreinadorPerfil);

        try {
            //Define o nome do treinador
            txtNomeTreinador.setText(ControladoraFachadaSingleton.getInstance().getUsuario().getLogin());

            //Define a imagem do perfil baseando-se no sexo do usuário
            if(ControladoraFachadaSingleton.getInstance().getUsuario().getSexo().equals("M"))
                imageView.setImageResource(R.drawable.male_grande);
            else
                imageView.setImageResource(R.drawable.female_grande);

            //Define o início da aventura
            txtInicioAventura.setText(ControladoraFachadaSingleton.getInstance().getUsuario().getDtCadastro());

            //Define o número de pokemons capturados pelo usuário
            int contCaptura = 0;
            for (Map.Entry<Pokemon,List<PokemonCapturado>> entry : ControladoraFachadaSingleton.getInstance().getUsuario().getPokemons().entrySet()){
                contCaptura += entry.getValue().size();
            }
            txtNumCapturas.setText(contCaptura+"");

        }catch (Exception e){
            Log.e("PERFIL", "ERRO: " + e.getMessage());
        }
    }

    public void clickLogout(View v){
        try {
            Log.i("LOGOUT", "Saindo...");

            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setTitle("SAIR");
            alerta.setMessage("Deseja finalizar essa sessão?");

            //Configura ação para confirmação positiva
            alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ControladoraFachadaSingleton.getInstance().logoutUser();
                    setResult(MapActivity.MENU_PERFIL);

                    finish();
                }
            });

            //Configura ação para negação da ação
            alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            //Exibe janela de confirmação
            alerta.show();

        }catch (Exception e){
            Log.e("LOGOUT", "ERRO: " + e.getMessage());
        }

    }

    public void clickVoltar(View v){
        finish();
    }
}
