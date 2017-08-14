package teste.lucasvegi.pokemongooffline.Model;

import java.io.Serializable;

/**
 * Created by Lucas on 08/12/2016.
 */
public class Tipo implements Serializable {
    private int idTipo;
    private String nome;

    public Tipo() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }
}
