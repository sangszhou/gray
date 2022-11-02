package gray.util;

import java.util.Random;

public class IdUtils {
    public static String generateId(String prefix) {
        long time = System.currentTimeMillis();
        int ranInt = new Random().nextInt(10000);

        String id = String.format("prefix-%d-%d", time, ranInt);
        return id;
    }
}
