package org.semyonq;

import java.awt.*;
import java.util.concurrent.PriorityBlockingQueue;

public class Elevator {
    // Очередь вызовов сортированная, по этажам. Гарантируется, что в очереди не может быть этажа ниже текущего.
    private PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>();

    private int lastFloor = Config.MIN_FLOOR;
    private int currentFloor = Config.MIN_FLOOR;
    private Direction dir = Direction.NONE;

    private final int number;

    private final int EPS = 1;

    private double tempSleep = 0;

    public void addTask(Task task) {
        dir = task.getDirection();
        queue.add(task);
        System.out.println("Add to " + this + " " + task);
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

    private int getDestY(int floor) {
        return Render.convertFloorToY(floor);
    }

    private void getNextTask() {
        boolean anyChanges = false;
        for(Task task : queue) {
            if(task.getFromFloor() == currentFloor) {
                anyChanges = true;
                if(task.getDirection() != Direction.NONE) {
                    task.genDestFloor();
                    queue.add(new Task(task.getDestFloor(), Direction.NONE));
                }
                queue.remove(task);
            }
        }
        if(anyChanges)
            tempSleep = Config.SMALL_INTERVAl;
        if(queue.isEmpty()) {
            dir = Direction.NONE;
            System.out.println("Finish task queue");
        }
    }

    public void move(double delta) {
        if(dir == Direction.NONE || tempSleep > 0) {
            tempSleep -= delta;
            return;
        }
        int toY = getDestY(queue.peek().getFromFloor());
        if(Math.abs(toY - y) < EPS) {
            getNextTask();
        }
        int sign = Integer.signum((int) (toY - y));
        y = y + sign * (delta * Config.SPEED);

        if(getDestY(currentFloor) >= y && sign < 0) {
            currentFloor += 1;
            System.out.println("Current floor is " + currentFloor);
        }
        else if(getDestY(currentFloor) <= y && sign > 0) {
            currentFloor -= 1;
            System.out.println("Current floor is " + currentFloor);
        }
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect((int) x, (int) y, Config.ELEVATOR_WIDTH, Config.ELEVATOR_HEIGHT);  // Рисуем лифт (прямоугольник)
    }
}
