package org.semyonq;

import java.util.Random;

public class Config {
    private final static Random RANDOM = new Random();
    // МОЖНО НАСТРАИВАТЬ ------
    public static final int MAX_FLOOR = 10;
    public static final int MIN_FLOOR = 1;

    public static final int N_WORKERS = 3; // Elevators count
    public static final int MIN_INTERVAL = 1;  // For task generation in sec
    public static final int MAX_INTERVAL = 3; // For task generation in sec
    public static final int SMALL_INTERVAl = 1; // For staying in floor

    public static final int FPS = 60;
    public static final double SPEED = 100;
    // ------
    public static final int PADDING = 10;
    public static final int ELEVATOR_PADDING = 90;
    public static final int ELEVATOR_WIDTH = 60;
    public static final int ELEVATOR_HEIGHT = 80;
    public static final int SCREEN_WIDTH = ELEVATOR_WIDTH * N_WORKERS + ELEVATOR_PADDING * (N_WORKERS + 1);
    public static final int SCREEN_HEIGHT = 700;

    private Config() { }

    public static int getRandomFloor() {
        return Config.MIN_FLOOR + RANDOM.nextInt(Config.MAX_FLOOR - Config.MIN_FLOOR + 1);
    }

    public static int getRandomInterval() {
        return (Config.MIN_INTERVAL + RANDOM.nextInt(Config.MAX_INTERVAL - Config.MIN_INTERVAL + 1)) * 1000;
    }
}
