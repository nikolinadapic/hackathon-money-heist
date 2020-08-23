package com.ag04.sbss.hackathon.app.scheduling;

/**
 * Created by Vitomir M on 23.8.2020.
 */

import com.ag04.sbss.hackathon.app.model.Heist;
import com.ag04.sbss.hackathon.app.model.StatusHeist;
import com.ag04.sbss.hackathon.app.repositories.HeistRepository;

import java.util.Optional;

public class ScheduledStatusChange implements Runnable {
    private String heistName;
    private StatusHeist newStatus;
    private HeistRepository heistRepository;

    public ScheduledStatusChange(String heistName, StatusHeist newStatus, HeistRepository heistRepository) {
        this.heistName = heistName;
        this.newStatus = newStatus;
        this.heistRepository = heistRepository;
    }

    @Override
    public void run() {
        Optional<Heist> heistOptional = heistRepository.findByName(heistName);

        if (heistOptional.isPresent()) {
            heistOptional.get().setStatus(newStatus);
            heistRepository.save(heistOptional.get());
        }
    }
}