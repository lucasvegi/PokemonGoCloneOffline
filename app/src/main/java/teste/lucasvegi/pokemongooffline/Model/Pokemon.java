package teste.lucasvegi.pokemongooffline.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import teste.lucasvegi.pokemongooffline.Util.BancoDadosSingleton;

/**
 * Created by Lucas on 02/12/2016.
 */
public class Pokemon implements Serializable{
    private int numero;
    private String nome;
    private String categoria;
    private int foto;
    private int icone;
    private List<Tipo> tipos;
    private int idDoce;
    private int idPokemonBase;
    Pokemon evolucao;

    public Pokemon(){

    }

    protected Pokemon(int numero, String nome, String categoria, int foto, int icone, int idDoce, int idPokemonBase, ControladoraFachadaSingleton cg){
        this.numero = numero;
        this.nome = nome;
        this.categoria = categoria;
        this.foto = foto;
        this.icone = icone;
        this.tipos = new ArrayList<Tipo>();
        this.idDoce = idDoce;
        this.idPokemonBase = idPokemonBase;

        preencherTipos(cg);
    }

    private void preencherTipos(ControladoraFachadaSingleton cg){
        //Select t.idTipo idTipo from pokemon p, tipo t, pokemontipo pt where p.idPokemon = pt.idPokemon and t.idTipo = pt.idTipo and p.idPokemon = numero
        Cursor cTipo = BancoDadosSingleton.getInstance().buscar("pokemon p, tipo t, pokemontipo pt",
                new String[]{"t.idTipo idTipo"},
                "p.idPokemon = pt.idPokemon AND t.idTipo = pt.idTipo AND p.idPokemon = " + this.numero,
                "");

        while (cTipo.moveToNext()){
            int idT = cTipo.getColumnIndex("idTipo");

            //procura o tipo retornado do banco na lista de tipos da controladora geral
            for(Tipo t : cg.getTipos()){
                if(t.getIdTipo() == cTipo.getInt(idT)){
                    this.tipos.add(t);
                }
            }
        }
        cTipo.close();
    }

