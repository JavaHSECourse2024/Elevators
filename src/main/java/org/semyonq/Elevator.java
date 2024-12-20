package org.semyonq;

import java.awt.*;
import java.util.concurrent.PriorityBlockingQueue;

public class Elevator {
    // Очередь вызовов сортированная, по этажам. Гарантируется, что в очереди не может быть этажа ниже текущего.
    private PriorityBlockingQueue<User> queue = new PriorityBlockingQueue<>();

    private int lastFloor = Config.MIN_FLOOR;
    private int currentFloor = Config.MIN_FLOOR;
    private Direction dir = Direction.NONE;

    private final int number;

    public void addTask(User user) {
        queue.add(user);
        System.out.println("Add to " + this + " " + user);
    }

    @Override
    public String toString() {
        return String.format("Elevator %d", number);
    }

    public Direction getDirection() {
        return dir;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    private double x, y;

    public Elevator(int number, double x, double y) {
        this.number = number;
        this.x = x;
        this.y = y;
    }

    public void move(double delta) {
        if(dir == Direction.NONE)
            return;

        if (dir == Direction.UP && y > 0) {
            y -= delta * Config.SPEED;
        } else if (dir == Direction.DOWN && y < Config.MAX_FLOOR * 40) {
            y += delta * Config.SPEED;
        }

        // Меняем направление движения, если лифт достиг верхнего или нижнего этажа
        if (y <= 0 || y >= Config.MAX_FLOOR * 40) {
            dir = (dir == Direction.UP) ? Direction.DOWN : Direction.UP;
        }
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect((int) x, (int) y, Config.ELEVATOR_WIDTH, Config.ELEVATOR_HEIGHT);  // Рисуем лифт (прямоугольник)
    }
}
