package org.semyonq;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Render extends JPanel implements Runnable {
    private final ArrayList<Elevator> elevators;
    private static int[] floorY = new int[Config.MAX_FLOOR - Config.MIN_FLOOR + 1];

    private final long SEC = 1_000_000_000;
    private final long FRAME_TIME = SEC / Config.FPS; // 1 sec in nano / 60 frames

    private long lastFrameTime = System.nanoTime();

    public Render(ArrayList<Elevator> els) {
        elevators = els;
        final int startY = Config.SCREEN_HEIGHT - (Config.ELEVATOR_HEIGHT + Config.PADDING);
        for(int i = 0; i < Config.N_WORKERS; i++)
            elevators.add(new Elevator(i + 1, (i + 1) * Config.ELEVATOR_PADDING + Config.ELEVATOR_WIDTH * i, startY));
        setPreferredSize(new Dimension(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT));
    }

    public static int convertFloorToY(int floor) {
        return floorY[floor - 1];
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
        int floors_num = Config.MAX_FLOOR - Config.MIN_FLOOR;
        int lineInterval = Config.SCREEN_HEIGHT / (floors_num + 1);
        for (int i = 0; i <= floors_num; i++) {
            int startY = Config.SCREEN_HEIGHT - (i * lineInterval + Config.PADDING);
            floorY[i] = startY - Config.ELEVATOR_HEIGHT + 1;
            g.fillRect(Config.PADDING, startY, Config.SCREEN_WIDTH - Config.PADDING * 2, 2);  // Horizontal stripes
        }

        for(Elevator el : elevators)
            el.paint(g);
    }
}
