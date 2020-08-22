package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.converters.RequiredSkillListFormToRequiredSkillSet;
import com.ag04.sbss.hackathon.app.forms.RequiredSkillListForm;
import com.ag04.sbss.hackathon.app.model.Heist;
import com.ag04.sbss.hackathon.app.model.StatusHeist;
import com.ag04.sbss.hackathon.app.repositories.HeistRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HeistServiceImpl implements HeistService {

    private final HeistRepository heistRepository;
    private final RequiredSkillListFormToRequiredSkillSet requiredSkillListFormToRequiredSkillSet;

    public HeistServiceImpl(HeistRepository heistRepository, RequiredSkillListFormToRequiredSkillSet requiredSkillListFormToRequiredSkillSet) {
        this.heistRepository = heistRepository;
        this.requiredSkillListFormToRequiredSkillSet = requiredSkillListFormToRequiredSkillSet;
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

    @Override
    public void updateHeistSkills(Long id, RequiredSkillListForm requiredSkillListForm) {
        Optional<Heist> heistOptional = heistRepository.findById(id);

        if(heistOptional.isEmpty()) {
            throw new RuntimeException("Heist with given ID does not exist.");
        }

        if(heistOptional.get().getStatus().equals(StatusHeist.IN_PROGRESS) || heistOptional.get().getStatus().equals(StatusHeist.FINISHED)) {
            throw new RuntimeException("Heist has already started.");
        }

        heistOptional.get().setSkills(requiredSkillListFormToRequiredSkillSet.convert(requiredSkillListForm));

        heistRepository.save(heistOptional.get());
    }

    @Override
    public void startHeist(Long id) {
        Optional<Heist> heistOptional = heistRepository.findById(id);

        if (heistOptional.isEmpty()) {
            throw new RuntimeException("Heist with given ID does not exist.");
        }

        if (!heistOptional.get().getStatus().equals(StatusHeist.READY)) {
            throw new RuntimeException("Heist is not ready.");
        }

        heistOptional.get().setStatus(StatusHeist.IN_PROGRESS);

        heistRepository.save(heistOptional.get());
    }
}
