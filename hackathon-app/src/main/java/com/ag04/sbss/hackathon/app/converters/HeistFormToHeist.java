package com.ag04.sbss.hackathon.app.converters;

import com.ag04.sbss.hackathon.app.forms.HeistForm;
import com.ag04.sbss.hackathon.app.forms.RequiredSkillForm;
import com.ag04.sbss.hackathon.app.model.Heist;
import com.ag04.sbss.hackathon.app.model.RequiredSkill;
import com.ag04.sbss.hackathon.app.model.StatusHeist;
import com.sun.istack.Nullable;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class HeistFormToHeist implements Converter<HeistForm, Heist> {

    private final RequiredSkillFormToRequiredSkill requiredSkillFormToRequiredSkill;

    public HeistFormToHeist(RequiredSkillFormToRequiredSkill requiredSkillFormToRequiredSkill) {
        this.requiredSkillFormToRequiredSkill = requiredSkillFormToRequiredSkill;
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

        Set<RequiredSkill> skills = new HashSet<>();
        for(RequiredSkillForm skillForm : source.getSkills()) {
            RequiredSkill requiredSkill = requiredSkillFormToRequiredSkill.convert(skillForm);

            for(RequiredSkill reqSkill : skills) {
                if(reqSkill.getSkill().getName().equals(requiredSkill.getSkill().getName())
                    && reqSkill.getLevel().equals(requiredSkill.getLevel())) {
                    throw new RuntimeException("Cannot add multiple required skills with same name and level.");
                }
            }

            skills.add(requiredSkill);
        }
        heist.setSkills(skills);

        return heist;
    }
}
