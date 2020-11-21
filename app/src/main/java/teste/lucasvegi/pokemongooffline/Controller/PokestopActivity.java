package teste.lucasvegi.pokemongooffline.Controller;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import teste.lucasvegi.pokemongooffline.Model.Pokestop;
import teste.lucasvegi.pokemongooffline.R;
import teste.lucasvegi.pokemongooffline.Util.BancoDadosSingleton;

public class PokestopActivity extends Activity {
    private TextView placeName;
    private TextView placeInfo;
    private ImageView imgPokestopIcon;
    private Date tempoPkstop;
    private Pokestop Pkstp;
    private boolean Pegou = false;
    public String Portuga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokestop);

        placeName = (TextView) findViewById(R.id.placeName);
        placeInfo = (TextView) findViewById(R.id.placeInfo);
        imgPokestopIcon = (ImageView) findViewById(R.id.imgPokestopIcon);
        ContentValues valores = new ContentValues();
        BancoDadosSingleton.getInstance().inserir("Pokestop",valores);
        Intent it = getIntent();
        Pokestop pokestop = (Pokestop) it.getSerializableExtra("pokestop");
        byte[] byteArray = it.getByteArrayExtra("foto");
        Pkstp = pokestop;
        Cursor cTradutor = BancoDadosSingleton.getInstance().buscar("traducao trad",
                new String[]{"trad.portugues portugues"},
                "trad.ingles = '" + pokestop.getDescri() + "'",
                "");
        while (cTradutor.moveToNext()) {
            int coluna = cTradutor.getColumnIndex("portugues");
            Portuga = cTradutor.getString(coluna);
        }
        placeName.setText(pokestop.getNome());
        placeInfo.setText(Portuga);
        if(byteArray != null)
            imgPokestopIcon.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
        tempoPkstop = pokestop.getUltimoAcesso();
        cTradutor.close();
    }

    public void clickReturnBtn(View btnReturn){
        Intent it = new Intent(this, MapActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        it.putExtra("tempo", tempoPkstop);
        it.putExtra("pokestop", Pkstp);
        startActivity(it);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(this, MapActivity.class);
        it.putExtra("tempo", tempoPkstop);
        it.putExtra("pokestop", Pkstp);
        it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(it);
        finish();
    }

    public void PegaOvo(View view) {
        Date TempoAtual = Calendar.getInstance().getTime();

        Cursor cPokestop = BancoDadosSingleton.getInstance().buscar("pokestop pkstp",
                new String[]{"pkstp.idPokestop idPokestop"},
                "pkstp.idPokestop = '" + Pkstp.getID() + "'",
                "");

        //Toast.makeText(this,TempoAtual.toString(),Toast.LENGTH_SHORT).show();
        if (tempoPkstop==null && Pegou==false) {
            //Log.d("PEGA OVOO", "ENTROUU NO PRIMEIRO CASO");
            //atualiza o acesso da Pokestop (passao tempo novo com setUltimoAcesso e passa false pro setDisponivel)
            tempoPkstop = TempoAtual;
            Pkstp.setUltimoAcesso(TempoAtual);
            Atualiza(cPokestop,Pkstp);
            //pega ovo
            Toast.makeText(this,"Pegou Ovo ",Toast.LENGTH_LONG).show();
            Pegou = true;

        } else {
            double diff = TempoAtual.getTime() - tempoPkstop.getTime();
            int diffSec = (int)diff / (1000);
            if (diffSec>300) Pegou = false;
            if (diffSec>300 && Pegou==false){
                //atualiza o acesso da Pokestop (passao tempo novo com setUltimoAcesso e passa false pro setDisponivel)
                tempoPkstop = TempoAtual;
                Pkstp.setUltimoAcesso(TempoAtual);
                Atualiza(cPokestop,Pkstp);
                //pega ovo
                Toast.makeText(this,"Pegou Ovo ",Toast.LENGTH_LONG).show();
                Pegou = true;
            }
            else Toast.makeText(this,"Espere mais "+ String.valueOf(300-diffSec) +" segundos",Toast.LENGTH_SHORT).show();
        }
        Log.i("VALOR QUE E PRA PASSAR ", String.valueOf(TempoAtual.getTime()));

        cPokestop.close();
    }

    public void Atualiza(Cursor cPokestop, Pokestop pkstp){
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = Calendar.getInstance().getTime();
        if (cPokestop.getCount()==0){
            ContentValues valores = new ContentValues();
                valores.put("idPokestop",pkstp.getID());
                valores.put("latitude",pkstp.getlat());
                valores.put("longitude",pkstp.getlongi());
                valores.put("disponivel",pkstp.getDisponivel());
                valores.put("acesso",date.getTime());

            BancoDadosSingleton.getInstance().inserir("Pokestop",valores);
            Log.i("PASSADO O VALOR NOVO D ", String.valueOf(date.getTime()));
            Log.d("POKEACTIVITY","CADASTROU NO BD OU ATUALIZOU");
        } else {
            ContentValues valores = new ContentValues();
                valores.put("disponivel",false);
                valores.put("acesso",String.valueOf(date.getTime()));
            BancoDadosSingleton.getInstance().atualizar("Pokestop",valores,"idPokestop = '"+pkstp.getID()+"'");
            Log.d("POKEACTIVITY","CADASTROU NO BD OU ATUALIZOU");
        }
    }
}

