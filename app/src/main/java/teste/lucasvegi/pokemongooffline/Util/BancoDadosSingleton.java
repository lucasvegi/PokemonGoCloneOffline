package teste.lucasvegi.pokemongooffline.Util;

/**
 * Created by Lucas on 12/12/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Field;

import teste.lucasvegi.pokemongooffline.R;

public final class BancoDadosSingleton {

    protected SQLiteDatabase db;
    private final String NOME_BANCO = "pokemon_go_bd";
    private static BancoDadosSingleton INSTANCE = new BancoDadosSingleton();

    private final String[] SCRIPT_DATABASE_CREATE = new String[] {
            "CREATE TABLE pokemon (" +
                    "  idPokemon INTEGER PRIMARY KEY," +
                    "  nome TEXT NOT NULL," +
                    "  categoria TEXT NOT NULL," +
                    "  foto INTEGER NOT NULL," +
                    "  icone INTEGER NOT NULL," +
                    "  idDoce INTEGER NOT NULL," +
                    "  idPokemonBase INTEGER ," +
                    " CONSTRAINT fk_pokemon_doce FOREIGN KEY (idDoce) REFERENCES doce (idDoce)," +    // relacionamento com Doce
                    " CONSTRAINT fk_pokemon_pokemon FOREIGN KEY (idPokemonBase) REFERENCES pokemon (idPokemon)" + //rel. evolução
                    ");",
            //agora os pokemons estao agrupados por evolucao
            "INSERT INTO pokemon (idPokemon, nome, categoria, foto, icone, idDoce, idPokemonBase) VALUES" +
                    "(1, 'Bulbasaur', 'I', "+ R.drawable.p1+", "+ R.drawable.i1+", 1, null)," +
                    "(2, 'Ivysaur', 'I', "+ R.drawable.p2+", "+ R.drawable.i2+", 1, 1)," +
                    "(3, 'Venusaur', 'R', "+ R.drawable.p3+", "+ R.drawable.i3+", 1, 2)," +
                    "(4, 'Charmander', 'I', "+ R.drawable.p4+", "+ R.drawable.i4+", 2, null)," +
                    "(5, 'Charmeleon', 'I', "+ R.drawable.p5+", "+ R.drawable.i5+", 2, 4)," +
                    "(6, 'Charizard', 'R', "+ R.drawable.p6+", "+ R.drawable.i6+", 2, 5)," +
                    "(7, 'Squirtle', 'I', "+ R.drawable.p7+", "+ R.drawable.i7+", 3, null)," +
                    "(8, 'Wartortle', 'I', "+ R.drawable.p8+", "+ R.drawable.i8+", 3, 7)," +
                    "(9, 'Blastoise', 'R', "+ R.drawable.p9+", "+ R.drawable.i9+", 3, 8)," +
                    "(10, 'Caterpie', 'C', "+ R.drawable.p10+", "+ R.drawable.i10+", 4, null)," +
                    "(11, 'Metapod', 'C', "+ R.drawable.p11+", "+ R.drawable.i11+", 4, 10)," +
                    "(12, 'Butterfree', 'I', "+ R.drawable.p12+", "+ R.drawable.i12+", 4, 11)," +
                    "(13, 'Weedle', 'C', "+ R.drawable.p13+", "+ R.drawable.i13+", 5, null)," +
                    "(14, 'Kakuna', 'C', "+ R.drawable.p14+", "+ R.drawable.i14+", 5, 13)," +
                    "(15, 'Beedrill', 'I', "+ R.drawable.p15+", "+ R.drawable.i15+", 5, 14)," +
                    "(16, 'Pidgey', 'C', "+ R.drawable.p16+", "+ R.drawable.i16+", 6, null)," +
                    "(17, 'Pidgeotto', 'I', "+ R.drawable.p17+", "+ R.drawable.i17+", 6, 16)," +
                    "(18, 'Pidgeot', 'R', "+ R.drawable.p18+", "+ R.drawable.i18+", 6, 17)," +
                    "(19, 'Rattata', 'C', "+ R.drawable.p19+", "+ R.drawable.i19+", 7, null)," +
                    "(20, 'Raticate', 'I', "+ R.drawable.p20+", "+ R.drawable.i20+", 7, 19)," +
                    "(21, 'Spearow', 'C', "+ R.drawable.p21+", "+ R.drawable.i21+", 8, null)," +
                    "(22, 'Fearow', 'I', "+ R.drawable.p22+", "+ R.drawable.i22+", 8, 21)," +
                    "(23, 'Ekans', 'C', "+ R.drawable.p23+", "+ R.drawable.i23+", 9, null)," +
                    "(24, 'Arbok', 'I', "+ R.drawable.p24+", "+ R.drawable.i24+", 9, 23)," +
                    "(25, 'Pikachu', 'C', "+ R.drawable.p25+", "+ R.drawable.i25+", 10, null)," +
                    "(26, 'Raichu', 'I', "+ R.drawable.p26+", "+ R.drawable.i26+", 10, 25)," +
                    "(27, 'Sandshrew', 'C', "+ R.drawable.p27+", "+ R.drawable.i27+", 11, null)," +
                    "(28, 'Sandslash', 'I', "+ R.drawable.p28+", "+ R.drawable.i28+", 11, 27)," +
                    "(29, 'Nidoran Femea', 'C', "+ R.drawable.p29+", "+ R.drawable.i29+", 12, null)," +
                    "(30, 'Nidorina', 'I', "+ R.drawable.p30+", "+ R.drawable.i30+", 12, 29)," +
                    "(31, 'Nidoqueen', 'R', "+ R.drawable.p31+", "+ R.drawable.i31+", 12, 30)," +
                    "(32, 'Nidoran Macho', 'C', "+ R.drawable.p32+", "+ R.drawable.i32+", 13, null)," +
                    "(33, 'Nidorino', 'I', "+ R.drawable.p33+", "+ R.drawable.i33+", 13, 32)," +
                    "(34, 'Nidoking', 'R', "+ R.drawable.p34+", "+ R.drawable.i34+", 13, 33)," +
                    "(35, 'Clefairy', 'I', "+ R.drawable.p35+", "+ R.drawable.i35+", 14, null)," +
                    "(36, 'Clefable', 'R', "+ R.drawable.p36+", "+ R.drawable.i36+", 14, 35)," +
                    "(37, 'Vulpix', 'C', "+ R.drawable.p37+", "+ R.drawable.i37+", 15, null)," +
                    "(38, 'Ninetales', 'R', "+ R.drawable.p38+", "+ R.drawable.i38+", 15, 37)," +
                    "(39, 'Jigglypuff', 'C', "+ R.drawable.p39+", "+ R.drawable.i39+", 16, null)," +
                    "(40, 'Wigglytuff', 'R', "+ R.drawable.p40+", "+ R.drawable.i40+", 16, 39)," +
                    "(41, 'Zubat', 'C', "+ R.drawable.p41+", "+ R.drawable.i41+", 17, null)," +
                    "(42, 'Golbat', 'I', "+ R.drawable.p42+", "+ R.drawable.i42+", 17, 41)," +
                    "(43, 'Oddish', 'C', "+ R.drawable.p43+", "+ R.drawable.i43+", 18, null)," +
                    "(44, 'Gloom', 'C', "+ R.drawable.p44+", "+ R.drawable.i44+", 18,43)," +
                    "(45, 'Vileplume', 'I', "+ R.drawable.p45+", "+ R.drawable.i45+", 18,44)," +
                    "(46, 'Paras', 'C', "+ R.drawable.p46+", "+ R.drawable.i46+", 19, null)," +
                    "(47, 'Parasect', 'I', "+ R.drawable.p47+", "+ R.drawable.i47+", 19, 46)," +
                    "(48, 'Venonat', 'C', "+ R.drawable.p48+", "+ R.drawable.i48+", 20, null)," +
                    "(49, 'Venomoth', 'I', "+ R.drawable.p49+", "+ R.drawable.i49+", 20, 48)," +
                    "(50, 'Diglett', 'C', "+ R.drawable.p50+", "+ R.drawable.i50+", 21, null)," +
                    "(51, 'Dugtrio', 'I', "+ R.drawable.p51+", "+ R.drawable.i51+", 21, 50)," +
                    "(52, 'Meowth', 'C', "+ R.drawable.p52+", "+ R.drawable.i52+", 22, null)," +
                    "(53, 'Persian', 'I', "+ R.drawable.p53+", "+ R.drawable.i53+", 22, 52)," +
                    "(54, 'Psyduck', 'C', "+ R.drawable.p54+", "+ R.drawable.i54+", 23, null)," +
                    "(55, 'Golduck', 'I', "+ R.drawable.p55+", "+ R.drawable.i55+", 23, 54)," +
                    "(56, 'Mankey', 'C', "+ R.drawable.p56+", "+ R.drawable.i56+", 24, null)," +
                    "(57, 'Primeape', 'I', "+ R.drawable.p57+", "+ R.drawable.i57+", 24, 56)," +
                    "(58, 'Growlithe', 'C', "+ R.drawable.p58+", "+ R.drawable.i58+", 25, null)," +
                    "(59, 'Arcanine', 'R', "+ R.drawable.p59+", "+ R.drawable.i59+", 25, 58)," +
                    "(60, 'Poliwag', 'C', "+ R.drawable.p60+", "+ R.drawable.i60+", 26, null)," +
                    "(61, 'Poliwhril', 'C', "+ R.drawable.p61+", "+ R.drawable.i61+", 26, 60)," +
                    "(62, 'Poliwrath', 'R', "+ R.drawable.p62+", "+ R.drawable.i62+", 26, 61)," +
                    "(63, 'Abra', 'C', "+ R.drawable.p63+", "+ R.drawable.i63+", 27, null)," +
                    "(64, 'Kadabra', 'I', "+ R.drawable.p64+", "+ R.drawable.i64+", 27, 63)," +
                    "(65, 'Alakazam', 'R', "+ R.drawable.p65+", "+ R.drawable.i65+", 27, 64)," +
                    "(66, 'Machop', 'C', "+ R.drawable.p66+", "+ R.drawable.i66+", 28, null)," +
                    "(67, 'Machoke', 'I', "+ R.drawable.p67+", "+ R.drawable.i67+", 28, 66)," +
                    "(68, 'Machamp', 'R', "+ R.drawable.p68+", "+ R.drawable.i68+", 28, 67)," +
                    "(69, 'Bellsprout', 'C', "+ R.drawable.p69+", "+ R.drawable.i69+", 29, null)," +
                    "(70, 'Weepinbell', 'I', "+ R.drawable.p70+", "+ R.drawable.i70+", 29, 69)," +
                    "(71, 'Victreebel', 'R', "+ R.drawable.p71+", "+ R.drawable.i71+", 29, 70)," +
                    "(72, 'Tentacool', 'C', "+ R.drawable.p72+", "+ R.drawable.i72+", 30, null)," +
                    "(73, 'Tentacruel', 'I', "+ R.drawable.p73+", "+ R.drawable.i73+", 30, 72)," +
                    "(74, 'Geodude', 'C', "+ R.drawable.p74+", "+ R.drawable.i74+", 31, null)," +
                    "(75, 'Graveler', 'I', "+ R.drawable.p75+", "+ R.drawable.i75+", 31, 74)," +
                    "(76, 'Golem', 'R', "+ R.drawable.p76+", "+ R.drawable.i76+", 31, 75)," +
                    "(77, 'Ponyta', 'C', "+ R.drawable.p77+", "+ R.drawable.i77+", 32, null)," +
                    "(78, 'Rapidash', 'I', "+ R.drawable.p78+", "+ R.drawable.i78+", 32, 77)," +
                    "(79, 'Slowpoke', 'C', "+ R.drawable.p79+", "+ R.drawable.i79+", 33, null)," +
                    "(80, 'Slowbro', 'I', "+ R.drawable.p80+", "+ R.drawable.i80+", 33, 79)," +
                    "(81, 'Magnemite', 'C', "+ R.drawable.p81+", "+ R.drawable.i81+", 34, null)," +
                    "(82, 'Magneton', 'I', "+ R.drawable.p82+", "+ R.drawable.i82+", 34, 81)," +
                    "(83, 'Farfetch''d', 'C', "+ R.drawable.p83+", "+ R.drawable.i83+", 35, null)," +
                    "(84, 'Doduo', 'C', "+ R.drawable.p84+", "+ R.drawable.i84+", 36, null)," +
                    "(85, 'Dodrio', 'I', "+ R.drawable.p85+", "+ R.drawable.i85+", 36, 84)," +
                    "(86, 'Seel', 'C', "+ R.drawable.p86+", "+ R.drawable.i86+", 37, null)," +
                    "(87, 'Dewgong', 'R', "+ R.drawable.p87+", "+ R.drawable.i87+", 37, 86)," +
                    "(88, 'Grimer', 'C', "+ R.drawable.p88+", "+ R.drawable.i88+", 38, null)," +
                    "(89, 'Muk', 'R', "+ R.drawable.p89+", "+ R.drawable.i89+", 38, 88)," +
                    "(90, 'Shellder', 'C', "+ R.drawable.p90+", "+ R.drawable.i90+", 39, null)," +
                    "(91, 'Cloyster', 'R', "+ R.drawable.p91+", "+ R.drawable.i91+", 39, 90)," +
                    "(92, 'Gastly', 'C', "+ R.drawable.p92+", "+ R.drawable.i92+", 40, null)," +
                    "(93, 'Haunter', 'I', "+ R.drawable.p93+", "+ R.drawable.i93+", 40, 92)," +
                    "(94, 'Gengar', 'R', "+ R.drawable.p94+", "+ R.drawable.i94+", 40, 93)," +
                    "(95, 'Onix', 'C', "+ R.drawable.p95+", "+ R.drawable.i95+", 41, null)," +
                    "(96, 'Drowzee', 'C', "+ R.drawable.p96+", "+ R.drawable.i96+", 42, null)," +
                    "(97, 'Hypno', 'I', "+ R.drawable.p97+", "+ R.drawable.i97+", 42, 96)," +
                    "(98, 'Krabby', 'C', "+ R.drawable.p98+", "+ R.drawable.i98+", 43, null)," +
                    "(99, 'Kingler', 'I', "+ R.drawable.p99+", "+ R.drawable.i99+", 43, 98)," +
                    "(100, 'Voltorb', 'C', "+ R.drawable.p100+", "+ R.drawable.i100+", 44, null)," +
                    "(101, 'Electrode', 'I', "+ R.drawable.p101+", "+ R.drawable.i101+", 44, 100)," +
                    "(102, 'Exeggcute', 'I', "+ R.drawable.p102+", "+ R.drawable.i102+", 45, null)," +
                    "(103, 'Exeggutor', 'R', "+ R.drawable.p103+", "+ R.drawable.i103+", 45, 102)," +
                    "(104, 'Cubone', 'C', "+ R.drawable.p104+", "+ R.drawable.i104+", 46, null)," +
                    "(105, 'Marowak', 'I', "+ R.drawable.p105+", "+ R.drawable.i105+", 46, 104)," +
                    "(106, 'Hitmonlee', 'I', "+ R.drawable.p106+", "+ R.drawable.i106+", 47, null)," +
                    "(107, 'Hitmonchan', 'I', "+ R.drawable.p107+", "+ R.drawable.i107+", 47, 106)," +
                    "(108, 'Lickitung', 'I', "+ R.drawable.p108+", "+ R.drawable.i108+", 48, null)," +
                    "(109, 'Koffing', 'C', "+ R.drawable.p109+", "+ R.drawable.i109+", 49, null)," +
                    "(110, 'Weezing', 'I', "+ R.drawable.p110+", "+ R.drawable.i110+", 49, 109)," +
                    "(111, 'Rhyhorn', 'C', "+ R.drawable.p111+", "+ R.drawable.i111+", 50, null)," +
                    "(112, 'Rhydon', 'I', "+ R.drawable.p112+", "+ R.drawable.i112+", 50, 111)," +
                    "(113, 'Chansey', 'I', "+ R.drawable.p113+", "+ R.drawable.i113+", 51, null)," +
                    "(114, 'Tangela', 'I', "+ R.drawable.p114+", "+ R.drawable.i114+", 52, null)," +
                    "(115, 'Kangaskhan', 'I', "+ R.drawable.p115+", "+ R.drawable.i115+", 53, null)," +
                    "(116, 'Horsea', 'C', "+ R.drawable.p116+", "+ R.drawable.i116+", 54, null)," +
                    "(117, 'Seadra', 'I', "+ R.drawable.p117+", "+ R.drawable.i117+", 54, 116)," +
                    "(118, 'Goldeen', 'C', "+ R.drawable.p118+", "+ R.drawable.i118+", 55, null)," +
                    "(119, 'Seaking', 'I', "+ R.drawable.p119+", "+ R.drawable.i119+", 55, 118)," +
                    "(120, 'Staryu', 'C', "+ R.drawable.p120+", "+ R.drawable.i120+", 56, null)," +
                    "(121, 'Starmie', 'I', "+ R.drawable.p121+", "+ R.drawable.i121+", 56, 120)," +
                    "(122, 'Mr. Mime', 'I', "+ R.drawable.p122+", "+ R.drawable.i122+", 57, null)," +
                    "(123, 'Scyther', 'I', "+ R.drawable.p123+", "+ R.drawable.i123+", 58, null)," +
                    "(124, 'Jynx', 'I', "+ R.drawable.p124+", "+ R.drawable.i124+", 59, null)," +
                    "(125, 'Electabuzz', 'I', "+ R.drawable.p125+", "+ R.drawable.i125+", 60, null)," +
                    "(126, 'Magmar', 'I', "+ R.drawable.p126+", "+ R.drawable.i126+", 61, null)," +
                    "(127, 'Pinsir', 'I', "+ R.drawable.p127+", "+ R.drawable.i127+", 62, null)," +
                    "(128, 'Tauros', 'I', "+ R.drawable.p128+", "+ R.drawable.i128+", 63, null)," +
                    "(129, 'Magikarp', 'C', "+ R.drawable.p129+", "+ R.drawable.i129+", 64, null)," +
                    "(130, 'Gyarados', 'I', "+ R.drawable.p130+", "+ R.drawable.i130+", 64, 129)," +
                    "(131, 'Lapras', 'I', "+ R.drawable.p131+", "+ R.drawable.i131+", 65, null)," +
                    "(132, 'Ditto', 'I', "+ R.drawable.p132+", "+ R.drawable.i132+", 66, null)," +
                    "(133, 'Eevee', 'I', "+ R.drawable.p133+", "+ R.drawable.i133+", 67, null)," +
                    "(134, 'Vaporeon', 'R', "+ R.drawable.p134+", "+ R.drawable.i134+", 67, 133)," +
                    "(135, 'Jolteon', 'R', "+ R.drawable.p135+", "+ R.drawable.i135+", 67, 133)," +
                    "(136, 'Flareon', 'R', "+ R.drawable.p136+", "+ R.drawable.i136+", 67, 133)," +
                    "(137, 'Porygon', 'R', "+ R.drawable.p137+", "+ R.drawable.i137+", 68, null)," +
                    "(138, 'Omanyte', 'R', "+ R.drawable.p138+", "+ R.drawable.i138+", 69, null)," +
                    "(139, 'Omastar', 'R', "+ R.drawable.p139+", "+ R.drawable.i139+", 69, 138)," +
                    "(140, 'Kabuto', 'R', "+ R.drawable.p140+", "+ R.drawable.i140+", 70, null)," +
                    "(141, 'Kabutops', 'R', "+ R.drawable.p141+", "+ R.drawable.i141+", 70, 140)," +
                    "(142, 'Aerodactyl', 'R', "+ R.drawable.p142+", "+ R.drawable.i142+", 71, null)," +
                    "(143, 'Snorlax', 'I', "+ R.drawable.p143+", "+ R.drawable.i143+", 72, null)," +
                    "(144, 'Articuno', 'L', "+ R.drawable.p144+", "+ R.drawable.i144+", 73, null)," +
                    "(145, 'Zapdos', 'L', "+ R.drawable.p145+", "+ R.drawable.i145+", 74, null)," +
                    "(146, 'Moltres', 'L', "+ R.drawable.p146+", "+ R.drawable.i146+", 75, null)," +
                    "(147, 'Dratini', 'I', "+ R.drawable.p147+", "+ R.drawable.i147+", 76, null)," +
                    "(148, 'Dragonair', 'I', "+ R.drawable.p148+", "+ R.drawable.i148+", 76, 147)," +
                    "(149, 'Dragonite', 'R', "+ R.drawable.p149+", "+ R.drawable.i149+", 76, 148)," +
                    "(150, 'Mewtwo', 'L', "+ R.drawable.p150+", "+ R.drawable.i150+", 77, null)," +
                    "(151, 'Mew', 'L', "+ R.drawable.p151+", "+ R.drawable.i151+", 78, null);",

            "CREATE TABLE doce (" +
                    "  idDoce INTEGER PRIMARY KEY," +
                    "  nome TEXT NOT NULL," +
                    "  quant INTEGER NOT NULL" +
                    ");",

            "INSERT INTO doce (idDoce, nome, quant) VALUES" +
                    "(1, 'Bulbasaur', 0)," +
                    "(2, 'Charmander', 0)," +
                    "(3, 'Squirtle', 0)," +
                    "(4, 'Caterpie', 0)," +
                    "(5, 'Weedle', 0)," +
                    "(6, 'Pidgey', 0)," +
                    "(7, 'Rattata', 0)," +
                    "(8, 'Spearow', 0)," +
                    "(9, 'Ekans', 0)," +
                    "(10, 'Pikachu', 0)," +
                    "(11, 'Sandshrew', 0)," +
                    "(12, 'Nidoran Femea', 0)," +
                    "(13, 'Nidoran Macho', 0)," +
                    "(14, 'Clefairy', 0)," +
                    "(15, 'Vulpix', 0)," +
                    "(16, 'Jigglypuff', 0)," +
                    "(17, 'Zubat', 0)," +
                    "(18, 'Oddish', 0)," +
                    "(19, 'Paras', 0)," +
                    "(20, 'Venonat', 0)," +
                    "(21, 'Diglett', 0)," +
                    "(22, 'Meowth', 0)," +
                    "(23, 'Psyduck', 0)," +
                    "(24, 'Mankey', 0)," +
                    "(25, 'Growlithe', 0)," +
                    "(26, 'Poliwag', 0)," +
                    "(27, 'Abra', 0)," +
                    "(28, 'Machop', 0)," +
                    "(29, 'Bellsprout', 0)," +
                    "(30, 'Tentacool', 0)," +
                    "(31, 'Geodude', 0)," +
                    "(32, 'Ponyta', 0)," +
                    "(33, 'Slowpoke', 0)," +
                    "(34, 'Magnemite', 0)," +
                    "(35, 'Farfetch''d', 0)," +
                    "(36, 'Doduo', 0)," +
                    "(37, 'Seel', 0)," +
                    "(38, 'Grimer', 0)," +
                    "(39, 'Shellder', 0)," +
                    "(40, 'Gastly', 0)," +
                    "(41, 'Onix', 0)," +
                    "(42, 'Drowzee', 0)," +
                    "(43, 'Krabby', 0)," +
                    "(44, 'Voltorb', 0)," +
                    "(45, 'Exeggcute', 0)," +
                    "(46, 'Cubone', 0)," +
                    "(47, 'Tyrogue', 0)," +
                    "(48, 'Lickitung', 0)," +
                    "(49, 'Koffing', 0)," +
                    "(50, 'Rhyhorn', 0)," +
                    "(51, 'Chansey', 0)," +
                    "(52, 'Tangela', 0)," +
                    "(53, 'Kangaskhan', 0)," +
                    "(54, 'Horsea', 0)," +
                    "(55, 'Goldeen', 0)," +
                    "(56, 'Staryu', 0)," +
                    "(57, 'Mr. Mime', 0)," +
                    "(58, 'Scyther', 0)," +
                    "(59, 'Jynx', 0)," +
                    "(60, 'Electabuzz', 0)," +
                    "(61, 'Magmar', 0)," +
                    "(62, 'Pinsir', 0)," +
                    "(63, 'Tauros', 0)," +
                    "(64, 'Magikarp', 0)," +
                    "(65, 'Lapras', 0)," +
                    "(66, 'Ditto', 0)," +
                    "(67, 'Eevee', 0)," +
                    "(68, 'Porygon', 0)," +
                    "(69, 'Omanyte', 0)," +
                    "(70, 'Kabuto', 0)," +
                    "(71, 'Aerodactyl', 0)," +
                    "(72, 'Snorlax', 0)," +
                    "(73, 'Articuno', 0)," +
                    "(74, 'Zapdos', 0)," +
                    "(75, 'Moltres', 0)," +
                    "(76, 'Dratini', 0)," +
                    "(77, 'Mewtwo', 0)," +
                    "(78, 'Mew', 0);",

            "CREATE TABLE tipo (" +
                    "  idTipo INTEGER PRIMARY KEY," +
                    "  nome TEXT NOT NULL" +
                    ");",
            "INSERT INTO tipo (idTipo, nome) VALUES" +
                    "(1, 'Normal')," +
                    "(2, 'Fire')," +
                    "(3, 'Fighting')," +
                    "(4, 'Water')," +
                    "(5, 'Flying')," +
                    "(6, 'Grass')," +
                    "(7, 'Poison')," +
                    "(8, 'Electric')," +
                    "(9, 'Ground')," +
                    "(10, 'Psychic')," +
                    "(11, 'Rock')," +
                    "(12, 'Ice')," +
                    "(13, 'Bug')," +
                    "(14, 'Dragon')," +
                    "(15, 'Ghost')," +
                    "(16, 'Dark')," +
                    "(17, 'Steel')," +
                    "(18, 'Fairy');",
            "CREATE TABLE pokemontipo (" +
                    "  idPokemon INTEGER NOT NULL," +
                    "  idTipo INTEGER NOT NULL," +
                    "  PRIMARY KEY  (idPokemon,idTipo)," +
                    "  CONSTRAINT fk_pokemontipo_pokemon FOREIGN KEY (idPokemon) REFERENCES pokemon (idPokemon)," +
                    "  CONSTRAINT fk_pokemontipo_tipo FOREIGN KEY (idTipo) REFERENCES tipo (idTipo)" +
                    ");",
            "INSERT INTO pokemontipo (idPokemon, idTipo) VALUES" +
                    "(16, 1)," +
                    "(17, 1)," +
                    "(18, 1)," +
                    "(19, 1)," +
                    "(20, 1)," +
                    "(21, 1)," +
                    "(22, 1)," +
                    "(39, 1)," +
                    "(40, 1)," +
                    "(52, 1)," +
                    "(53, 1)," +
                    "(83, 1)," +
                    "(84, 1)," +
                    "(85, 1)," +
                    "(108, 1)," +
                    "(113, 1)," +
                    "(115, 1)," +
                    "(128, 1)," +
                    "(132, 1)," +
                    "(133, 1)," +
                    "(137, 1)," +
                    "(143, 1)," +
                    "(4, 2)," +
                    "(5, 2)," +
                    "(6, 2)," +
                    "(37, 2)," +
                    "(38, 2)," +
                    "(58, 2)," +
                    "(59, 2)," +
                    "(77, 2)," +
                    "(78, 2)," +
                    "(126, 2)," +
                    "(136, 2)," +
                    "(146, 2)," +
                    "(56, 3)," +
                    "(57, 3)," +
                    "(62, 3)," +
                    "(66, 3)," +
                    "(67, 3)," +
                    "(68, 3)," +
                    "(106, 3)," +
                    "(107, 3)," +
                    "(7, 4)," +
                    "(8, 4)," +
                    "(9, 4)," +
                    "(54, 4)," +
                    "(55, 4)," +
                    "(60, 4)," +
                    "(61, 4)," +
                    "(62, 4)," +
                    "(72, 4)," +
                    "(73, 4)," +
                    "(79, 4)," +
                    "(80, 4)," +
                    "(86, 4)," +
                    "(87, 4)," +
                    "(90, 4)," +
                    "(91, 4)," +
                    "(98, 4)," +
                    "(99, 4)," +
                    "(116, 4)," +
                    "(117, 4)," +
                    "(118, 4)," +
                    "(119, 4)," +
                    "(120, 4)," +
                    "(121, 4)," +
                    "(129, 4)," +
                    "(130, 4)," +
                    "(131, 4)," +
                    "(134, 4)," +
                    "(138, 4)," +
                    "(139, 4)," +
                    "(140, 4)," +
                    "(141, 4)," +
                    "(6, 5)," +
                    "(12, 5)," +
                    "(16, 5)," +
                    "(17, 5)," +
                    "(18, 5)," +
                    "(21, 5)," +
                    "(22, 5)," +
                    "(41, 5)," +
                    "(42, 5)," +
                    "(83, 5)," +
                    "(84, 5)," +
                    "(85, 5)," +
                    "(123, 5)," +
                    "(130, 5)," +
                    "(142, 5)," +
                    "(144, 5)," +
                    "(145, 5)," +
                    "(146, 5)," +
                    "(149, 5)," +
                    "(1, 6)," +
                    "(2, 6)," +
                    "(3, 6)," +
                    "(43, 6)," +
                    "(44, 6)," +
                    "(45, 6)," +
                    "(46, 6)," +
                    "(47, 6)," +
                    "(69, 6)," +
                    "(70, 6)," +
                    "(71, 6)," +
                    "(102, 6)," +
                    "(103, 6)," +
                    "(114, 6)," +
                    "(1, 7)," +
                    "(2, 7)," +
                    "(3, 7)," +
                    "(13, 7)," +
                    "(14, 7)," +
                    "(15, 7)," +
                    "(23, 7)," +
                    "(24, 7)," +
                    "(29, 7)," +
                    "(30, 7)," +
                    "(31, 7)," +
                    "(32, 7)," +
                    "(33, 7)," +
                    "(34, 7)," +
                    "(41, 7)," +
                    "(42, 7)," +
                    "(43, 7)," +
                    "(44, 7)," +
                    "(45, 7)," +
                    "(48, 7)," +
                    "(49, 7)," +
                    "(69, 7)," +
                    "(70, 7)," +
                    "(71, 7)," +
                    "(72, 7)," +
                    "(73, 7)," +
                    "(88, 7)," +
                    "(89, 7)," +
                    "(92, 7)," +
                    "(93, 7)," +
                    "(94, 7)," +
                    "(109, 7)," +
                    "(110, 7)," +
                    "(25, 8)," +
                    "(26, 8)," +
                    "(81, 8)," +
                    "(82, 8)," +
                    "(100, 8)," +
                    "(101, 8)," +
                    "(125, 8)," +
                    "(135, 8)," +
                    "(145, 8)," +
                    "(27, 9)," +
                    "(28, 9)," +
                    "(31, 9)," +
                    "(34, 9)," +
                    "(50, 9)," +
                    "(51, 9)," +
                    "(74, 9)," +
                    "(75, 9)," +
                    "(76, 9)," +
                    "(95, 9)," +
                    "(104, 9)," +
                    "(105, 9)," +
                    "(111, 9)," +
                    "(112, 9)," +
                    "(63, 10)," +
                    "(64, 10)," +
                    "(65, 10)," +
                    "(79, 10)," +
                    "(80, 10)," +
                    "(96, 10)," +
                    "(97, 10)," +
                    "(102, 10)," +
                    "(103, 10)," +
                    "(121, 10)," +
                    "(122, 10)," +
                    "(124, 10)," +
                    "(150, 10)," +
                    "(151, 10)," +
                    "(74, 11)," +
                    "(75, 11)," +
                    "(76, 11)," +
                    "(95, 11)," +
                    "(111, 11)," +
                    "(112, 11)," +
                    "(138, 11)," +
                    "(139, 11)," +
                    "(140, 11)," +
                    "(141, 11)," +
                    "(142, 11)," +
                    "(87, 12)," +
                    "(91, 12)," +
                    "(124, 12)," +
                    "(131, 12)," +
                    "(144, 12)," +
                    "(10, 13)," +
                    "(11, 13)," +
                    "(12, 13)," +
                    "(13, 13)," +
                    "(14, 13)," +
                    "(15, 13)," +
                    "(46, 13)," +
                    "(47, 13)," +
                    "(48, 13)," +
                    "(49, 13)," +
                    "(123, 13)," +
                    "(127, 13)," +
                    "(147, 14)," +
                    "(148, 14)," +
                    "(149, 14)," +
                    "(92, 15)," +
                    "(93, 15)," +
                    "(94, 15)," +
                    "(81, 17)," +
                    "(82, 17)," +
                    "(35, 18)," +
                    "(36, 18)," +
                    "(39, 18)," +
                    "(40, 18)," +
                    "(122, 18);",
            "CREATE TABLE usuario (" +
                    "  login TEXT PRIMARY KEY," +
                    "  senha TEXT NOT NULL," +
                    "  nome TEXT NOT NULL," +
                    "  sexo TEXT NOT NULL," +
                    "  foto TEXT," +
                    "  dtCadastro TEXT NOT NULL," +
                    "  temSessao TEXT NOT NULL," +
                    "  nivel INTEGER DEFAULT 1 NOT NULL," +
                    "  xp INTEGER DEFAULT 0 NOT NULL" +
                    ");",
            "CREATE TABLE pokemonusuario (" +
                    "  login TEXT NOT NULL," +
                    "  idPokemon INTEGER NOT NULL," +
                    "  latitude REAL NOT NULL," +
                    "  longitude REAL NOT NULL," +
                    "  dtCaptura TEXT NOT NULL," +
                    "  evoluido INTEGER NOT NULL," +
                    "  PRIMARY KEY  (login,idPokemon,dtCaptura)," +
                    "  CONSTRAINT fk_usuariopokemon_login FOREIGN KEY (login) REFERENCES usuario (login)," +
                    "  CONSTRAINT fk_usuariopokemon_pokemon FOREIGN KEY (idPokemon) REFERENCES pokemon (idPokemon)" +
                    ");",
            "CREATE TABLE tipoovo (" +
                    "  idTipoOvo TEXT PRIMARY KEY," +
                    "  foto INTEGER NOT NULL," +
                    "  fotoIncubadora INTEGER NOT NULL," +
                    "  quilometragem DOUBLE NOT NULL," +
                    "  cor TEXT NOT NULL" +
                    ");",
            "INSERT INTO tipoovo (idTipoOvo, foto, fotoIncubadora,quilometragem,cor) VALUES" +
                    "('C', "+R.drawable.ovo_verde+", "+R.drawable.incubadora_verde+",2,'Verde')," +
                    "('I', "+R.drawable.ovo_laranja+", "+R.drawable.incubadora_laranja+",5,'Laranja')," +
                    "('R', "+R.drawable.ovo_azul+", "+R.drawable.incubadora_azul+",7,'Azul')," +
                    "('L', "+R.drawable.ovo_vermelho+", "+R.drawable.incubadora_vermelha+",10,'Vermelho');",
            "CREATE TABLE ovo (" +
                    "  idOvo INTEGER NOT NULL," +
                    "  idPokemon INTEGER NOT NULL," +
                    "  idTipoOvo TEXT NOT NULL," +
                    "  incubado INTEGER NOT NULL," +
                    "  chocado INTEGER NOT NULL," +
                    "  exibido INTEGER NOT NULL," +
                    "  KmAndado DOUBLE NOT NULL," +
                    "  PRIMARY KEY  (idOvo,idPokemon,idTipoOvo)," +
                    "  CONSTRAINT fk_usuariopokemon_pokemon FOREIGN KEY (idPokemon) REFERENCES pokemon (idPokemon)," +
                    "  CONSTRAINT fk_tipoovo FOREIGN KEY (idTipoOvo) REFERENCES tipoovo (idTipoOvo)" +
                    ");",
            "INSERT INTO ovo (idOvo, idPokemon, idTipoOvo, incubado, chocado, exibido,KmAndado) VALUES" +
                    "(1, 23, 'C', 0, 0, 0, 0)," +
                    "(2, 18, 'R', 0, 0, 0, 0)," +
                    "(3, 50, 'C', 0, 0, 0, 0)," +
                    "(4, 19, 'L', 0, 0, 0, 0)," +
                    "(5, 16, 'C', 0, 0, 0, 0)," +
                    "(6, 22, 'I', 0, 0, 0, 0);",
            "CREATE TABLE pokestop (" +
                    "  idPokestop TEXT NOT NULL," +
                    "  latitude REAL NOT NULL," +
                    "  longitude REAL NOT NULL," +
                    "  disponivel BOOLEAN NOT NULL," +
                    "  PRIMARY KEY  (idPokestop)" +
                    ");",
            "CREATE TABLE interacaopokestop ("+
                    " idPokestop TEXT NOT NULL,"+
                    " loginUsuario TEXT NOT NULL,"+
                    " ultimoAcesso TEXT NOT NULL,"+
                    " PRIMARY KEY(idPokestop, loginUsuario),"+
                    " CONSTRAINT fk_interacaopokestop_pokestop FOREIGN KEY (idPokestop) REFERENCES pokestop (idPokestop),"+
                    " CONSTRAINT fk_interacaopokestop_usuario FOREIGN KEY (loginUsuario) REFERENCES usuario (login)"+
                    ");",
            "CREATE TABLE traducao (" +
                    "  ingles TEXT NOT NULL," +
                    "  portugues TEXT NOT NULL," +
                    "  PRIMARY KEY  (ingles)" +
                    ");",
            "INSERT INTO traducao (ingles, portugues) VALUES" +
                    "('accounting','Agência de Contabilidade'),"+
                    "('airport','aeroporto'),"+
                    "('amusement_park','Parque de Diversões'),"+
                    "('aquarium','Aquário'),"+
                    "('art_gallery','Galeria de Arte'),"+
                    "('atm','Caixa 24h'),"+
                    "('bakery','Padaria'),"+
                    "('bank','Banco'),"+
                    "('bar','Bar'),"+
                    "('beauty_salon','Salão de Beleza'),"+
                    "('bicycle_store','Loja de Bicicleta'),"+
                    "('book_store','Livraria'),"+
                    "('bowling_alley','Boliche'),"+
                    "('bus_station','Ponto de Ônibus'),"+
                    "('cafe','Café'),"+
                    "('campground','Área de Acampamento'),"+
                    "('car_dealer','Concecionária de Carros'),"+
                    "('car_rental','Aluguel de Carros'),"+
                    "('car_repair','Mecânico'),"+
                    "('car_wash','Lava Jato'),"+
                    "('casino','Cassino'),"+
                    "('cemetery','Cemitério'),"+
                    "('church','Igreja'),"+
                    "('city_hall','Câmara Municipal'),"+
                    "('clothing_store','Loja de Roupas'),"+
                    "('convenience_store','Loja de Conveniência'),"+
                    "('courthouse','Tribunal'),"+
                    "('dentist','Dentista'),"+
                    "('department_store','Loja de Departamento'),"+
                    "('doctor','Doutor'),"+
                    "('drugstore','Farmácia'),"+
                    "('electrician','Eletricista'),"+
                    "('embassy','Embaixada'),"+
                    "('fire_station','Corpo de Bombeiros'),"+
                    "('florist','Floricultura'),"+
                    "('funeral_home','Funerária'),"+
                    "('furniture_store','Loja de Móveis'),"+
                    "('gas_station','Posto de Gasolina'),"+
                    "('gym','Academia'),"+
                    "('hair_care','Cabeleleiro'),"+
                    "('hardware_store','Loja de Material de Construção'),"+
                    "('hindu_temple','Templo Hindu'),"+
                    "('home_goods_store','Loja de Artigos Domésticos'),"+
                    "('hospital','Hospital'),"+
                    "('insurance_agency','Agência de Seguros'),"+
                    "('jewelry_store','Joaleria'),"+
                    "('laundry','Lavanderia'),"+
                    "('lawyer','Advocacia'),"+
                    "('library','Biblioteca'),"+
                    "('light_rail_station','Estação de Metro Leve'),"+
                    "('liquor_store','Distribuidora de Bebidas'),"+
                    "('local_government_office','Escritório do Governo Local'),"+
                    "('locksmith','chaveiro'),"+
                    "('lodging','Pousada'),"+
                    "('meal_delivery','Entrega de Refeições'),"+
                    "('meal_takeaway','Refeições para Viagem'),"+
                    "('mosque','Mosqueiro'),"+
                    "('movie_rental','Locadora de Filmes'),"+
                    "('movie_theater','Cinema'),"+
                    "('moving_company','Fretista'),"+
                    "('museum','Museu'),"+
                    "('night_club','Balada'),"+
                    "('painter','Pintor'),"+
                    "('park','Parque'),"+
                    "('parking','Estacionamento'),"+
                    "('pet_store','Petshop'),"+
                    "('pharmacy','Farmácia'),"+
                    "('physiotherapist','Fisioterapeuta'),"+
                    "('plumber','Encanador'),"+
                    "('police','Estação de Polícia'),"+
                    "('post_office','Agência dos Correios'),"+
                    "('primary_school','Escola'),"+
                    "('real_estate_agency','Imobiliária'),"+
                    "('restaurant','Restaurante'),"+
                    "('roofing_contractor','Empreiteiro de Telhados'),"+
                    "('rv_park','Parque de Trailers'),"+
                    "('school','Escola'),"+
                    "('secondary_school','Colégio'),"+
                    "('shoe_store','Loja de Sapatos'),"+
                    "('shopping_mall','Shopping'),"+
                    "('spa','Spa'),"+
                    "('stadium','Estadio'),"+
                    "('storage','Depósito'),"+
                    "('store','Loja'),"+
                    "('subway_station','Estação de Metro'),"+
                    "('supermarket','Supermercado'),"+
                    "('synagogue','Sinagoga'),"+
                    "('taxi_stand','Ponto de Táxi'),"+
                    "('tourist_attraction','Atração Turística'),"+
                    "('train_station','Estação de Trem'),"+
                    "('transit_station','Estação de Trânsito'),"+
                    "('travel_agency','Agência de Viagens'),"+
                    "('university','Universidade'),"+
                    "('veterinary_care','Veterinária'),"+
                    "('zoo','Zoológico');"
            };

    private BancoDadosSingleton() {
        Context ctx = MyApp.getAppContext();
        // Abre o banco de dados já existente ou então cria um banco novo
        db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);

        //busca por tabelas existentes no banco = "show tables" do MySQL
        //SELECT * FROM sqlite_master WHERE type = "table"
        Cursor c = buscar("sqlite_master", null, "type = 'table'", "");

        //Cria tabelas do banco de dados caso o mesmo estiver vazio.
        //Sempre os bancos criados pelo método openOrCreateDatabase() possuem uma tabela padrão "android_metadata"
        if(c.getCount() == 1){
            for(int i = 0; i < SCRIPT_DATABASE_CREATE.length; i++){
                db.execSQL(SCRIPT_DATABASE_CREATE[i]);
            }
            Log.i("BANCO_DADOS", "Criou tabelas do banco e as populou.");
        }
        else{
            //Banco já criado
            //precisamos garantir que os hashes dos resources sejam os mesmos

            //primeiro buscamos no banco os dados dos pokemons
            c = buscar("pokemon", new String[]{"idPokemon,foto,icone"}, "", "");

            Class res = R.drawable.class;
            while (c.moveToNext()){
                int idPokemon = c.getColumnIndex("idPokemon");
                int fotoCol = c.getColumnIndex("foto");
                int iconeCol = c.getColumnIndex("icone");

                int id = c.getInt(idPokemon);
                int foto = c.getInt(fotoCol);
                int icone = c.getInt(iconeCol);
                try {
                    //recuperamos os resources de um pokemon específico
                    Field idFoto = res.getDeclaredField("p"+id);
                    Field idIcone = res.getDeclaredField("i"+id);

                    //se o hash do icone ou foto for diferente atualizamos o hash do banco
                    if(idFoto.getInt(null) != foto || idIcone.getInt(null) != icone ){
                        ContentValues ct = new ContentValues();
                        ct.put("foto", idFoto.getInt(null));
                        ct.put("icone", idIcone.getInt(null));
                        atualizar("pokemon", ct, "idPokemon="+id);
                    }

                } catch (NoSuchFieldException e) {
                    Log.e("BANCO_DADOS", "Imagem de pokemon acessado não existe. idPokemon="+id);
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    Log.e("BANCO_DADOS", "Sistema não pemitiu acesso ao resource de imagem do pokemon. idPokemon="+id);
                    e.printStackTrace();
                }
            }
        }

        c.close();
        Log.i("BANCO_DADOS", "Abriu conexão com o banco.");
    }

    public static BancoDadosSingleton getInstance(){
        return INSTANCE;
    }

    // Insere um novo registro
    public long inserir(String tabela, ContentValues valores) {
        long id = db.insert(tabela, null, valores);

        Log.i("BANCO_DADOS", "Cadastrou registro com o id [" + id + "]");
        return id;
    }

    // Atualiza registros
    public int atualizar(String tabela, ContentValues valores, String where) {
        int count = db.update(tabela, valores, where, null);

        Log.i("BANCO_DADOS", "Atualizou [" + count + "] registros");
        return count;
    }

    // Deleta registros
    public int deletar(String tabela, String where) {
        int count = db.delete(tabela, where, null);

        Log.i("BANCO_DADOS", "Deletou [" + count + "] registros");
        return count;
    }

    // Busca registros
    public Cursor buscar(String tabela, String colunas[], String where, String orderBy) {
        Cursor c;
        if(!where.equals(""))
            c = db.query(tabela, colunas, where, null, null, null, orderBy);
        else
            c = db.query(tabela, colunas, null, null, null, null, orderBy);

        Log.i("BANCO_DADOS", "Realizou uma busca e retornou [" + c.getCount() + "] registros.");
        return c;
    }

    // Abre conexão com o banco
    public void abrir() {
        Context ctx = MyApp.getAppContext();
        // Abre o banco de dados já existente
        db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
        Log.i("BANCO_DADOS", "Abriu conexão com o banco.");
    }

    // Fecha o banco
    public void fechar() {
        // fecha o banco de dados
        if (db != null) {
            db.close();
            Log.i("BANCO_DADOS", "Fechou conexão com o Banco.");
        }
    }
}
