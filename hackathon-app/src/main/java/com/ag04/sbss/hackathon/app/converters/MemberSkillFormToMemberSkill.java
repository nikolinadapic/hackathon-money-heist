package com.ag04.sbss.hackathon.app.converters;

import com.ag04.sbss.hackathon.app.forms.MemberSkillForm;
import com.ag04.sbss.hackathon.app.model.MemberSkill;
import com.ag04.sbss.hackathon.app.model.Skill;
import com.ag04.sbss.hackathon.app.repositories.MemberSkillRepository;
import com.ag04.sbss.hackathon.app.repositories.SkillRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MemberSkillFormToMemberSkill implements Converter<MemberSkillForm, MemberSkill> {

    private SkillRepository skillRepository;
    private MemberSkillRepository memberSkillRepository;

    public MemberSkillFormToMemberSkill(SkillRepository skillRepository, MemberSkillRepository memberSkillRepository ) {
        this.skillRepository = skillRepository;
        this.memberSkillRepository = memberSkillRepository;
    }

    @Override
    public MemberSkill convert(MemberSkillForm source) {
        MemberSkill converted = new MemberSkill();
        Optional<Skill> skillOptional = skillRepository.findByName(source.getName());

        if(skillOptional.isEmpty()){
            //add a skill
            Skill newSkill = skillRepository.save(new Skill(source.getName()));
            converted.setSkill(newSkill);
        } else {
            converted.setSkill(skillOptional.get());
        }
        converted.setLevel(source.getLevel());
        return converted;
    }
}
