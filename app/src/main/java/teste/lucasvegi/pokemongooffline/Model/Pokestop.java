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
import java.util.Calendar;
import java.util.Date;
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
    private transient Bitmap foto = null;
    private Double lat;
    private Double longi;
    private String descri;
    private Date acesso;
    private boolean disponivel;

    public Pokestop(){

    }

    public Pokestop(String ID, String Name) {
        this.id = ID;
        this.nome = Name;
        //this.foto = Photo;
        this.acesso = null;
        this.disponivel = true;
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

    public Date getUltimoAcesso() {
        return acesso;
    }

    public void setUltimoAcesso(Date tempo) {
        this.acesso = tempo;
    }

    public boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disp) {
        this.disponivel = disp;
    }



    public MarkerOptions getMarkerOptions(boolean interactionPossible){
        MarkerOptions markeropt = new MarkerOptions();

        if (interactionPossible && this.disponivel) {
            markeropt.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.pokestop_perto))
                .position(new LatLng(lat, longi))
                .title(nome)
                .alpha(3);
        } else if (!interactionPossible && this.disponivel){
            markeropt.icon(BitmapDescriptorFactory
                .fromResource(R.drawable.pokestop_longe))
                .position(new LatLng(lat, longi))
                .title(nome)
                .alpha(3);
        } else if (interactionPossible && !this.disponivel){
            markeropt.icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.pokestop_perto_unable))
                    .position(new LatLng(lat, longi))
                    .title(nome)
                    .alpha(3);
        } else if (!interactionPossible && !this.disponivel) {
            markeropt.icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.pokestop_longe_unable))
                    .position(new LatLng(lat, longi))
                    .title(nome)
                    .alpha(3);
        }
        return markeropt;
    }
}
