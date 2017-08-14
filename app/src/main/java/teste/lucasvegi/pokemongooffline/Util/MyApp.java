package teste.lucasvegi.pokemongooffline.Util;

import android.app.Application;
import android.content.Context;

/**
 * Created by Lucas on 12/12/2016.
 */
public class MyApp extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        //método usado para recuperar o context do app
        //de qualquer parte do programa
        return MyApp.context;
    }
}
