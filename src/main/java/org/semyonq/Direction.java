package org.semyonq;

import java.util.Random;

public enum Direction {
    UP,
    DOWN;

    private static final Direction[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();

    public static Direction getRandom() {
        return VALUES[RANDOM.nextInt(SIZE)];
    }
}
