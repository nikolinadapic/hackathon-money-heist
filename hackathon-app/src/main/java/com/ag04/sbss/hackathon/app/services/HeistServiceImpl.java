package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.converters.MemberToHeistMemberDTO;
import com.ag04.sbss.hackathon.app.converters.RequiredSkillListFormToRequiredSkillSet;
import com.ag04.sbss.hackathon.app.dto.EligibleMembersDTO;
import com.ag04.sbss.hackathon.app.dto.HeistMemberDTO;
import com.ag04.sbss.hackathon.app.forms.MemberSkillForm;
import com.ag04.sbss.hackathon.app.forms.RequiredSkillForm;
import com.ag04.sbss.hackathon.app.forms.RequiredSkillListForm;
import com.ag04.sbss.hackathon.app.model.*;
import com.ag04.sbss.hackathon.app.repositories.HeistRepository;
import com.ag04.sbss.hackathon.app.repositories.MemberRepository;
import com.ag04.sbss.hackathon.app.services.exception.MethodNotAllowedException;
import com.ag04.sbss.hackathon.app.services.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HeistServiceImpl implements HeistService {

    private final HeistRepository heistRepository;
    private final RequiredSkillListFormToRequiredSkillSet requiredSkillListFormToRequiredSkillSet;
    private final RequiredSkillService requiredSkillService;
    private final MemberRepository memberRepository;
    private final MemberToHeistMemberDTO memberToHeistMemberDTO;

    public HeistServiceImpl(HeistRepository heistRepository, RequiredSkillListFormToRequiredSkillSet requiredSkillListFormToRequiredSkillSet, RequiredSkillService requiredSkillService, MemberRepository memberRepository, MemberToHeistMemberDTO memberToHeistMemberDTO) {
        this.heistRepository = heistRepository;
        this.requiredSkillListFormToRequiredSkillSet = requiredSkillListFormToRequiredSkillSet;
        this.requiredSkillService = requiredSkillService;
        this.memberRepository = memberRepository;
        this.memberToHeistMemberDTO = memberToHeistMemberDTO;
    }

    @Override
    public void addHeist(Heist heist) {
        Optional<Heist> heistOptional = heistRepository.findByName(heist.getName());

        if(heistOptional.isPresent()) {
            throw new MethodNotAllowedException("Heist with name " + heist.getName() + " already exists");
        }

        if(heist.getStartTime().after(heist.getEndTime()) || heist.getEndTime().before(new Date(System.currentTimeMillis()))) {
            throw new MethodNotAllowedException("Invalid date.");
        }

        heistRepository.save(heist);
    }

    @Override
    public void updateHeistSkills(Long id, RequiredSkillListForm requiredSkillListForm) {
        Optional<Heist> heistOptional = heistRepository.findById(id);

        if(heistOptional.isEmpty()) {
            throw new ResourceNotFoundException("Heist with given ID does not exist.");
        }

        if(heistOptional.get().getStatus().equals(StatusHeist.IN_PROGRESS) || heistOptional.get().getStatus().equals(StatusHeist.FINISHED)) {
            throw new MethodNotAllowedException("Heist has already started.");
        }

        heistOptional.get().setSkills(requiredSkillListFormToRequiredSkillSet.convert(requiredSkillListForm));

        heistRepository.save(heistOptional.get());
    }

    @Override
    public void startHeist(Long id) {
        Optional<Heist> heistOptional = heistRepository.findById(id);

        if (heistOptional.isEmpty()) {
            throw new ResourceNotFoundException("Heist with given ID does not exist.");
        }

        if (!heistOptional.get().getStatus().equals(StatusHeist.READY)) {
            throw new MethodNotAllowedException("Heist is not ready.");
        }

        heistOptional.get().setStatus(StatusHeist.IN_PROGRESS);

        heistRepository.save(heistOptional.get());
    }

    @Override
    public EligibleMembersDTO getEligibleMembers(Long id) {
        Optional<Heist> heistOptional = heistRepository.findById(id);

        if (heistOptional.isEmpty()) {
            throw new ResourceNotFoundException("Heist with given ID does not exist.");
        }

        EligibleMembersDTO eligibleMembersDTO = new EligibleMembersDTO();
        eligibleMembersDTO.setSkills(requiredSkillService.convertToSkillFormList(heistOptional.get().getSkills()));

        List<HeistMemberDTO> heistMemberDTOList = new LinkedList<>();
        List<Member> activeMembers = memberRepository.findByStatusIn(List.of(StatusMember.AVAILABLE, StatusMember.RETIRED));

        for(Member member : activeMembers) {
            outerloop: for(MemberSkill memberSkill : member.getSkills()) {
                for(RequiredSkill requiredSkill : heistOptional.get().getSkills()) {
                    if(memberSkill.getSkill().equals(requiredSkill.getSkill())
                            && memberSkill.getLevel().contains(requiredSkill.getLevel())) {

                        heistMemberDTOList.add(memberToHeistMemberDTO.convert(member));
                        break outerloop;
                    }
                }
            }
        }
        eligibleMembersDTO.setMembers(heistMemberDTOList);

        return eligibleMembersDTO;
    }
}
