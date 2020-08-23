package com.ag04.sbss.hackathon.app.converters;

import com.ag04.sbss.hackathon.app.forms.MemberSkillDTO;
import com.ag04.sbss.hackathon.app.model.MemberSkill;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MemberSkillToMemberSkillDTO implements Converter<MemberSkill, MemberSkillDTO> {

    @Override
    public MemberSkillDTO convert(MemberSkill source) {
        MemberSkillDTO memberSkillDTO = new MemberSkillDTO();

        memberSkillDTO.setName(source.getSkill().getName());
        memberSkillDTO.setLevel(source.getLevel());

        return memberSkillDTO;
    }
}
