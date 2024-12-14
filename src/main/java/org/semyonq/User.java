package org.semyonq;

import java.util.Random;

public class User {
    private final int fromFloor;
    private final Direction direction;
    private final static Random RANDOM = new Random();

    private int destFloor;

    public User(int fromFloor, Direction direction) {
        if(fromFloor < Config.MIN_FLOOR || fromFloor > Config.MAX_FLOOR)
            throw new RuntimeException("Bad value for fromFloor");
        if(fromFloor == Config.MAX_FLOOR && direction == Direction.UP)
            throw new RuntimeException("You cannot move UP from MAX_FLOOR.");
        else if (fromFloor == Config.MIN_FLOOR && direction == Direction.DOWN)
            throw new RuntimeException("You cannot move DOWN from MIN_FLOOR.");

        this.fromFloor = fromFloor;
        this.direction = direction;
    }

    public User(int fromFloor) {
        if(fromFloor < Config.MIN_FLOOR || fromFloor > Config.MAX_FLOOR)
            throw new RuntimeException("Bad value for fromFloor");
        this.fromFloor = fromFloor;
        if(fromFloor == Config.MAX_FLOOR)
            direction = Direction.DOWN;
        else if (fromFloor == Config.MIN_FLOOR)
            direction = Direction.UP;
        else
            direction = Direction.getRandom();

    }

    public User() {
        this(RANDOM.nextInt(Config.MAX_FLOOR) + Config.MIN_FLOOR);
    }

    public void genDestFloor() {
        do {
            destFloor = RANDOM.nextInt(Config.MAX_FLOOR) + Config.MIN_FLOOR; // Генерируем число от 1 до 13
        } while (destFloor == fromFloor);
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getDestFloor() {
        return destFloor;
    }
}
