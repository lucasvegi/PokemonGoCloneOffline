package teste.lucasvegi.pokemongooffline.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.location.Location;
import android.util.Log;

import java.io.Serializable;

import teste.lucasvegi.pokemongooffline.Util.BancoDadosSingleton;

public class Ovo implements Serializable {
    private int idOvo;
    private int idPokemon;
    private String idTipoOvo;
    private int incubado;
    private int chocado;
    private int exibido;
    private int Foto;
    private int FotoInc;
    private double Km;
    private double KmAndado;
    Location localizacao = null;

    public Ovo(int idOvo, int idPokemon, String idTipoOvo, int incubado, int chocado, int exibido,double KmAndado) {
        this.idOvo = idOvo;
        this.idPokemon = idPokemon;
        this.idTipoOvo = idTipoOvo;
        this.incubado = incubado;
        this.chocado = chocado;
        this.exibido = exibido;
        this.KmAndado = KmAndado;
    }

    public int getIdOvo(){ return idOvo; }
    public int getidPokemon(){ return idPokemon; }
    public String getIdTipoOvo() { return idTipoOvo; }
    public int getIncubado(){ return incubado;}

    public int getFoto(){
        Cursor c = BancoDadosSingleton.getInstance().buscar("ovo o, tipoovo t", new String[]{"t.foto ft"}, "o.idTipoOvo = t.idTipoOvo AND o.idOvo = '"+idOvo+"'","");
        while (c.moveToNext()) {
            int idFoto = c.getColumnIndex("ft");
            Foto = c.getInt(idFoto);
        }
        c.close();
        return Foto;
    }

    public int getFotoIncubado(){
        Cursor c = BancoDadosSingleton.getInstance().buscar("ovo o, tipoovo t", new String[]{"t.fotoIncubadora ftInc"}, "o.idTipoOvo = t.idTipoOvo AND o.idOvo = '"+idOvo+"'","");
        while (c.moveToNext()) {
            int idFotoInc = c.getColumnIndex("ftInc");
            FotoInc = c.getInt(idFotoInc);
        }
        c.close();
        return FotoInc;
    }

    public double getKm(){
        Cursor c = BancoDadosSingleton.getInstance().buscar("ovo o, tipoovo t", new String[]{"t.quilometragem km"}, "o.idTipoOvo = t.idTipoOvo AND o.idOvo = '"+idOvo+"'","");
        while (c.moveToNext()) {
            int idKm = c.getColumnIndex("km");
            Km = c.getDouble(idKm);
        }
        c.close();
        return Km;
    }

    public Location getLocalizacao(){
        return localizacao;
    }
    public double getKmAndado(){
        return KmAndado;
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
    public void setLocalizacao(Location localizacao){
        this.localizacao = localizacao;
    }
    public void setKmAndado(double KmAndado){
        this.KmAndado = KmAndado;
    }

    public int getChocado() {
        return chocado;
    }

    public void setChocado(int chocado){
        this.chocado = chocado;
    }

    public int getExibido() {
        return exibido;
    }

    public void setExibido(int exibido) {
        this.exibido = exibido;
    }
}