    public List<Tipo> getTipos() {
        return tipos;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public int getIcone() {
        return icone;
    }

    public void setIcone(int icone) {
        this.icone = icone;
    }

    public int getIdDoce(){return idDoce;}

    public int getIdPokemonBase(){return idPokemonBase;}

    public Pokemon getEvolucao() {
        Cursor cPkmn = BancoDadosSingleton.getInstance().buscar("pokemon p, pokemon pe",
                new String[]{"pe.idPokemon idPokemon", "pe.nome nome", "pe.categoria categoria", "pe.foto foto", "pe.icone icone",
                        "pe.idDoce idDoce", "pe.idPokemonBase idPokemonBase"},
                "pe.idPokemonBase = p.idPokemon AND pe.idPokemonBase = " + this.numero, "");

        //Se a busca não encontrar nada, já retorna null
        if(!cPkmn.moveToNext()) {
            cPkmn.close();
            return null;
        }

        //Caso especial: Eevee (possui 3 evoluções diretas)
        if(this.nome.equals("Eevee")) {
            Random evolEeve = new Random();
            int n = evolEeve.nextInt(3);
            Log.i("EVOLUCAO EEVVEE", "n = " + n);
            while (n>0){
                cPkmn.moveToNext();
                n--;
            }
        }

        int numero = cPkmn.getColumnIndex("idPokemon");
        int nome = cPkmn.getColumnIndex("nome");
        int categoria = cPkmn.getColumnIndex("categoria");
        int foto = cPkmn.getColumnIndex("foto");
        int icone = cPkmn.getColumnIndex("icone");
        int idDoce = cPkmn.getColumnIndex("idDoce");
        int idPokemonBase = cPkmn.getColumnIndex("idPokemonBase");

        evolucao = new Pokemon(cPkmn.getInt(numero),cPkmn.getString(nome),cPkmn.getString(categoria),
                cPkmn.getInt(foto),cPkmn.getInt(icone),cPkmn.getInt(idDoce), cPkmn.getInt(idPokemonBase),
                ControladoraFachadaSingleton.getInstance());

        cPkmn.close();

        return evolucao;
    }

    public boolean estaDisponivel(boolean atualizarFlagEvoluido){
        // SELECT pu.login login, pu.idPokemon idPokemon, pu.latitude latitude,pu.longitude longitude,pu.dtCaptura dtCaptura
        // FROM pokemon p, pokemonusuario pu
        // WHERE pu.evoluido = 0 AND p.idPokemon = pu.idPokemon AND pu.idPokemon = this.numero
        Cursor c = BancoDadosSingleton.getInstance().buscar("pokemon p, pokemonusuario pu",
                new String[]{"pu.login login", "pu.idPokemon idPokemon", "pu.latitude latitude",
                        "pu.longitude longitude","pu.dtCaptura dtCaptura" },
                "pu.evoluido = 0 AND p.idPokemon = pu.idPokemon AND pu.idPokemon = " + this.numero, "");

        //Se a busca não encontrar nada, retorna false
        if(!c.moveToNext()){
            c.close();
            return false;
        }

        // Atualizando a flag evoluido para true
        if(atualizarFlagEvoluido) {
            int login = c.getColumnIndex("login");
            int idPokemon = c.getColumnIndex("idPokemon");
            int latitude = c.getColumnIndex("latitude");
            int longitude = c.getColumnIndex("longitude");
            int dtCaptura = c.getColumnIndex("dtCaptura");

            //Prepara valores para serem persistidos no banco
            ContentValues valores = new ContentValues();
            valores.put("login", c.getString(login));
            valores.put("idPokemon", c.getInt(idPokemon));
            valores.put("latitude", c.getDouble(latitude));
            valores.put("longitude", c.getDouble(longitude));
            valores.put("dtCaptura", c.getString(dtCaptura));
            valores.put("evoluido", 1);

            //Atualiza o banco
            BancoDadosSingleton.getInstance().atualizar("pokemonusuario",valores,"login = '" + c.getString(login) +
                    "' AND idPokemon = '" + c.getInt(idPokemon) + "' AND dtCaptura = '" + c.getString(dtCaptura) + "'");
        }

        c.close();
        return true;
    }

    public int getQuantDocesNecessarios() {
        switch (this.categoria) {
            case "C":
                return 25;
            case "I":
                return 50;
            case "R":
                return 75;
            default:
                return 100;
        }
    }

    public int getQuantDocesObtidos(){
        Cursor cDoce = BancoDadosSingleton.getInstance().buscar("pokemon p, doce d",
                new String[]{"d.quant quant"},
                "p.idDoce = d.idDoce and d.idDoce = '" + this.getIdDoce() + "'",null);
        cDoce.moveToNext(); //obs: fora do while pois deve haver apenas uma linha de resposta

        return cDoce.getInt(cDoce.getColumnIndex("quant"));
    }

    @Override
    public boolean equals(Object obj) {
        try {
            //Verificando se o segundo participante está nulo
            if (obj == null)
                return false;

            //verifica se são da mesma classe
            if (this.getClass() != obj.getClass())
                return false;

            //verifica se ocupam o mesmo lugar na memória
            if (super.equals(obj))
                return true;

            Pokemon pkmn = (Pokemon) obj;

            //Compara os dois objetos pelo estado interno
            if(this.getNumero() == pkmn.getNumero())
                return true;
            else
                return false;

        }catch (Exception e){
            return false;
        }
    }

    @Override
    public int hashCode() {
        //geração própria da hashCode para evitar colisão - objetos de classes diferentes com o mesmo hashCode
        //evita também que NÃO se retorne o mesmo hashCode para o mesmo objeto
        try {
            int numPrimo = 17;
            int hash = 1;

            //TÉCNICA: somar os hashCodes de todos os atributos da classe e multiplicar por um número primo
            hash = numPrimo * hash + ((this.nome == null) ? 0 : this.nome.hashCode());
            hash = numPrimo * hash + ((this.categoria == null) ? 0 : this.categoria.hashCode());
            hash = numPrimo * hash + (this.numero);
            hash = numPrimo * hash + (this.foto);
            hash = numPrimo * hash + (this.icone);
            hash = numPrimo * hash + (this.tipos.get(0).getIdTipo());
            hash = numPrimo * hash + ((this.tipos.get(0).getNome() == null) ? 0 : this.tipos.get(0).getNome().hashCode());
            if (this.tipos.size() > 1){
                hash = numPrimo * hash + (this.tipos.get(1).getIdTipo());
                hash = numPrimo * hash + ((this.tipos.get(1).getNome() == null) ? 0 : this.tipos.get(1).getNome().hashCode());
            }

            return hash;
        }catch (Exception e){
            return super.hashCode();
        }
    }
}
