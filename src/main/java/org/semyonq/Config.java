package org.semyonq;

public class Config {
    public static final int MAX_FLOOR = 10;
    public static final int MIN_FLOOR = 1;

    public static final int N_WORKERS = 3;

    public static final int FPS = 60;
    public static final double SPEED = 50;


    public static final int PADDING = 10;
    public static final int ELEVATOR_PADDING = 90;
    public static final int ELEVATOR_WIDTH = 60;
    public static final int ELEVATOR_HEIGHT = 80;
    public static final int SCREEN_WIDTH = ELEVATOR_WIDTH * N_WORKERS + ELEVATOR_PADDING * (N_WORKERS + 1);
    public static final int SCREEN_HEIGHT = 700;

    private Config() { }
}
