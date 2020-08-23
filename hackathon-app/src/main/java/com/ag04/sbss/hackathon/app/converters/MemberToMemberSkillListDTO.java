package com.ag04.sbss.hackathon.app.converters;

import com.ag04.sbss.hackathon.app.forms.MemberSkillDTO;
import com.ag04.sbss.hackathon.app.forms.MemberSkillListDTO;
import com.ag04.sbss.hackathon.app.model.Member;
import com.ag04.sbss.hackathon.app.model.MemberSkill;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Vitomir M on 23.8.2020.
 */
@Component
public class MemberToMemberSkillListDTO implements Converter<Member, MemberSkillListDTO> {

    private final MemberSkillToMemberSkillDTO memberSkillDTOConverter;

    public MemberToMemberSkillListDTO(MemberSkillToMemberSkillDTO memberSkillDTOConverter) {
        this.memberSkillDTOConverter = memberSkillDTOConverter;
    }

    @Override
    public MemberSkillListDTO convert(Member source) {
        MemberSkillListDTO listDTO = new MemberSkillListDTO();
        List<MemberSkillDTO> skillsList = new LinkedList<>();
        for(MemberSkill skill : source.getSkills()){
            skillsList.add(memberSkillDTOConverter.convert(skill));
        }
        listDTO.setSkills(skillsList);
        if(source.getMainSkill() != null)
        listDTO.setMainSkill(source.getMainSkill().getName());

        return listDTO;
    }
}
