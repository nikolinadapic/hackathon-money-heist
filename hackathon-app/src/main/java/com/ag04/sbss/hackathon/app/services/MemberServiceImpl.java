package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.converters.MemberFormToMember;
import com.ag04.sbss.hackathon.app.forms.MemberForm;
import com.ag04.sbss.hackathon.app.forms.MemberSkillForm;
import com.ag04.sbss.hackathon.app.forms.MemberSkillListForm;
import com.ag04.sbss.hackathon.app.model.Member;
import com.ag04.sbss.hackathon.app.model.MemberSkill;
import com.ag04.sbss.hackathon.app.model.Skill;
import com.ag04.sbss.hackathon.app.repositories.MemberRepository;
import com.ag04.sbss.hackathon.app.services.exception.RequestDeniedException;
import com.ag04.sbss.hackathon.app.services.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberFormToMember memberConverter;
    private final MemberSkillService memberSkillService;


    public MemberServiceImpl(MemberRepository memberRepository, MemberFormToMember memberConverter, MemberSkillService memberSkillService) {
        this.memberRepository = memberRepository;
        this.memberConverter = memberConverter;
        this.memberSkillService = memberSkillService;
    }

    @Override
    public Member createMember(MemberForm memberToCreate) {
        Member newMember = memberConverter.convert(memberToCreate);
        if (newMember != null) {
            return memberRepository.save(newMember);

        } else {
            throw new RequestDeniedException("Cannot create a member!");
        }
    }

    @Override
    public void updateMemberSkills(Long memberId, MemberSkillListForm skills) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (memberOptional.isEmpty()) {
            throw new ResourceNotFoundException("Member with the given ID does not exist.");
        } else {
            Member member = memberOptional.get();
            //main skill check
            if (skills.getSkills() != null && skills.getMainSkill() == null) {
                member.setSkills(mergeSkillSets(memberSkillService.convertToSkillSet(skills.getSkills()),
                        member.getSkills()));
            } else if (skills.getSkills() == null && skills.getMainSkill() != null) {
                checkIfMainSkillPresent(null, skills.getMainSkill(), member);
            } else if (skills.getSkills() != null && skills.getMainSkill() != null) {
                checkIfMainSkillPresent(skills.getSkills(), skills.getMainSkill(), member);
            }
            memberRepository.save(member);
        }
    }

    private void checkIfMainSkillPresent(List<MemberSkillForm> skills, String mainSkill, Member member) {
        Set<MemberSkill> mergedSkills;
        if (skills != null && !skills.isEmpty()) {
            mergedSkills = mergeSkillSets(memberSkillService.convertToSkillSet(skills),
                    member.getSkills());
            member.setSkills(mergedSkills);
        } else {
            mergedSkills = mergeSkillSets(null,
                    member.getSkills());
            member.setSkills(mergedSkills);
        }
        Optional<Skill> skillOptional =
                mergedSkills.stream()
                        .filter(memberSkill ->
                                memberSkill.getSkill().getName()
                                        .equals(mainSkill))
                        .findFirst().map(MemberSkill::getSkill);

        if (skillOptional.isPresent()) {
            member.setMainSkill(skillOptional.get());
        } else {
            throw new RequestDeniedException("None of the skills in the list match the main skill");
        }
    }

    private Set<MemberSkill> mergeSkillSets(Set<MemberSkill> updatedSkills, Set<MemberSkill> originalSkills) {
        if (updatedSkills == null || updatedSkills.isEmpty()) {
            return originalSkills;
        }
        Set<MemberSkill> mergedSkills = new HashSet<>(updatedSkills);
        mergedSkills.addAll(originalSkills);
        return mergedSkills;
    }
}
