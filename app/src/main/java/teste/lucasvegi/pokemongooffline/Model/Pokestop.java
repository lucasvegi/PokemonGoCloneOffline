package teste.lucasvegi.pokemongooffline.Model;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import teste.lucasvegi.pokemongooffline.Util.BancoDadosSingleton;

/**
 * Created by Lucas on 02/12/2016.
 */
public class Pokestop implements Serializable{
    private String id;
    private String nome;
    private Bitmap foto;
    private Map<String,String> UltimoAcesso;

    public Pokestop(){

    }

    protected Pokestop(String ID, String Name) {
        this.id = ID;
        this.nome = Name;
        //this.foto = Photo;
        //this.UltimoAcesso = getActualTime;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public Map<String,String> UltimoAcesso() {
        return UltimoAcesso;
    }

    public void setUltimoAcesso(Map<String,String> tempo) {
        this.UltimoAcesso = tempo;
    }


}
