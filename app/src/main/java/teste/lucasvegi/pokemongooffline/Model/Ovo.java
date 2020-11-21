package teste.lucasvegi.pokemongooffline.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.io.Serializable;

import teste.lucasvegi.pokemongooffline.Util.BancoDadosSingleton;

public class Ovo implements Serializable {
    private int idOvo;
    private int idPokemon;
    private String idTipoOvo;
    private int incubado;
    private int idFoto, Foto;
    private int idFotoInc, FotoInc;

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

    public int getFoto(){
        Cursor c = BancoDadosSingleton.getInstance().buscar("ovo o, tipoovo t", new String[]{"t.foto ft"}, "o.idTipoOvo = t.idTipoOvo AND o.idOvo = '"+idOvo+"'","");
        while (c.moveToNext()) {
            idFoto = c.getColumnIndex("ft");
            Foto = c.getInt(idFoto);
        }
        c.close();
        return Foto;
    }

    public int getFotoIncubado(){
        Cursor c = BancoDadosSingleton.getInstance().buscar("ovo o, tipoovo t", new String[]{"t.fotoIncubadora ftInc"}, "o.idTipoOvo = t.idTipoOvo AND o.idOvo = '"+idOvo+"'","");
        while (c.moveToNext()) {
            idFotoInc = c.getColumnIndex("ftInc");
            FotoInc = c.getInt(idFotoInc);
        }
        c.close();
        return FotoInc;
    }

    public void setIdOvo(int idOvo) {
        this.idOvo = idOvo;
    }

    public void setidPokemon(int idPokemon) {
        this.idPokemon = idPokemon;
    }

    public void setIdTipoOvo(String idTipoOvo) { this.idTipoOvo = idTipoOvo; }

    public void setIncubado(int inc){
        ContentValues valores = new ContentValues();
        valores.put("incubado", inc);
        Log.i("OVOS", "idOvo: " + idOvo);
        BancoDadosSingleton.getInstance().atualizar("ovo",valores,"idOvo = '"+idOvo+"'");
        this.incubado = inc;
    }
}
