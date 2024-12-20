package org.semyonq;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorkArbitrage extends Thread {

    private final ConcurrentLinkedQueue<Task> tasks = new ConcurrentLinkedQueue<>();
    private final ArrayList<Elevator> elevators;

    public WorkArbitrage(ArrayList<Elevator> els) {
        elevators = els;
    }

    public void addTask(Task newTask) {
        tasks.add(newTask);
    }

    @Override
    public void run() {
        while (true) {
            Task task = tasks.poll();
            if(task == null) continue;
            try {
                setTaskToElevator(task);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private synchronized void setTaskToElevator(Task task) throws InterruptedException {
        Elevator bestEl = null;
        int closestFloor = Config.MAX_FLOOR - Config.MIN_FLOOR + 1;
        for(Elevator el : elevators) {
            if (el.getDirection() == Direction.NONE ||
                    (el.getDirection() == task.getDirection() &&
                        ((task.getDirection() == Direction.DOWN && el.getCurrentFloor() >= task.getFromFloor()) ||
                            (task.getDirection() == Direction.UP && el.getCurrentFloor() <= task.getFromFloor())
                        )
                    )
            ) {
                int floorDiff = Math.abs(el.getCurrentFloor() - task.getFromFloor());
                if(floorDiff < closestFloor) {
                    closestFloor = floorDiff;
                    bestEl = el;
                }
            }
        }

        if(bestEl != null) {
            bestEl.addTask(task);
            return;
        }

//        System.out.println("No available elevators. Return task to queue: " + task);
        addTask(task);
        Thread.sleep(Config.SMALL_INTERVAl * 1000);
    }
}
