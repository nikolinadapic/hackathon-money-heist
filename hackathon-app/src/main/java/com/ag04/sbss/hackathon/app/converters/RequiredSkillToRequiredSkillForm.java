package com.ag04.sbss.hackathon.app.converters;

import com.ag04.sbss.hackathon.app.forms.RequiredSkillForm;
import com.ag04.sbss.hackathon.app.model.RequiredSkill;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RequiredSkillToRequiredSkillForm implements Converter<RequiredSkill, RequiredSkillForm> {
    @Override
    public RequiredSkillForm convert(RequiredSkill source) {
        RequiredSkillForm requiredSkillForm = new RequiredSkillForm();

        requiredSkillForm.setName(source.getSkill().getName());
        requiredSkillForm.setLevel(source.getLevel());
        requiredSkillForm.setMembers(source.getMembers());

        return requiredSkillForm;
    }
}
