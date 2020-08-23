package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.converters.MemberFormToMember;
import com.ag04.sbss.hackathon.app.converters.MemberToMemberDTO;
import com.ag04.sbss.hackathon.app.forms.MemberDTO;
import com.ag04.sbss.hackathon.app.forms.MemberSkillForm;
import com.ag04.sbss.hackathon.app.forms.MemberSkillListForm;
import com.ag04.sbss.hackathon.app.model.Member;
import com.ag04.sbss.hackathon.app.model.MemberSkill;
import com.ag04.sbss.hackathon.app.model.Skill;
import com.ag04.sbss.hackathon.app.repositories.MemberRepository;
import com.ag04.sbss.hackathon.app.repositories.MemberSkillRepository;
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
    private final MemberToMemberDTO memberDTOconverter;
    private final MemberSkillService memberSkillService;
    private final MemberSkillRepository skillRepository;


    public MemberServiceImpl(MemberRepository memberRepository, MemberFormToMember memberConverter, MemberToMemberDTO memberDTOconverter, MemberSkillService memberSkillService, MemberSkillRepository skillRepository) {
        this.memberRepository = memberRepository;
        this.memberConverter = memberConverter;
        this.memberDTOconverter = memberDTOconverter;
        this.memberSkillService = memberSkillService;
        this.skillRepository = skillRepository;
    }

    @Override
    public Member createMember(MemberDTO memberToCreate) {
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

    @Override
    public void deleteSkill(Long memberId, String skillName) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (memberOptional.isEmpty()) {
            throw new ResourceNotFoundException("Member with the given ID does not exist.");
        } else {
            Optional<MemberSkill> skillOptional =memberOptional.get().getSkills().stream().filter(memberSkill ->
                memberSkill.getSkill().getName().equals(skillName)
            ).findFirst();
            if(skillOptional.isEmpty()){
                throw  new ResourceNotFoundException("Skill with the given name does not exist.");
            } else {
                MemberSkill skillToDelete = skillOptional.get();
                Member member = memberOptional.get();
                member.getSkills().remove(skillToDelete);
                if(skillName.equals(member.getMainSkill().getName())){
                    member.setMainSkill(null);
                    memberRepository.save(member);
                }
                skillRepository.delete(skillToDelete);
            }
        }
    }

    @Override
    public MemberDTO findById(Long id) {
        Optional<Member> memberOptional = memberRepository.findById(id);

        if(memberOptional.isEmpty()){
            throw new ResourceNotFoundException("Member with the given ID does not exist.");
        }
        return memberDTOconverter.convert(memberOptional.get());
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
