package teste.lucasvegi.pokemongooffline.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.model.PlacesSearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import teste.lucasvegi.pokemongooffline.Util.BancoDadosSingleton;
import teste.lucasvegi.pokemongooffline.Util.MyApp;
import teste.lucasvegi.pokemongooffline.Util.NearbySearch;
import teste.lucasvegi.pokemongooffline.Util.RandomUtil;
import teste.lucasvegi.pokemongooffline.Util.TimeUtil;

/**
 * Created by Lucas on 08/12/2016.
 */
public final class ControladoraFachadaSingleton {
    private Usuario user;
    private Map<String,List<Pokemon>> pokemons;
    private Aparecimento[] aparecimentos = new Aparecimento[10];
    private List<Tipo> tiposPokemon;
    private static ControladoraFachadaSingleton INSTANCE = new ControladoraFachadaSingleton();
    private boolean sorteouLendario = false;
    private List<Ovo> ovos;


    private List<Doce> doces;

    private void daoDoce(){
        this.doces = new ArrayList<Doce>();

        Cursor c = BancoDadosSingleton.getInstance().buscar("doce",new String[]{"idDoce","nome","quant"},"","");

        while(c.moveToNext()){
            int idD = c.getColumnIndex("idDoce");
            int nome = c.getColumnIndex("nome");
            int quant = c.getColumnIndex("quant");

            Doce d = new Doce();
            d.setIdDoce(c.getInt(idD));
            d.setNomePkm(c.getString(nome));
            d.setQuantidade(c.getInt(quant));

            this.doces.add(d);
        }

        c.close();

        //TODO: apagar testes de impressão doce

        //IMPRIME DE TESTE
        for (Doce d : doces){
            Log.d("DOCES",d.getNomePkm() + ": " + d.getQuantidade());
        }
    }

    private ControladoraFachadaSingleton() {
        daoDoce();
        daoTipo();
        daoPokemons(this);
        daoOvo();
    }

    private void daoOvo(){
        this.ovos = new ArrayList<>();

        Cursor c = BancoDadosSingleton.getInstance().buscar("ovo",new String[]{"idOvo","idPokemon","idTipoOvo","incubado","chocado","exibido","KmAndado"},"exibido = 0","");

        while(c.moveToNext()){
            int idO = c.getColumnIndex("idOvo");
            int idP = c.getColumnIndex("idPokemon");
            int idTO = c.getColumnIndex("idTipoOvo");
            int idInc = c.getColumnIndex("incubado");
            int idCho = c.getColumnIndex ("chocado");
            int idExi = c.getColumnIndex ("exibido");
            int idKmAnd = c.getColumnIndex ("KmAndado");

            ovos.add(new Ovo(c.getInt(idO), c.getInt(idP), c.getString(idTO),c.getInt(idInc),c.getInt(idCho),c.getInt(idExi),c.getDouble(idKmAnd)));
        }

        c.close();

    }

    private void daoTipo(){
        this.tiposPokemon = new ArrayList<>();

        Cursor c = BancoDadosSingleton.getInstance().buscar("tipo",new String[]{"idTipo","nome"},"","");

        while(c.moveToNext()){
            int idT = c.getColumnIndex("idTipo");
            int name = c.getColumnIndex("nome");

            Tipo t = new Tipo();
            t.setIdTipo(c.getInt(idT));
            t.setNome(c.getString(name));

            this.tiposPokemon.add(t);
        }

        c.close();

        //TODO: apagar testes de impressão tipo

        //IMPRIME DE TESTE
        for (Tipo tp : tiposPokemon){
            Log.d("TIPOS",tp.getNome());
        }
    }

