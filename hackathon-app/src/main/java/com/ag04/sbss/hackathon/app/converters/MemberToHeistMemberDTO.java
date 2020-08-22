package com.ag04.sbss.hackathon.app.converters;

import com.ag04.sbss.hackathon.app.dto.HeistMemberDTO;
import com.ag04.sbss.hackathon.app.forms.MemberSkillForm;
import com.ag04.sbss.hackathon.app.model.Member;
import com.ag04.sbss.hackathon.app.model.MemberSkill;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MemberToHeistMemberDTO implements Converter<Member, HeistMemberDTO> {

    private final MemberSkillToMemberSkillForm memberSkillToMemberSkillForm;

    public MemberToHeistMemberDTO(MemberSkillToMemberSkillForm memberSkillToMemberSkillForm) {
        this.memberSkillToMemberSkillForm = memberSkillToMemberSkillForm;
    }

    @Override
    public HeistMemberDTO convert(Member source) {
        HeistMemberDTO heistMemberDTO = new HeistMemberDTO();

        heistMemberDTO.setName(source.getName());

        List<MemberSkillForm> skillFormList = new ArrayList<>();

        for(MemberSkill skill : source.getSkills()) {
            skillFormList.add(memberSkillToMemberSkillForm.convert(skill));
        }

        heistMemberDTO.setSkills(skillFormList);

        return heistMemberDTO;
    }
}
