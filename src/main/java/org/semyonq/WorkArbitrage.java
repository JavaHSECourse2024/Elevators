package org.semyonq;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorkArbitrage extends Thread {

    private final ConcurrentLinkedQueue<Task> tasks = new ConcurrentLinkedQueue<>();
    private final ArrayList<Elevator> elevators;

    private static final int[] upTasks = new int[Config.MAX_FLOOR - Config.MIN_FLOOR + 1];
    private static final int[] downTasks = new int[Config.MAX_FLOOR - Config.MIN_FLOOR + 1];

    public static int getCountTasks(Direction dir, int floor_idx) {
        if(dir == Direction.UP) {
            return upTasks[floor_idx];
        } else if (dir == Direction.DOWN) {
            return downTasks[floor_idx];
        }
        return 0;
    }

    public static synchronized void upTaskCount(Direction dir, int floor) {
        if(dir == Direction.UP) {
            upTasks[floor - Config.MIN_FLOOR]++;
        } else if (dir == Direction.DOWN) {
            downTasks[floor - Config.MIN_FLOOR]++;
        }
    }

    public static synchronized void downTaskCount(Direction dir, int floor) {
        if(dir == Direction.UP) {
            upTasks[floor - Config.MIN_FLOOR]--;
        } else if (dir == Direction.DOWN) {
            downTasks[floor - Config.MIN_FLOOR]--;
        }
    }

    public WorkArbitrage(ArrayList<Elevator> els) {
        elevators = els;
    }

    public void addTask(Task newTask) {
        tasks.add(newTask);
        upTaskCount(newTask.getDirection(), newTask.getFromFloor());
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
        tasks.add(task);
        Thread.sleep(Config.SMALL_INTERVAl * 1000);
    }
}
