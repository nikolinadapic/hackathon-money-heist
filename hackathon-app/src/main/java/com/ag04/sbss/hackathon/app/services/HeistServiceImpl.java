package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.model.Heist;
import com.ag04.sbss.hackathon.app.repositories.HeistRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class HeistServiceImpl implements HeistService {

    private final HeistRepository heistRepository;

    public HeistServiceImpl(HeistRepository heistRepository) {
        this.heistRepository = heistRepository;
    }

    @Override
    public void addHeist(Heist heist) {
        Optional<Heist> heistOptional = heistRepository.findByName(heist.getName());

        if(heistOptional.isPresent()) {
            throw new RuntimeException("Heist with name " + heist.getName() + " already exists");
        }

        if(heist.getStartTime().after(heist.getEndTime()) || heist.getEndTime().before(new Date(System.currentTimeMillis()))) {
            throw new RuntimeException("Invalid date.");
        }

        heistRepository.save(heist);
    }
}
