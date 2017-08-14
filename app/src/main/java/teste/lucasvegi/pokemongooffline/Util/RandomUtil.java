package teste.lucasvegi.pokemongooffline.Util;

import java.util.Random;

/**
 * Created by Lucas on 13/12/2016.
 */
public class RandomUtil {

    public static double randomDoubleInRange(double min, double max) {
        Random random = new Random();
        double range = max - min;
        double scaled = random.nextDouble() * range;
        double shifted = scaled + min;
        return shifted; // == (rand.nextDouble() * (max-min)) + min;
    }

    public static int randomIntInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt(max-min) + min;
    }
}
