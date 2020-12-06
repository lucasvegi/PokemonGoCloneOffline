package teste.lucasvegi.pokemongooffline.Model;

import android.database.Cursor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import teste.lucasvegi.pokemongooffline.Util.BancoDadosSingleton;

public class Doce implements Serializable{
    private int idDoce;
    private String nomePkm;
    private int quantidade;

    public Doce(){

    }

    public int getIdDoce() {
        return idDoce;
    }

    public void setIdDoce(int idDoce) {
        this.idDoce = idDoce;
    }

    public String getNomePkm() {
        return nomePkm;
    }

    public void setNomePkm(String nomePkm) {
        this.nomePkm = nomePkm;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    protected Doce(int idDoce, String nomePkm, int quantidade){
        this.idDoce = idDoce;
        this.nomePkm = nomePkm;
        this.quantidade = quantidade;
    }
}
