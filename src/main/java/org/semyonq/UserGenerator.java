package org.semyonq;

public class UserGenerator extends Thread {

    WorkArbitrage workArbitrage;

    public UserGenerator(WorkArbitrage workArbitrage) {
        this.workArbitrage = workArbitrage;
    }

    @Override
    public void run() {
        while (true) {
            try {
                long waitTime = Config.getRandomInterval();
                Thread.sleep(waitTime * 1000);
                User user = new User();
                System.out.println("Generated user: " + user);
                this.workArbitrage.addTask(user);
            } catch (InterruptedException e) {
                System.out.println("Processor interrupted. Exiting...");
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

}
