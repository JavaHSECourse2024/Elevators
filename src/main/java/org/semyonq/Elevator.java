package org.semyonq;

import java.awt.*;
import java.util.concurrent.PriorityBlockingQueue;

public class Elevator implements Runnable {
    // Очередь вызовов сортированная, по этажам. Гарантируется, что в очереди не может быть этажа ниже текущего.
    private PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue<>();

    private int last_floor = Config.MIN_FLOOR;
    private int current_floor = Config.MIN_FLOOR;
    private Direction dir = Direction.UP;

    private double x, y;

    public Elevator(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void move(double delta) {
        System.out.println(delta);
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

    @Override
    public void run() {
        System.out.println("Run elevator");
    }
}
