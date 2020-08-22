package com.ag04.sbss.hackathon.app.converter;


import com.ag04.sbss.hackathon.app.forms.MemberForm;
import com.ag04.sbss.hackathon.app.model.Member;
import com.ag04.sbss.hackathon.app.model.MemberSkill;
import com.ag04.sbss.hackathon.app.model.Skill;
import com.ag04.sbss.hackathon.app.repositories.MemberRepository;
import com.ag04.sbss.hackathon.app.repositories.SkillRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by Vitomir M on 22.8.2020.
 */
@Component
public class MemberFormToMember implements Converter<MemberForm, Member> {

    private MemberRepository memberRepository;
    private SkillRepository skillRepository;
    private MemberSkillFormToMemberSkill skillConverter;

    public MemberFormToMember(MemberRepository memberRepository,
                              MemberSkillFormToMemberSkill skillConverter,
                              SkillRepository skillRepository) {
        this.memberRepository = memberRepository;
        this.skillConverter = skillConverter;
        this.skillRepository = skillRepository;
    }

    @Override
    public Member convert(MemberForm source) {
        Member converted = new Member();
        //check for the existing name
        Optional<Member> memberByNameOptional = memberRepository.findByName(source.getName());
        Optional<Member> memberByEmailOptional = memberRepository.findByEmail(source.getEmail());
        if(memberByNameOptional.isPresent() || memberByEmailOptional.isPresent()){
            throw new RuntimeException("The member already exists");
        } else{
            converted.setName(source.getName());
            converted.setEmail(source.getEmail());
            converted.setSex(source.getSex());
            converted.setStatus(source.getStatus());
            //convert skill forms to skills
            source.getSkills().forEach(
                    //fetch the one with the id!!!
                    memberSkillForm ->  converted.getSkills().add(skillConverter.convert(memberSkillForm))
            );
            //check if the main skill exists in the skills set
            if(source.getMainSkill()!= null){
                String mainSkillName = source.getMainSkill();
                Optional<Skill> skillOptional =
                        converted.getSkills().stream()
                        .filter(memberSkill ->
                                memberSkill.getSkill().getName()
                                        .equals(mainSkillName))
                        .findFirst().map(MemberSkill::getSkill);

                if(skillOptional.isPresent()){
                    converted.setMainSkill(skillOptional.get());
                } else{
                    throw new RuntimeException("None of the skills in the list match the main skill");
                }
            }
            return converted;
        }
    }
}
