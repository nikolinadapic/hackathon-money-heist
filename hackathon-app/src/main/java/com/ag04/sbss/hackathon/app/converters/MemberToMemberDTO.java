package com.ag04.sbss.hackathon.app.converters;

import com.ag04.sbss.hackathon.app.forms.MemberDTO;
import com.ag04.sbss.hackathon.app.forms.MemberSkillDTO;
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
public class MemberToMemberDTO implements Converter<Member, MemberDTO> {

    private final MemberSkillToMemberSkillDTO skillConverter;

    public MemberToMemberDTO(MemberSkillToMemberSkillDTO skillConverter) {
        this.skillConverter = skillConverter;
    }

    @Override
    public MemberDTO convert(Member source) {
        MemberDTO memberDTO = new MemberDTO();

        memberDTO.setName(source.getName());
        memberDTO.setSex(source.getSex());
        memberDTO.setEmail(source.getEmail());
        memberDTO.setMainSkill(source.getMainSkill().getName());
        memberDTO.setStatus(source.getStatus());

        List<MemberSkillDTO> skills = new LinkedList<>();
        for (MemberSkill skill : source.getSkills()){
            skills.add(skillConverter.convert(skill));
        }
        memberDTO.setSkills(skills);

        return memberDTO;
    }
}
