package teste.lucasvegi.pokemongooffline.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Lucas on 14/12/2016.
 */
public class TimeUtil {

    public static Map<String,String> getHoraMinutoSegundoDiaMesAno(){
        Map<String,String> timeStamp = new HashMap<String,String>();
        Date dtAux = Calendar.getInstance().getTime();

        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        //System.out.println(sdf.format(new Date())); //-prints-> 2015-01-22T03:23:26Z

        SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.getDefault());
        timeStamp.put("hora",sdf.format(dtAux)); //add hora

        sdf = new SimpleDateFormat("mm", Locale.getDefault());
        timeStamp.put("minuto",sdf.format(dtAux)); //add minuto

        sdf = new SimpleDateFormat("ss", Locale.getDefault());
        timeStamp.put("segundo",sdf.format(dtAux)); //add segundos

        sdf = new SimpleDateFormat("dd", Locale.getDefault());
        timeStamp.put("dia",sdf.format(dtAux)); //add dia

        sdf = new SimpleDateFormat("MM", Locale.getDefault());
        timeStamp.put("mes",sdf.format(dtAux)); //add mes

        sdf = new SimpleDateFormat("yyyy", Locale.getDefault());
        timeStamp.put("ano", sdf.format(dtAux)); //add ano

        sdf = new SimpleDateFormat("z", Locale.getDefault());
        timeStamp.put("timezone", sdf.format(dtAux)); //add timezone

        return timeStamp;
    }
}
