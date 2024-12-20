package org.semyonq;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorkArbitrage extends Thread {

    private final ConcurrentLinkedQueue<User> tasks = new ConcurrentLinkedQueue<>();
    private final ArrayList<Elevator> elevators;

    public WorkArbitrage(ArrayList<Elevator> els) {
        elevators = els;
    }

    public void addTask(User newTask) {
        tasks.add(newTask);
        System.out.println("Task added: " + newTask);
    }

    @Override
    public void run() {
        while (true) {
            User user = tasks.poll();
            if(user == null) continue;
            setTaskToElevator(user);
        }
    }

    private synchronized void setTaskToElevator(User user) {
        System.out.println("Got user, do smth...\n\n");
        Elevator bestEl = null;
        int closestFloor = Config.MAX_FLOOR - Config.MIN_FLOOR;
        for(Elevator el : elevators) {
            if (el.getDirection() == Direction.NONE || el.getDirection() == user.getDirection()) {
                int floorDiff = Math.abs(el.getCurrentFloor() - user.getFromFloor());
                if(floorDiff < closestFloor) {
                    closestFloor = floorDiff;
                    bestEl = el;
                }
            }
        }

        if(bestEl != null) {
            bestEl.addTask(user);
            return;
        }

        System.out.println("No available elevators. Return user to queue: " + user);
        addTask(user);
    }
}
