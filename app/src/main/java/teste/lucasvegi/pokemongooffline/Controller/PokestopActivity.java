package teste.lucasvegi.pokemongooffline.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import teste.lucasvegi.pokemongooffline.Model.Pokestop;
import teste.lucasvegi.pokemongooffline.R;

public class PokestopActivity extends Activity {
    private TextView placeName;
    private TextView placeInfo;
    private ImageView imgPokestopIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokestop);

        placeName = (TextView) findViewById(R.id.placeName);
        placeInfo = (TextView) findViewById(R.id.placeInfo);
        imgPokestopIcon = (ImageView) findViewById(R.id.imgPokestopIcon);

        Intent it = getIntent();
        Pokestop pokestop = (Pokestop) it.getSerializableExtra("pokestop");
        byte[] byteArray = (byte[]) it.getByteArrayExtra("foto");

        placeName.setText(pokestop.getNome());
        placeInfo.setText(pokestop.getDescri());
        imgPokestopIcon.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
    }

    public void clickReturnBtn(View btnReturn){
        Intent it = new Intent(this, MapActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(it);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(this, MapActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(it);
        finish();
    }
}