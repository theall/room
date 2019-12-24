package lan.client.game.base;

import java.util.Random;

public class RandomWrapper {
    private Random random;
    private static RandomWrapper randomWrapper;
    private RandomWrapper(long seed) {
        random = new Random(seed);
    }

    public synchronized static void initialize(long seed)  {
        if(randomWrapper == null) {
            randomWrapper = new RandomWrapper(seed);
        }
    }

    public static RandomWrapper getInstance() {
        return randomWrapper;
    }

    public int nextInt(int bound) {
        return random.nextInt(bound);
    }
    
    public int nextInt(int from, int to) {
        return random.nextInt(to-from) + from;
    }
}
