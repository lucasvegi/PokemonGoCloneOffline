package teste.lucasvegi.pokemongooffline.Model;

import java.io.Serializable;
import java.util.Date;

public class InteracaoPokestop implements Serializable {
    private Pokestop p;
    private Usuario user;
    private Date ultimoAcesso;

    public InteracaoPokestop(Pokestop p, Usuario user, Date ultimoAcesso){
        this.p = p;
        this.user = user;
        this.ultimoAcesso = ultimoAcesso;
    }

    public Pokestop getPokestop() {
        return p;
    }

    public void setPokestop(Pokestop p) {
        this.p = p;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public Date getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(Date ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }
}
