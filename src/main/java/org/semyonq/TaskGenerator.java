package org.semyonq;

public class TaskGenerator extends Thread {

    WorkArbitrage workArbitrage;

    public TaskGenerator(WorkArbitrage workArbitrage) {
        this.workArbitrage = workArbitrage;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(Config.getRandomInterval());
                Task task = new Task();
                System.out.println("Generated task: " + task);
                this.workArbitrage.addTask(task);
            } catch (InterruptedException e) {
                System.out.println("Processor interrupted. Exiting...");
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

}
