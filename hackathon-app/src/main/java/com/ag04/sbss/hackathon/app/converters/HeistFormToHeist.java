package com.ag04.sbss.hackathon.app.converters;

import com.ag04.sbss.hackathon.app.forms.HeistForm;
import com.ag04.sbss.hackathon.app.model.Heist;
import com.ag04.sbss.hackathon.app.model.StatusHeist;
import com.ag04.sbss.hackathon.app.services.RequiredSkillService;
import com.sun.istack.Nullable;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class HeistFormToHeist implements Converter<HeistForm, Heist> {

    private final RequiredSkillService requiredSkillService;

    public HeistFormToHeist(RequiredSkillService requiredSkillService) {
        this.requiredSkillService = requiredSkillService;
    }

    @Synchronized
    @Nullable
    @Override
    public Heist convert(HeistForm source) {
        if(source == null) {
            return null;
        }

        Heist heist = new Heist();

        heist.setName(source.getName());
        heist.setLocation(source.getLocation());
        heist.setStartTime(source.getStartTime());
        heist.setEndTime(source.getEndTime());
        heist.setStatus(StatusHeist.PLANNING);
        heist.setSkills(requiredSkillService.convertToSkillSet(source.getSkills()));

        return heist;
    }
}
