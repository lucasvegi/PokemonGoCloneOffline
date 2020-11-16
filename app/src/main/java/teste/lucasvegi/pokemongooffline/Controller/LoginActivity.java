package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import teste.lucasvegi.pokemongooffline.Model.ControladoraFachadaSingleton;
import teste.lucasvegi.pokemongooffline.R;

public class LoginActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

    }

    public void clickLogin(View v){
        try {
            Log.i("LOGIN", "Autenticando a entrada no sistema...");

            EditText edtUsuario = (EditText) findViewById(R.id.edtUsuarioLogin);
            EditText edtSenha = (EditText) findViewById(R.id.edtSenhaLogin);

            //Obtem dados do usuário
            String usuario = edtUsuario.getText().toString();
            String senha = edtSenha.getText().toString();

            if (ControladoraFachadaSingleton.getInstance().loginUser(usuario, senha)) {
                Intent it = new Intent(this, MapActivity.class);
                startActivity(it);
                finish();
            } else {
                Toast.makeText(this, "Usuário e/ou senha inválido(s)!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.e("LOGIN", "ERRO: " + e.getMessage());
        }

    }

    public void clickCadastrar(View v){
        Intent it = new Intent(this, CadastrarActivity.class);
        startActivity(it);
        finish();
    }
}