    private void daoPokemons(ControladoraFachadaSingleton controladorGeral){
        pokemons = new HashMap<String,List<Pokemon>>();

        Cursor c = BancoDadosSingleton.getInstance().buscar("pokemon",new String[]{"idPokemon","nome","categoria","foto","icone","idDoce","idPokemonBase"},"","");

        while(c.moveToNext()){
            int idP = c.getColumnIndex("idPokemon");
            int name = c.getColumnIndex("nome");
            int cat = c.getColumnIndex("categoria");
            int foto = c.getColumnIndex("foto");
            int icone = c.getColumnIndex("icone");
            int idDoce = c.getColumnIndex("idDoce");
            int idPokemonBase = c.getColumnIndex("idPokemonBase");

            Pokemon p = new Pokemon(c.getInt(idP),c.getString(name),c.getString(cat),c.getInt(foto),c.getInt(icone),c.getInt(idDoce), c.getInt(idPokemonBase),controladorGeral);

            //verifica se lista de alguma categoria ainda não existe
            if(pokemons.get(p.getCategoria()) == null)
                pokemons.put(p.getCategoria(),new ArrayList<Pokemon>());

            //adiciona o pokemon na lista da sua categoria
            pokemons.get(p.getCategoria()).add(p);
        }

        c.close();

        //TODO: apagar testes de impressão pokemon

        //IMPRIME POKEMONS COMUNS
        for(Pokemon pokemon : pokemons.get("C")){
            String tipos = "";
            for (Tipo tp :  pokemon.getTipos()){
                tipos += tp.getNome();
                tipos += "/";
            }
            Log.d("POKEMONS", pokemon.getNumero() + " - " + pokemon.getNome() + " - " + pokemon.getCategoria() + " - " + pokemon.getFoto() + " - " + pokemon.getIcone() + " - " + tipos);
        }

        //IMPRIME POKEMONS INCOMUNS
        for(Pokemon pokemon : pokemons.get("I")){
            String tipos = "";
            for (Tipo tp :  pokemon.getTipos()){
                tipos += tp.getNome();
                tipos += "/";
            }
            Log.d("POKEMONS", pokemon.getNumero() + " - " + pokemon.getNome() + " - " + pokemon.getCategoria() + " - " + pokemon.getFoto() + " - " + pokemon.getIcone() + " - " + tipos);
        }

        //IMPRIME POKEMONS RAROS
        for(Pokemon pokemon : pokemons.get("R")){
            String tipos = "";
            for (Tipo tp :  pokemon.getTipos()){
                tipos += tp.getNome();
                tipos += "/";
            }
            Log.d("POKEMONS", pokemon.getNumero() + " - " + pokemon.getNome() + " - " + pokemon.getCategoria() + " - " + pokemon.getFoto() + " - " + pokemon.getIcone() + " - " + tipos);
        }

        //IMPRIME POKEMONS LENDARIOS
        for(Pokemon pokemon : pokemons.get("L")){
            String tipos = "";
            for (Tipo tp :  pokemon.getTipos()){
                tipos += tp.getNome();
                tipos += "/";
            }
            Log.d("POKEMONS", pokemon.getNumero() + " - " + pokemon.getNome() + " - " + pokemon.getCategoria() + " - " + pokemon.getFoto() + " - " + pokemon.getIcone() + " - " + tipos);
        }
    }

    private void daoUsuario(){

        Cursor c = BancoDadosSingleton.getInstance().buscar("usuario",new String[]{"login","senha","nome","sexo","foto","dtCadastro","xp","nivel"},"","");

        while(c.moveToNext()){
            int login = c.getColumnIndex("login");
            int pass = c.getColumnIndex("senha");
            int name = c.getColumnIndex("nome");
            int sexo = c.getColumnIndex("sexo");
            int foto = c.getColumnIndex("foto");
            int dtCad = c.getColumnIndex("dtCadastro");
            int xp = c.getColumnIndex("xp");
            int nivel = c.getColumnIndex("nivel");

            user = new Usuario(c.getString(login));

            user.setSenha(c.getString(pass));
            user.setNome(c.getString(name));
            user.setSexo(c.getString(sexo));
            user.setFoto(c.getString(foto)); //IMPLEMENTAR RETIRAR FOTO DE USUÁRIO NO CADASTRO
            user.setDtCadastro(c.getString(dtCad));
            user.setXp(c.getInt(xp));
            user.setNivel(c.getInt(nivel));
        }

        c.close();

    }

