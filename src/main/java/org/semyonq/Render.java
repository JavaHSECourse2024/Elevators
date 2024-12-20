package org.semyonq;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class Render extends JPanel implements Runnable {
    private final ArrayList<Elevator> elevators;
    private static int[] floorY = new int[Config.MAX_FLOOR - Config.MIN_FLOOR + 1];

    private final long SEC = 1_000_000_000;
    private final long FRAME_TIME = SEC / Config.FPS; // 1 sec in nano / 60 frames

    private long lastFrameTime = System.nanoTime();
    private final ArrayBlockingQueue<Integer> upTasks = new ArrayBlockingQueue<>(Config.MAX_FLOOR - Config.MIN_FLOOR + 1);
    private final ArrayBlockingQueue<Integer> downTasks = new ArrayBlockingQueue<>(Config.MAX_FLOOR - Config.MIN_FLOOR + 1);

    public Render(ArrayList<Elevator> els) {
        elevators = els;
        final int startY = Config.SCREEN_HEIGHT - (Config.ELEVATOR_HEIGHT + Config.PADDING);
        for(int i = 0; i < Config.N_WORKERS; i++)
            elevators.add(new Elevator(i + 1, (i + 1) * Config.ELEVATOR_PADDING + Config.ELEVATOR_WIDTH * i, startY));
        setPreferredSize(new Dimension(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT));
    }

    public static int convertFloorToY(int floor) {
        floor -= Config.MIN_FLOOR;
        return floorY[floor];
    }

    @Override
    public void run() {
        while (true) {
            long currentTime = System.nanoTime();
            long deltaTime = currentTime - lastFrameTime;
            double deltaTimeInSeconds = deltaTime / 1_000_000_000.0;

            for(Elevator el : elevators)
                el.move(deltaTimeInSeconds);

            lastFrameTime = currentTime;

            long frameTime = System.nanoTime() - currentTime;
            long sleepTime = FRAME_TIME - frameTime;

            if(sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1_000_000, (int)(sleepTime % 1_000_000));
                } catch (InterruptedException e) {
                    System.out.println("Processor interrupted. Exiting...");
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        int floors_num = Config.MAX_FLOOR - Config.MIN_FLOOR + 1;
        int lineInterval = Config.SCREEN_HEIGHT / floors_num;
        for (int i = 0; i < floors_num; i++) {
            int startY = Config.SCREEN_HEIGHT - (i * lineInterval + Config.PADDING);
            floorY[i] = startY - Config.ELEVATOR_HEIGHT + 1;
            g.fillRect(Config.PADDING, startY, Config.SCREEN_WIDTH - Config.PADDING * 2, 2);  // Horizontal stripes

            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString(String.valueOf(i + 1), Config.PADDING, startY - 5);
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString("Вверх: " + WorkArbitrage.getCountTasks(Direction.UP, i), Config.PADDING * 3, startY - 30);
            g.drawString("Вниз : " + WorkArbitrage.getCountTasks(Direction.DOWN, i), Config.PADDING * 3, startY - 5);
        }

        for(Elevator el : elevators)
            el.paint(g);

    }
}
