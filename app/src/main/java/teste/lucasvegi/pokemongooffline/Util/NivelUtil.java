package teste.lucasvegi.pokemongooffline.Util;

import android.content.ContentValues;

import teste.lucasvegi.pokemongooffline.Model.ControladoraFachadaSingleton;
import teste.lucasvegi.pokemongooffline.Model.Usuario;

public class NivelUtil {

	public static void aumentaXp(String evento) {
		final Usuario usuario = ControladoraFachadaSingleton.getInstance().getUsuario();
		final int xpRecebido = getXpEvento(evento);
		final int nivelAtual = usuario.getNivel();
		final int xpAtual = usuario.getXp();
		final int xpMax = xpMaximo(nivelAtual);
		int xpFinal = xpAtual, nivelFinal = nivelAtual;

		if((xpAtual + xpRecebido) >= xpMax) {
			xpFinal = (xpAtual + xpRecebido) - xpMax;
			nivelFinal++;
			usuario.setNivel(nivelFinal);
		} else {
			xpFinal = xpAtual + xpRecebido;
		}

		usuario.setXp(xpFinal);

		ContentValues valores = new ContentValues();
		valores.put("login", usuario.getLogin());
		valores.put("senha", usuario.getSenha());
		valores.put("nome", usuario.getNome());
		valores.put("sexo", usuario.getSexo());
		valores.put("foto", usuario.getFoto());
		valores.put("dtCadastro", usuario.getDtCadastro());
		valores.put("temSessao", "");
		valores.put("nivel", nivelFinal);
		valores.put("xp", xpFinal);
		BancoDadosSingleton.getInstance().atualizar("usuario", valores, "login=" + usuario.getLogin());
	}

	public static int xpMaximo(int nivelUsuario) {
		return nivelUsuario*1000;
	}

	public static int getXpEvento(String evento) {
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