    public static ControladoraFachadaSingleton getInstance(){
        return INSTANCE;
    }

    public Usuario getUsuario(){
        return this.user;
    }

    public InteracaoPokestop interagePokestop(Pokestop p, Date acesso){
        ContentValues valores = new ContentValues();

        valores.put("ultimoAcesso",acesso.getTime());

        Cursor cPokestop = BancoDadosSingleton.getInstance().buscar("interacaopokestop ip",
                new String[]{"ip.idPokestop idPokestop"},
                "ip.idPokestop = '" + p.getID() + "' and ip.loginUsuario = '"+user.getLogin()+"'",
                "");

        if(cPokestop.getCount() > 0){
            BancoDadosSingleton.getInstance().atualizar("interacaopokestop",
                    valores,
                    "idPokestop = '"+p.getID()+"' and "+
                            "loginUsuario='"+user.getLogin()+"'"
            );
        }
        else{
            valores.put("idPokestop",p.getID());
            valores.put("loginUsuario",user.getLogin());
            BancoDadosSingleton.getInstance().inserir("interacaopokestop",valores);
        }


        InteracaoPokestop interacaoPokestop = new InteracaoPokestop(p, user, acesso);
        p.setDisponivel(false);
        aumentaXp("pokestop");
        sorteiaOvo();
        return interacaoPokestop;
    }

    public InteracaoPokestop getUltimaInteracao(Pokestop p){
        Cursor cPokestop = BancoDadosSingleton.getInstance().buscar("interacaopokestop",
                new String[]{"ultimoAcesso"},
                "idPokestop = '" + p.getID() + "' and loginUsuario = '"+user.getLogin()+"'",
                "");

        Date access = null;
        while(cPokestop.getCount() > 0 && cPokestop.moveToNext()){
            int cAcesso = cPokestop.getColumnIndex("ultimoAcesso");

            access = new Date( cPokestop.getLong(cAcesso)) ;
        }

        if(access == null)
            p.setDisponivel(true);
        else{
            Date TempoAtual = Calendar.getInstance().getTime();
            double diff = TempoAtual.getTime() - access.getTime();
            int diffSec = (int)diff/1000;
            if(diffSec > 300)
                p.setDisponivel(true);
            else
                p.setDisponivel(false);
        }

        InteracaoPokestop interac = new InteracaoPokestop(p, user, access);
        return interac;
    }

    public List<Pokemon> getPokemons(){
        //extrai do MAP todos os valores pokemon e junta em uma lista ordenada para retornar.
        List<Pokemon> pkmn = new ArrayList<>();

        for (Map.Entry<String, List<Pokemon>> entry : pokemons.entrySet()){
            //add listas de pokemon no final da lista a ser retornada
            pkmn.addAll(entry.getValue());
        }

        //ordena a lista a ser retornada baseado no número do pokemon
        Collections.sort(pkmn, new Comparator<Pokemon>() {
            @Override
            public int compare(Pokemon pk2, Pokemon pk1) {
                if(pk1.getNumero() > pk2.getNumero())
                    return -1;
                else if(pk1.getNumero() < pk2.getNumero())
                    return +1;
                return 0;
            }
        });

        //TODO: apagar testes de impressão lista pokemon
        for(Pokemon p : pkmn){
            Log.d("LISTA_PKMN", "Pokemon: " + p.getNome());
        }

        return pkmn;
    }

