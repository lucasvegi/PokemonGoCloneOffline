package teste.lucasvegi.pokemongooffline.Model;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import teste.lucasvegi.pokemongooffline.R;
import teste.lucasvegi.pokemongooffline.Util.BancoDadosSingleton;

/**
 * Created by Lucas on 02/12/2016.
 */
public class Pokestop implements Serializable{
    private String id;
    private String nome;
    private Bitmap foto = null;
    private Double lat;
    private Double longi;
    private String descri;
    private Map<String,String> UltimoAcesso;

    public Pokestop(){

    }

    public Pokestop(String ID, String Name) {
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

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }

    public double getlat() {
        return lat;
    }

    public void setlat(double lat) {
        this.lat = lat;
    }

    public double getlongi() {
        return longi;
    }

    public void setlong(double longi) {
        this.longi = longi;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLongi() {
        return longi;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }

    public MarkerOptions getMarkerOptions(boolean interactionPossible){
        MarkerOptions markeropt = new MarkerOptions();

        if (interactionPossible) {
            markeropt.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.pokestop_perto))
                .position(new LatLng(lat, longi))
                .title(nome)
                .alpha(3);
        } else {
            markeropt.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.pokestop_longe))
                .position(new LatLng(lat, longi))
                .title(nome)
                .alpha(3);
        }
        return markeropt;
    }
}
