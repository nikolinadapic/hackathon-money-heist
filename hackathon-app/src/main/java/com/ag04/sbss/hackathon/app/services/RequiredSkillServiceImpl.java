package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.converters.RequiredSkillFormToRequiredSkill;
import com.ag04.sbss.hackathon.app.converters.RequiredSkillToRequiredSkillForm;
import com.ag04.sbss.hackathon.app.forms.RequiredSkillForm;
import com.ag04.sbss.hackathon.app.model.RequiredSkill;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class RequiredSkillServiceImpl implements RequiredSkillService {

    private final RequiredSkillFormToRequiredSkill requiredSkillFormToRequiredSkill;
    private final RequiredSkillToRequiredSkillForm requiredSkillToRequiredSkillForm;

    public RequiredSkillServiceImpl(RequiredSkillFormToRequiredSkill requiredSkillFormToRequiredSkill, RequiredSkillToRequiredSkillForm requiredSkillToRequiredSkillForm) {
        this.requiredSkillFormToRequiredSkill = requiredSkillFormToRequiredSkill;
        this.requiredSkillToRequiredSkillForm = requiredSkillToRequiredSkillForm;
    }

    @Override
    public Set<RequiredSkill> convertToSkillSet(List<RequiredSkillForm> forms) {
        Set<RequiredSkill> skills = new HashSet<>();
        for(RequiredSkillForm skillForm : forms) {
            RequiredSkill requiredSkill = requiredSkillFormToRequiredSkill.convert(skillForm);

            for(RequiredSkill reqSkill : skills) {
                if(reqSkill.getSkill().getName().equals(requiredSkill.getSkill().getName())
                        && reqSkill.getLevel().equals(requiredSkill.getLevel())) {
                    throw new RuntimeException("Cannot add multiple required skills with same name and level.");
                }
            }

            skills.add(requiredSkill);
        }

        return skills;
    }

    public List<RequiredSkillForm> convertToSkillFormList(Set<RequiredSkill> skills) {
        List<RequiredSkillForm> skillForms = new LinkedList<>();

        for(RequiredSkill skill : skills) {
            skillForms.add(requiredSkillToRequiredSkillForm.convert(skill));
        }

        return skillForms;
    }
}
