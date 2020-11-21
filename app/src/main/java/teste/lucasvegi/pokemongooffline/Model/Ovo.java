package teste.lucasvegi.pokemongooffline.Model;

import java.io.Serializable;

public class Ovo implements Serializable {
    private int idOvo;
    private int idPokemon;
    private String idTipoOvo;
    private int incubado;

    public Ovo(int idOvo, int idPokemon, String idTipoOvo, int incubado) {
        this.idOvo = idOvo;
        this.idPokemon = idPokemon;
        this.idTipoOvo = idTipoOvo;
        this.incubado = incubado;
    }

    public int getIdOvo(){ return idOvo; }
    public int getidPokemon(){ return idPokemon; }
    public String getIdTipoOvo() { return idTipoOvo; }
    public int getIncubado(){ return incubado;}

    public void setIdOvo(int idOvo) {
        this.idOvo = idOvo;
    }
    public void setidPokemon(int idPokemon) {
        this.idPokemon = idPokemon;
    }
    public void setIdTipoOvo(String idTipoOvo) { this.idTipoOvo = idTipoOvo; }
    public void setIncubado(int incubado){this.incubado = incubado;}
}
