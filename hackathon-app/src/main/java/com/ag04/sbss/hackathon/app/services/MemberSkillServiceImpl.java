package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.converters.MemberSkillFormToMemberSkill;
import com.ag04.sbss.hackathon.app.forms.MemberSkillForm;
import com.ag04.sbss.hackathon.app.model.MemberSkill;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Vitomir M on 22.8.2020.
 */
@Service
public class MemberSkillServiceImpl implements MemberSkillService {

    private final MemberSkillFormToMemberSkill memberSkillConverter;

    public MemberSkillServiceImpl(MemberSkillFormToMemberSkill memberSkillConverter) {
        this.memberSkillConverter = memberSkillConverter;
    }

    @Override
    public Set<MemberSkill> convertToSkillSet(List<MemberSkillForm> forms) {
        Set<MemberSkill> skills = new HashSet<>();
        for(MemberSkillForm skillForm : forms){
            MemberSkill memberSkill = memberSkillConverter.convert(skillForm);

            for(MemberSkill memSkill : skills){
                if(memSkill.getSkill().getName().equals(memberSkill.getSkill().getName())) {
                    throw new RuntimeException("Cannot add multiple member skills with same name.");
                }
            }
            skills.add(memberSkill);
        }
        return skills;
    }
}
