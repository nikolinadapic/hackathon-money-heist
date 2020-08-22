package com.ag04.sbss.hackathon.app.converters;

import com.ag04.sbss.hackathon.app.forms.RequiredSkillForm;
import com.ag04.sbss.hackathon.app.model.RequiredSkill;
import com.ag04.sbss.hackathon.app.model.Skill;
import com.ag04.sbss.hackathon.app.repositories.RequiredSkillRepository;
import com.ag04.sbss.hackathon.app.repositories.SkillRepository;
import com.sun.istack.Nullable;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RequiredSkillFormToRequiredSkill implements Converter<RequiredSkillForm, RequiredSkill> {

    private final SkillRepository skillRepository;
    private final RequiredSkillRepository requiredSkillRepository;

    public RequiredSkillFormToRequiredSkill(SkillRepository skillRepository, RequiredSkillRepository requiredSkillRepository) {
        this.skillRepository = skillRepository;
        this.requiredSkillRepository = requiredSkillRepository;
    }

    @Synchronized
    @Nullable
    @Override
    public RequiredSkill convert(RequiredSkillForm source) {
        RequiredSkill requiredSkill = new RequiredSkill();

        Optional<Skill> skillOptional = skillRepository.findByName(source.getName());
        if(skillOptional.isEmpty()) {
            Skill skill = skillRepository.save(new Skill(source.getName()));
            requiredSkill.setSkill(skill);
        } else {
            requiredSkill.setSkill(skillOptional.get());
        }

        requiredSkill.setLevel(source.getLevel());
        requiredSkill.setMembers(source.getMembers());

        return requiredSkill;
    }
}