    public List<Pokestop> getPokestops(double latitude, double longitude){
        List<Pokestop> list = new ArrayList<Pokestop>();

        PlacesSearchResult[] placesSearchResults = NearbySearch.run(new com.google.maps.model.LatLng(latitude, longitude)).results;
        if(placesSearchResults != null) {
            for (int i = 0; placesSearchResults != null && i < placesSearchResults.length / 2; i++) {
                double lat = placesSearchResults[i].geometry.location.lat;
                double lng = placesSearchResults[i].geometry.location.lng;

                Pokestop pokestop = new Pokestop(placesSearchResults[i].placeId, placesSearchResults[i].name);
                pokestop.setlat(lat);
                pokestop.setlong(lng);
                if (placesSearchResults[i].types != null && placesSearchResults[i].types.length > 0)
                    pokestop.setDescri(placesSearchResults[i].types[0]);

                //TODO : setar imagem do pokestop dentro da classe do pokestop
                if (placesSearchResults[i].photos != null && placesSearchResults[i].photos.length > 0)
                    getPlaceImage(pokestop);

                Cursor cPokestop = BancoDadosSingleton.getInstance().buscar("pokestop pkstp",
                        new String[]{"pkstp.disponivel disponivel"},
                        "pkstp.idPokestop = '" + pokestop.getID() + "'",
                        "");
                if (cPokestop.getCount() > 0) {
                    while (cPokestop.moveToNext()) {
                        int coluna = cPokestop.getColumnIndex("disponivel");
                        if (cPokestop.getInt(coluna) == 0) {
                            pokestop.setDisponivel(false);
                        } else
                            pokestop.setDisponivel(true);
                    }
                }
                else{
                    ContentValues valores = new ContentValues();
                    valores.put("idPokestop",pokestop.getID());
                    valores.put("latitude",pokestop.getlat());
                    valores.put("longitude",pokestop.getlongi());
                    valores.put("disponivel",true);

                    long id = BancoDadosSingleton.getInstance().inserir("Pokestop",valores);
                    Log.d("POKEACTIVITY","CADASTROU NO BD OU ATUALIZOU COM ID = "+id);
                }

                InteracaoPokestop it = getUltimaInteracao(pokestop);
                //atualizar se eh possivel interagir em questao de tempo
                if (it.getUltimoAcesso() != null) {
                    Date TempoAtual = Calendar.getInstance().getTime();
                    double diff = TempoAtual.getTime() - it.getUltimoAcesso().getTime();
                    int diffSec = (int) diff / (1000);
                    if (diffSec> 300) {
                        pokestop.setDisponivel(true);
                    }
                    else
                        pokestop.setDisponivel(false);
                }

                list.add(pokestop);

            }
        }
        return list;
    }

