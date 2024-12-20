package org.semyonq;

public class Task implements Comparable<Task> {
    private final int fromFloor;
    private final Direction direction;

    private int destFloor;

    public Task(int fromFloor, Direction direction) {
        if(fromFloor < Config.MIN_FLOOR || fromFloor > Config.MAX_FLOOR)
            throw new RuntimeException("Bad value for fromFloor");
        if(fromFloor == Config.MAX_FLOOR && direction == Direction.UP)
            throw new RuntimeException("You cannot move UP from MAX_FLOOR.");
        else if (fromFloor == Config.MIN_FLOOR && direction == Direction.DOWN)
            throw new RuntimeException("You cannot move DOWN from MIN_FLOOR.");

        this.fromFloor = fromFloor;
        this.direction = direction;
    }

    public Task(int fromFloor) {
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

    @Override
    public String toString() {
        return String.format("Task(FromFloor = %d, Dir = %s)", fromFloor, direction.toString());
    }

    public Task() {
        this(Config.getRandomFloor());
    }

    public void genDestFloor() {
        do {
            destFloor = Config.getRandomFloor();
        } while (destFloor == fromFloor);
        System.out.println("Destination floor = " + destFloor);
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

    @Override
    public int compareTo(Task other) {
        return Integer.compare(this.destFloor, other.destFloor);
    }
}
