package teste.lucasvegi.pokemongooffline.Model;

import java.io.Serializable;

public class Ovo implements Serializable {
    private int idOvo;
    private String cor;
    private String categoria;
    private int foto;
    private int fotoIncubadora;
    private boolean incubadora;
    public Ovo(int idOvo, String cor, String categoria, int foto, int fotoIncubadora) {
        this.idOvo = idOvo;
        this.cor = cor;
        this.categoria = categoria;
        this.foto = foto;
        this.fotoIncubadora = fotoIncubadora;
        this.incubadora = false;
    }

    public int getIdOvo(){ return idOvo; }
    public String getCor(){ return cor; }
    public String getCategoria(){ return categoria; }
    public int getFoto(){ return foto; }
    public int getFotoIncubadora(){ return fotoIncubadora; }
    public boolean getIncubado(){ return incubadora;}

    public void setIdOvo(int idOvo) {
        this.idOvo = idOvo;
    }
    public void setCor(String cor) {
        this.cor = cor;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public void setFoto(int foto) {
        this.foto = foto;
    }
    public void setFotoIncubadora(int fotoIncubadora) {
        this.fotoIncubadora = fotoIncubadora;
    }
    public void setIncubado(boolean incubadora){this.incubadora = incubadora;}
}
