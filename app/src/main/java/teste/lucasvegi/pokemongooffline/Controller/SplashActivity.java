package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import teste.lucasvegi.pokemongooffline.Model.ControladoraFachadaSingleton;
import teste.lucasvegi.pokemongooffline.R;

public class SplashActivity extends Activity {

    private WebView webView;
    private TextView versaoApp;
    private static int SPLASH_TIME_OUT = 6000;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        configuraViewInicio();
        configuraSomAbertura();


        //espera um tempo antes de ir para a proxima tela
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);*/

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    protected void configuraViewInicio(){
        //Mantem a tela de splash ligada enquanto ela é exibida.
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

        //Configura web view loader carregamento do app
        webView = (WebView) findViewById(R.id.loaderSplash);
        webView.loadUrl("file:///android_asset/loading.gif");
        webView.setBackgroundColor(Color.TRANSPARENT);

        //exibe versão do app dinamicamente
        versaoApp = (TextView) findViewById(R.id.versaoApp);

        //OBTEM A VERSÃO DO APLICATIVO
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            versaoApp.setText("v. " + version);
        }catch (Exception e){
            Log.e("SPLASH","ERRO: " + e.getMessage());
        }
    }

    protected void configuraSomAbertura(){
        try {
            mp = MediaPlayer.create(this, R.raw.abertura2);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //Executa quando a musica de abertura acaba

                    if(ControladoraFachadaSingleton.getInstance().temSessao()) {
                        Intent i = new Intent(getBaseContext(), MapActivity.class);
                        startActivity(i);
                        finish();
                    }else {
                        Intent i = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            });
            mp.start();
        }catch (Exception e){
            Log.e("SPLASH","ERRO: " + e.getMessage());
        }
    }
}
