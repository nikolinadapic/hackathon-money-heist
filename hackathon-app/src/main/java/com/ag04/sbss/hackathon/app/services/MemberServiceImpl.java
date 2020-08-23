package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.converters.MemberFormToMember;
import com.ag04.sbss.hackathon.app.converters.MemberToMemberDTO;
import com.ag04.sbss.hackathon.app.converters.MemberToMemberSkillListDTO;
import com.ag04.sbss.hackathon.app.forms.MemberDTO;
import com.ag04.sbss.hackathon.app.forms.MemberSkillDTO;
import com.ag04.sbss.hackathon.app.forms.MemberSkillListDTO;
import com.ag04.sbss.hackathon.app.model.*;
import com.ag04.sbss.hackathon.app.repositories.MemberRepository;
import com.ag04.sbss.hackathon.app.repositories.MemberSkillRepository;
import com.ag04.sbss.hackathon.app.services.exception.RequestDeniedException;
import com.ag04.sbss.hackathon.app.services.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberFormToMember memberConverter;
    private final MemberToMemberDTO memberDTOConverter;
    private final MemberSkillService memberSkillService;
    private final MemberSkillRepository skillRepository;
    private final MemberToMemberSkillListDTO skillListDTOConverter;
    private final EmailService emailService;
    @Value("${levelUpTime}")
    private int LEVEL_UP_TIME;

    public MemberServiceImpl(MemberRepository memberRepository,
                             MemberFormToMember memberConverter,
                             MemberToMemberDTO memberDTOConverter,
                             MemberSkillService memberSkillService,
                             MemberSkillRepository skillRepository,
                             MemberToMemberSkillListDTO skillListDTOConverter,
                             EmailService emailService) {
        this.memberRepository = memberRepository;
        this.memberConverter = memberConverter;
        this.memberDTOConverter = memberDTOConverter;
        this.memberSkillService = memberSkillService;
        this.skillRepository = skillRepository;
        this.skillListDTOConverter = skillListDTOConverter;
        this.emailService = emailService;
    }


    @Override
    public Member createMember(MemberDTO memberToCreate) {
        Optional<Member> memberOptional = memberRepository.findByEmail(memberToCreate.getEmail());

        if(memberOptional.isPresent()) {
            throw new RequestDeniedException("Member with same email already exists.");
        }

        Member newMember = memberConverter.convert(memberToCreate);
        if (newMember != null) {
            emailService.sendMessage(newMember.getEmail(),
                    "SECRET","Hello, "+ newMember.getName() + "! "+
                    "You have been added to our special group. ");
            return memberRepository.save(newMember);

        } else {
            throw new RequestDeniedException("Cannot create a member!");
        }
    }

    @Override
    public void updateMemberSkills(Long memberId, MemberSkillListDTO skills) {
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
    public void incrementSkills(Heist heist){
        //calculate duration
        long multiplicator = ((heist.getEndTime().getTime()-heist.getStartTime().getTime()) / 1000) % LEVEL_UP_TIME;
        for(Member member : heist.getMembers()){
            for(RequiredSkill requiredSkill : heist.getSkills()){
                Optional<MemberSkill> skill =
                        member.getSkills().stream().filter(memberSkill ->
                        memberSkill.getSkill().equals(requiredSkill.getSkill()))
                        .findFirst();
                if(skill.isPresent()){
                    MemberSkill memberSkill= skill.get();

                    for(int i = 1;i<=multiplicator; i++){
                        if(memberSkill.getLevel().length() <10){
                            memberSkill.setLevel(memberSkill.getLevel()+"*");
                        }
                    }
                }

            }
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
        return memberDTOConverter.convert(memberOptional.get());
    }

    @Override
    public MemberSkillListDTO findMemberSkills(Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);

        if(memberOptional.isEmpty()){
            throw new ResourceNotFoundException("Member with the given Id does not exist.");
        }
        return skillListDTOConverter.convert(memberOptional.get());
    }

    private void checkIfMainSkillPresent(List<MemberSkillDTO> skills, String mainSkill, Member member) {
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
