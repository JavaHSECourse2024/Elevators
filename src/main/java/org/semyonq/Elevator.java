package org.semyonq;

import java.awt.*;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Elevator {
    // Очередь вызовов сортированная, по этажам. Гарантируется, что в очереди не может быть этажа ниже текущего.
    private PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>();

    private int currentFloor = Config.MIN_FLOOR;
    private Direction dir = Direction.NONE;

    private final int number;

    private final double EPS = 1.5;

    private double tempSleep = 0;
    int activeTasks = 0;

    public void addTask(Task task) {
        dir = task.getDirection();
        queue.add(task);
        System.out.println("Add to " + this + " " + task + "\n");
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
        Task[] tasks_copy = new Task[queue.size()];
        queue.toArray(tasks_copy);

        for(Task task : tasks_copy) {
            if(task.getFromFloor() == currentFloor) {
                anyChanges = true;
                queue.remove(task);
                if(task.getDirection() != Direction.NONE) {
                    activeTasks++;
                    task.genDestFloor();
                    queue.add(new Task(task.getDestFloor(), Direction.NONE));
                }
                else
                    activeTasks--;
            }
        }
        if(anyChanges)
            tempSleep = Config.SMALL_INTERVAl;
        if(queue.isEmpty()) {
            dir = Direction.NONE;
            System.out.println(this + " finished task queue\n");
        }
    }

    private boolean circleY(double y1) {
        return Math.abs(y1 - y) < EPS;
    }

    public void move(double delta) {
        if(dir == Direction.NONE || tempSleep > 0) {
            tempSleep -= delta;
            return;
        }
        int toY = getDestY(queue.peek().getFromFloor());
        if(circleY(toY)) {
            getNextTask();
            return;
        }
        int sign = Integer.signum((int) (toY - y));
        y = y + sign * (delta * Config.SPEED);

        if(!circleY(getDestY(currentFloor)) && getDestY(currentFloor) > y && sign < 0) {
            currentFloor++;
//            System.out.println(this + "'s current floor is " + currentFloor);
        }
        else if(!circleY(getDestY(currentFloor)) && getDestY(currentFloor) < y && sign > 0) {
            currentFloor--;
//            System.out.println(this + "'s current floor is " + currentFloor);
        }
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect((int) x, (int) y, Config.ELEVATOR_WIDTH, Config.ELEVATOR_HEIGHT);  // Рисуем лифт (прямоугольник)
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));

        // Вычисляем позицию текста в центре лифта
        FontMetrics fm = g.getFontMetrics();
        String countText = String.valueOf(activeTasks);
        int textWidth = fm.stringWidth(countText);
        int textHeight = fm.getAscent();
        int textX = (int) (x + (Config.ELEVATOR_WIDTH - textWidth) / 2);
        int textY = (int) (y + (Config.ELEVATOR_HEIGHT + textHeight) / 2);

        g.drawString(countText, textX, textY);
    }
}