    //Recuperar imagem do local usando sdk places
    private void getPlaceImage(Pokestop pokestop) {
        //calcula a distancia e ve se eh valido interagir
        // Inicializa o SDK
        Places.initialize(MyApp.getAppContext(), "AIzaSyD_82FN8rMIJzMrZyx1l7xZbpW1SYN5pdU");
        // Instancia Placesclient
        PlacesClient placesClient = Places.createClient(MyApp.getAppContext());

        List<Place.Field> fields = Arrays.asList(Place.Field.PHOTO_METADATAS);

        FetchPlaceRequest placeRequest = FetchPlaceRequest.builder(pokestop.getID(), fields).build();

        // faz request pra imagem, depois ve um tamanho bom pra padronizar as imagens
        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            // Get the photo metadata.
            List<PhotoMetadata> list = place.getPhotoMetadatas();
            if (list != null && list.size() > 0) {
                PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);
                // Get the attribution text.
                String attributions = photoMetadata.getAttributions();
                // Create a FetchPhotoRequest.
                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(500) // Optional.
                        .setMaxHeight(300) // Optional.
                        .build();
                placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                    Bitmap bitmap = fetchPhotoResponse.getBitmap();
                    // PASSAR A IMAGEM PRO POKESTOP
                    pokestop.setFoto(bitmap);
                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        int statusCode = apiException.getStatusCode();
                        // Erro
                        Log.e("TAG", "Lugar nao encontrado: " + exception.getMessage());
                    }
                });
            }

        });
    }

    public Aparecimento[] getAparecimentos(){
        return this.aparecimentos;
    }

    protected List<Tipo> getTipos(){
        return tiposPokemon;
    }

    public List<Doce> getDoces() {
        return doces;
    }

    public List<Ovo> getOvos(){ return ovos; }

    public void removeOvo(int i){
        Ovo o = ovos.get(i);
        ovos.remove(o);
    }

    public Pokemon getPokemonOvo(int idOvo){
        Pokemon p = null;
        Cursor c = BancoDadosSingleton.getInstance().buscar("pokemon p, ovo o",new String[]{"p.idPokemon idPokemon","p.nome nome","p.categoria categoria","p.foto foto","p.icone icone","p.idDoce idDoce", "p.idPokemonBase idPokemonBase"},"o.idPokemon = p.idPokemon AND o.idOvo = '"+idOvo+"'","");
        while (c.moveToNext()) {
            int idP = c.getColumnIndex("idPokemon");
            int name = c.getColumnIndex("nome");
            int cat = c.getColumnIndex("categoria");
            int foto = c.getColumnIndex("foto");
            int icone = c.getColumnIndex("icone");
            int idDoce = c.getColumnIndex("idDoce");
            int idPokemonBase = c.getColumnIndex("idPokemonBase");

            p = new Pokemon(c.getInt(idP),c.getString(name),c.getString(cat),c.getInt(foto),c.getInt(icone),c.getInt(idDoce),c.getInt(idPokemonBase),this);
            Log.i("GET", "Nome: " + p.getNome());

        }
        c.close();
        return p;
    }

    public void setIncubado(int idOvo,int incubado){
        ContentValues valores = new ContentValues();
        valores.put("incubado",incubado);
        BancoDadosSingleton.getInstance().atualizar("ovo",valores,"idOvo = '"+idOvo+"'");
    }

    public void setExibido(int idOvo,int exibido){

        ContentValues valores = new ContentValues();
        valores.put("exibido",exibido);
        BancoDadosSingleton.getInstance().atualizar("ovo",valores,"idOvo = '"+idOvo+"'");

    }

    public void setChocado(int idOvo,int chocado){

        ContentValues valores = new ContentValues();
        valores.put("chocado",chocado);
        BancoDadosSingleton.getInstance().atualizar("ovo",valores,"idOvo = '"+idOvo+"'");

    }

    public void setKmAndado(int idOvo, double kmAndado){
        ContentValues valores = new ContentValues();
        valores.put("kmAndado",kmAndado);
        BancoDadosSingleton.getInstance().atualizar("ovo",valores,"idOvo = '"+idOvo+"'");
    }

    public int quantidadeOvosIncubado(){
        int quantidadeOvosIncubado = 0;
        for(int i = 0; i < ovos.size(); i++){
            if(ovos.get(i).getIncubado() == 1) quantidadeOvosIncubado++;
        }
        Log.i("INCUBADO:","Quantidade de ovos incubados: "+ quantidadeOvosIncubado);
        return quantidadeOvosIncubado;
    }

    public void sorteiaOvo(){

        int tamComum = pokemons.get("C").size();
        int tamIncomum = pokemons.get("I").size();
        int tamRaro = pokemons.get("R").size();
        int tamLendario = pokemons.get("L").size();

        //TO DO: idOvo precisa ser definido de alguma forma dentro deste método
        int idOvo = 0;

        //Monkey patch para definir id imitando autoincrement
        Cursor c = BancoDadosSingleton.getInstance().buscar("ovo",new String[]{"idOvo"}, "","");
        while (c.moveToNext()){
            idOvo++;
        }

        Log.d("SORTEIO","C: " + tamComum + " I: "+ tamIncomum + " R: "+ tamRaro + " L: " + tamLendario);

        int min = 0;
        int max;

        //obtem hora atual
        Map<String,String> tempo = TimeUtil.getHoraMinutoSegundoDiaMesAno();

        Log.d("TEMPO",tempo.get("hora") +":"+tempo.get("minuto")+":"+tempo.get("segundo")+" - "+tempo.get("dia")+"/"+tempo.get("mes")+"/"+tempo.get("ano")+" "+tempo.get("timezone"));

        //obtem valores a serem usado no critério de lendários
        int numIntSorteado = RandomUtil.randomIntInRange(1,101);
        int numIntSorteado2 = RandomUtil.randomIntInRange(1,101);
        int somaMinSegAtual = (Integer.parseInt(tempo.get("minuto")) + Integer.parseInt(tempo.get("segundo")));

        Log.d("SORTEIO","NumInt: " + numIntSorteado + " NumInt2: " + numIntSorteado2 + " SomaMinSeg: " + somaMinSegAtual);

        //sorteia OVO LENDÁRIO
        if(numIntSorteado % 2 == 0 && numIntSorteado2 % 2 == 0 && somaMinSegAtual % 2 != 0){
            max = tamLendario;
            int sorteio = RandomUtil.randomIntInRange(min,max);
            int idP = pokemons.get("L").get(sorteio).getNumero();

            cadastraOvo(idOvo, idP, "L", 0);   //colocar idOvo auto incremento?

            Log.d("SORTEIO","LENDÁRIO: " + pokemons.get("L").get(sorteio).getNome());

        //sorteia OVO RARO
        } else if(numIntSorteado % 2 != 0 && numIntSorteado2 % 2 != 0){
            max = tamRaro;
            int sorteio = RandomUtil.randomIntInRange(min,max);
            int idP = pokemons.get("R").get(sorteio).getNumero();

            cadastraOvo(idOvo, idP, "R", 0);   //colocar idOvo auto incremento?

            Log.d("SORTEIO","LENDÁRIO: " + pokemons.get("R").get(sorteio).getNome());

        //sorteia OVO INCOMUM
        } else if(numIntSorteado <= 35){
            max = tamIncomum;
            int sorteio = RandomUtil.randomIntInRange(min,max);
            int idP = pokemons.get("I").get(sorteio).getNumero();

            cadastraOvo(idOvo, idP, "I", 0);   //colocar idOvo auto incremento?

            Log.d("SORTEIO","LENDÁRIO: " + pokemons.get("I").get(sorteio).getNome());

         //Sorteia OVO COMUM
        } else {
            max = tamComum;
            int sorteio = RandomUtil.randomIntInRange(min,max);
            int idP = pokemons.get("C").get(sorteio).getNumero();

            //cadastraOvo(idOvo, idP, "C", 0);   //colocar idOvo auto incremento?

            Log.d("SORTEIO","LENDÁRIO: " + pokemons.get("C").get(sorteio).getNome());
        }
    }


    public void sorteiaAparecimentos(double LatMin, double LatMax, double LongMin, double LongMax){

        int tamComum = pokemons.get("C").size();
        int tamIncomum = pokemons.get("I").size();
        int tamRaro = pokemons.get("R").size();
        int tamLendario = pokemons.get("L").size();

        Log.d("SORTEIO","C: " + tamComum + " I: "+ tamIncomum + " R: "+ tamRaro + " L: " + tamLendario);

        int contAparecimentos = 0;
        int totalComuns = 0; //valor a ser definido no sorteio de lendário

        int min = 0;
        int max;

        //obtem hora atual
        Map<String,String> tempo = TimeUtil.getHoraMinutoSegundoDiaMesAno();
        Log.d("TEMPO",tempo.get("hora") +":"+tempo.get("minuto")+":"+tempo.get("segundo")+" - "+tempo.get("dia")+"/"+tempo.get("mes")+"/"+tempo.get("ano")+" "+tempo.get("timezone"));

        //obtem valores a serem usado no critério de lendários
        int numIntSorteado = RandomUtil.randomIntInRange(1,101);
        int numIntSorteado2 = RandomUtil.randomIntInRange(1,101);
        int somaMinSegAtual = (Integer.parseInt(tempo.get("minuto")) + Integer.parseInt(tempo.get("segundo")));

        Log.d("SORTEIO","NumInt: " + numIntSorteado + " NumInt2: " + numIntSorteado2 + " SomaMinSeg: " + somaMinSegAtual);

        //define se sorteia lendário
        if(!sorteouLendario && numIntSorteado % 2 == 0 && numIntSorteado2 % 2 == 0 && somaMinSegAtual % 2 != 0){
            sorteouLendario = true;
            totalComuns = 5;

            //sorteia pokemons lendário
            for(int i = 0; i < 1; i++){
                max = tamLendario;
                int sorteio = RandomUtil.randomIntInRange(min,max);

                Aparecimento ap = new Aparecimento();
                ap.setLatitude(RandomUtil.randomDoubleInRange(LatMin, LatMax));
                ap.setLongitude(RandomUtil.randomDoubleInRange(LongMin, LongMax));
                ap.setPokemon(pokemons.get("L").get(sorteio));

                this.aparecimentos[contAparecimentos] = ap;
                Log.d("SORTEIO","LENDÁRIO: " + aparecimentos[contAparecimentos].getPokemon().getNome());

                contAparecimentos++;
            }
        }
        else{
            sorteouLendario = false;
            totalComuns = 6;
        }

        //sorteia pokemons comuns
        for(int i = 0; i < totalComuns; i++){
            max = tamComum;
            int sorteio = RandomUtil.randomIntInRange(min,max);

            Aparecimento ap = new Aparecimento();
            ap.setLatitude(RandomUtil.randomDoubleInRange(LatMin, LatMax));
            ap.setLongitude(RandomUtil.randomDoubleInRange(LongMin, LongMax));
            ap.setPokemon(pokemons.get("C").get(sorteio));

            this.aparecimentos[contAparecimentos] = ap;
            Log.d("SORTEIO","COMUM: " + aparecimentos[contAparecimentos].getPokemon().getNome());

            contAparecimentos++;
        }

        //sorteia pokemons incomuns
        for(int i = 0; i < 3; i++){
            max = tamIncomum;
            int sorteio = RandomUtil.randomIntInRange(min,max);

            Aparecimento ap = new Aparecimento();
            ap.setLatitude(RandomUtil.randomDoubleInRange(LatMin, LatMax));
            ap.setLongitude(RandomUtil.randomDoubleInRange(LongMin, LongMax));
            ap.setPokemon(pokemons.get("I").get(sorteio));

            this.aparecimentos[contAparecimentos] = ap;
            Log.d("SORTEIO","INCOMUM: " + aparecimentos[contAparecimentos].getPokemon().getNome());

            contAparecimentos++;
        }

        //sorteia pokemons raros
        for(int i = 0; i < 1; i++){
            max = tamRaro;
            int sorteio = RandomUtil.randomIntInRange(min,max);

            Aparecimento ap = new Aparecimento();
            ap.setLatitude(RandomUtil.randomDoubleInRange(LatMin, LatMax));
            ap.setLongitude(RandomUtil.randomDoubleInRange(LongMin, LongMax));
            ap.setPokemon(pokemons.get("R").get(sorteio));

            this.aparecimentos[contAparecimentos] = ap;
            Log.d("SORTEIO","RARO: " + aparecimentos[contAparecimentos].getPokemon().getNome());

            contAparecimentos++;
        }
    }

    public boolean loginUser(String login, String senha){

        Cursor c = BancoDadosSingleton.getInstance().buscar("usuario",
                                                                new String[]{"login","senha","temSessao"},
                                                                "login = '"+login+"' AND senha = '"+senha+"'",
                                                                "");

        //significa que o usuário que está logando é o mesmo que fez login anteriormente
        if(c.getCount() == 1){
            //abri a sessão do usuário
            ContentValues valores = new ContentValues();
            valores.put("temSessao","SIM");
            BancoDadosSingleton.getInstance().atualizar("usuario",valores,"login = '"+login+"'");

            //chama apenas se existir o usuário
            daoUsuario();

            c.close();
            return true;
        }else{

            //TODO: implementar regras de negócio de sincronização com o servidor web
            c.close();
            return false;
        }
    }

    public boolean logoutUser(){

        //fecha a sessão do usuário
        ContentValues valores = new ContentValues();
        valores.put("temSessao","NAO");

        BancoDadosSingleton.getInstance().atualizar("usuario",valores,"login = '"+this.user.getLogin()+"'");
        return true;
    }

    public boolean cadastrarUser(String login, String senha, String nome, String sexo, String foto){

        Map<String,String> timeStamp = TimeUtil.getHoraMinutoSegundoDiaMesAno();

        ContentValues valores = new ContentValues();
        valores.put("login",login);
        valores.put("senha",senha);
        valores.put("nome",nome);
        valores.put("sexo",sexo);
        valores.put("foto",foto);
        valores.put("dtCadastro",timeStamp.get("dia")+"/"+timeStamp.get("mes")+"/"+timeStamp.get("ano")+" "+timeStamp.get("hora")+":"+timeStamp.get("minuto")+":"+timeStamp.get("segundo"));
        valores.put("temSessao","SIM");

        //limpa tabelas locais de pokemons capturados e de usuário
        BancoDadosSingleton.getInstance().deletar("pokemonusuario", "");
        BancoDadosSingleton.getInstance().deletar("usuario","");

        BancoDadosSingleton.getInstance().inserir("usuario",valores);
        //TODO: enviar o cadastro para o servidor web também

        //chama apenas após cadastrar usuário
        daoUsuario();
        return true;
    }

    public void cadastraOvo(int idOvo, int idPokemon, String idTipoOvo, int incubado){

        ContentValues valores = new ContentValues();
        valores.put("idOvo",idOvo);
        valores.put("idPokemon",idPokemon);
        valores.put("idTipoOvo",idTipoOvo);
        valores.put("incubado",incubado);

        BancoDadosSingleton.getInstance().inserir("ovo",valores);

        //daoOvos()    CHAMA OU NAO?

    }

    public boolean temSessao(){

        Cursor sessao = BancoDadosSingleton.getInstance().buscar("usuario",new String[]{"login","temSessao"},"temSessao = 'SIM'","");

        if(sessao.getCount() == 1){
            //chama apenas se existir uma sessão de usuário
            daoUsuario();

            sessao.close();
            return true;
        }else{
            sessao.close();
            return false;
        }
    }

    public Pokemon convertPokemonSerializableToObject(Pokemon pkmn) {
        //obtem lista de pokemons da controladora geral
        List<Pokemon> listPkmn = this.getPokemons();
        Pokemon pkmnAux = null;

        //procura na lista de pokemons da controladora o pokemon parâmetro.
        for (Pokemon pokemon : listPkmn) {
            if (pokemon.getNumero() == pkmn.getNumero()) {
                pkmnAux = pokemon;
                break;
            }
        }

        return pkmnAux;
    }

    public boolean aumentaXp(String evento) {
        final int xpRecebido = getXpEvento(evento);
        final int nivelAtual = getUsuario().getNivel();
        final int xpAtual = getUsuario().getXp();
        final int xpMax = xpMaximo(nivelAtual);
        int xpFinal = xpAtual, nivelFinal = nivelAtual;

        if((xpAtual + xpRecebido) >= xpMax) {
            xpFinal = (xpAtual + xpRecebido) - xpMax;
            nivelFinal++;

            if(nivelFinal > 40) {
                nivelFinal = 40;
                xpFinal = xpMaximo(nivelFinal);
            }

            getUsuario().setNivel(nivelFinal);
        } else {
            xpFinal = xpAtual + xpRecebido;
        }

        getUsuario().setXp(xpFinal);

        ContentValues valores = new ContentValues();

        valores.put("login", getUsuario().getLogin());
        valores.put("senha", getUsuario().getSenha());
        valores.put("nome", getUsuario().getNome());
        valores.put("sexo", getUsuario().getSexo());
        valores.put("foto", getUsuario().getFoto());
        valores.put("dtCadastro", getUsuario().getDtCadastro());
        valores.put("temSessao", "SIM");
        valores.put("nivel", nivelFinal);
        valores.put("xp", xpFinal);

        int count = BancoDadosSingleton.getInstance().atualizar("usuario", valores, "login='"+getUsuario().getLogin()+"'");

        return count == 1;
    }

    public int xpMaximo(int nivelUsuario) {
        return nivelUsuario*1000;
    }

    public int getXpEvento(String evento) {
        switch(evento) {
            case "captura":
                return 20;
            case "evolui":
                return 200;
            case "pokestop":
                return 50;
            case "choca":
                return 100;
            default:
                return 0;
        }
    }
}
