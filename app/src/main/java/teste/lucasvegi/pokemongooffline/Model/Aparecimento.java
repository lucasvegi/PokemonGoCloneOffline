package teste.lucasvegi.pokemongooffline.Model;

import java.io.Serializable;

/**
 * Created by Lucas on 08/12/2016.
 */
public class Aparecimento implements Serializable {
    private double latitude;
    private double longitude;
    private Pokemon pokemon;

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

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }
}
