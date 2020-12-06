package teste.lucasvegi.pokemongooffline.Model;

import java.util.ArrayList;

/**
 * Created by Lucas on 08/12/2016.
 */
public class PokemonCapturado {
    private double latitude;
    private double longitude;
    private String dtCaptura;

    protected PokemonCapturado(double latitude, double longitude, String dtCaptura){
        this.latitude = latitude;
        this.longitude = longitude;
        this.dtCaptura = dtCaptura;
    }

    public PokemonCapturado() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDtCaptura() {
        return dtCaptura;
    }

    public void setDtCaptura(String dtCaptura) {
        this.dtCaptura = dtCaptura;
    }
}
