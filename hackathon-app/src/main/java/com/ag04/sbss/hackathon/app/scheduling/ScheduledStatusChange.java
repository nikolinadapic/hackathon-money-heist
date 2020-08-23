package com.ag04.sbss.hackathon.app.scheduling;

import com.ag04.sbss.hackathon.app.model.Heist;
import com.ag04.sbss.hackathon.app.model.StatusHeist;
import com.ag04.sbss.hackathon.app.repositories.HeistRepository;

public class ScheduledStatusChange implements Runnable {
    private Heist heist;
    private StatusHeist newStatus;
    private HeistRepository heistRepository;

    public ScheduledStatusChange(Heist heist, StatusHeist newStatus, HeistRepository heistRepository){
        this.heist = heist;
        this.newStatus = newStatus;
        this.heistRepository = heistRepository;
    }

    @Override
    public void run() {
        heist.setStatus(newStatus);

        heistRepository.save(heist);
    }
}
