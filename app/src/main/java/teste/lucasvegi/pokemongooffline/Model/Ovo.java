package teste.lucasvegi.pokemongooffline.Model;

import java.io.Serializable;

public class Ovo implements Serializable {
    private int idOvo;
    private int idPokemon;
    private int idTipoOvo;
    private String cor;
    private boolean incubadora;

    public Ovo(int idOvo, int idPokemon, int idTipoOvo, String cor) {
        this.idOvo = idOvo;
        this.idPokemon = idPokemon;
        this.idTipoOvo = idTipoOvo;
        this.cor = cor;
        this.incubadora = false;
    }

    public int getIdOvo(){ return idOvo; }
    public int getidPokemon(){ return idPokemon; }
    public int getIdTipoOvo() { return idTipoOvo; }
    public String getCor(){ return cor; }
    public boolean getIncubado(){ return incubadora;}

    public void setIdOvo(int idOvo) {
        this.idOvo = idOvo;
    }
    public void setidPokemon(int idPokemon) {
        this.idPokemon = idPokemon;
    }
    public void setIdTipoOvo(int idTipoOvo) { this.idTipoOvo = idTipoOvo; }
    public void setCor(String cor) {
        this.cor = cor;
    }
    public void setIncubado(boolean incubadora){this.incubadora = incubadora;}
}
