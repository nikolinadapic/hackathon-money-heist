package com.ag04.sbss.hackathon.app.converters;

import com.ag04.sbss.hackathon.app.forms.MemberSkillForm;
import com.ag04.sbss.hackathon.app.model.MemberSkill;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MemberSkillToMemberSkillForm implements Converter<MemberSkill, MemberSkillForm> {

    @Override
    public MemberSkillForm convert(MemberSkill source) {
        MemberSkillForm memberSkillForm = new MemberSkillForm();

        memberSkillForm.setName(source.getSkill().getName());
        memberSkillForm.setLevel(source.getLevel());

        return  memberSkillForm;
    }
}
