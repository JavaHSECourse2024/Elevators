package org.semyonq;

import javax.swing.*;
import java.util.ArrayList;

public class Main {
    private static final ArrayList<Elevator> elevators = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello world!");
        JFrame frame = new JFrame("Elevator Simulation");
        Render render = new Render(elevators);
        frame.add(render);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        new Thread(render).start();
        WorkArbitrage workArbitrage = new WorkArbitrage(elevators);
        workArbitrage.start();
//        workArbitrage.addTask(new Task());
//        Thread.sleep(4000);
//        workArbitrage.addTask(new Task(1));
        new TaskGenerator(workArbitrage).start();
    }
}