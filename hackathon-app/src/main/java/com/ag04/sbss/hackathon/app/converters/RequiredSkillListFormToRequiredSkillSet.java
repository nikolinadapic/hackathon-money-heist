package com.ag04.sbss.hackathon.app.converters;

import com.ag04.sbss.hackathon.app.forms.RequiredSkillListForm;
import com.ag04.sbss.hackathon.app.model.RequiredSkill;
import com.ag04.sbss.hackathon.app.services.RequiredSkillService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RequiredSkillListFormToRequiredSkillSet implements Converter<RequiredSkillListForm, Set<RequiredSkill>> {

    private final RequiredSkillService requiredSkillService;

    public RequiredSkillListFormToRequiredSkillSet(RequiredSkillService requiredSkillService) {
        this.requiredSkillService = requiredSkillService;
    }

    @Override
    public Set<RequiredSkill> convert(RequiredSkillListForm source) {
        return requiredSkillService.convertToSkillSet(source.getSkills());
    }
}
