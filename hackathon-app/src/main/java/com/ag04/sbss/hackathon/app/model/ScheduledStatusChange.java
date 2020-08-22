package com.ag04.sbss.hackathon.app.model;

public class ScheduledStatusChange implements Runnable {
    private Heist heist;
    private StatusHeist newStatus;

    public ScheduledStatusChange(Heist heist, StatusHeist newStatus){
        this.heist = heist;
        this.newStatus = newStatus;
    }

    @Override
    public void run() {
        heist.setStatus(newStatus);

        System.out.println("set new status " + newStatus);
    }
}
