package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.converters.HeistToHeistDTO;
import com.ag04.sbss.hackathon.app.converters.MemberToHeistMemberDTO;
import com.ag04.sbss.hackathon.app.converters.RequiredSkillListFormToRequiredSkillSet;
import com.ag04.sbss.hackathon.app.dto.EligibleMembersDTO;
import com.ag04.sbss.hackathon.app.dto.HeistMemberDTO;
import com.ag04.sbss.hackathon.app.dto.HeistDTO;
import com.ag04.sbss.hackathon.app.forms.MemberNamesForm;
import com.ag04.sbss.hackathon.app.forms.RequiredSkillListForm;
import com.ag04.sbss.hackathon.app.model.*;
import com.ag04.sbss.hackathon.app.repositories.HeistRepository;
import com.ag04.sbss.hackathon.app.repositories.MemberRepository;
import com.ag04.sbss.hackathon.app.services.exception.MethodNotAllowedException;
import com.ag04.sbss.hackathon.app.services.exception.RequestDeniedException;
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
    private final HeistToHeistDTO heistToHeistDTO;

    public HeistServiceImpl(HeistRepository heistRepository, RequiredSkillListFormToRequiredSkillSet requiredSkillListFormToRequiredSkillSet, RequiredSkillService requiredSkillService, MemberRepository memberRepository, MemberToHeistMemberDTO memberToHeistMemberDTO, HeistToHeistDTO heistToHeistDTO) {
        this.heistRepository = heistRepository;
        this.requiredSkillListFormToRequiredSkillSet = requiredSkillListFormToRequiredSkillSet;
        this.requiredSkillService = requiredSkillService;
        this.memberRepository = memberRepository;
        this.memberToHeistMemberDTO = memberToHeistMemberDTO;
        this.heistToHeistDTO = heistToHeistDTO;
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
            if(isEligible(member, heistOptional.get())) {
                heistMemberDTOList.add(memberToHeistMemberDTO.convert(member));
            }
        }
        eligibleMembersDTO.setMembers(heistMemberDTOList);

        return eligibleMembersDTO;
    }

    public void addHeistMembers(Long id, MemberNamesForm memberNames) {
        Optional<Heist> heistOptional = heistRepository.findById(id);

        if (heistOptional.isEmpty()) {
            throw new ResourceNotFoundException("Heist with given ID does not exist.");
        }

        if(!heistOptional.get().getStatus().equals(StatusHeist.PLANNING)) {
            throw new MethodNotAllowedException("Heist status is not PLANNING.");
        }

        Set<Member> members = new HashSet<>();

        for(String memberName : memberNames.getMembers()) {
            Optional<Member> memberOptional = memberRepository.findByName(memberName);

            if(memberOptional.isEmpty()) {
                throw new ResourceNotFoundException("Member with given name does not exist.");
            }

            if(!isEligible(memberOptional.get(), heistOptional.get())) {
                throw new RequestDeniedException("Given member is not eligible.");
            }

            members.add(memberOptional.get());
        }

        heistOptional.get().setMembers(members);
        heistRepository.save(heistOptional.get());
    }

    public HeistDTO findById(Long id) {
        Optional<Heist> heistOptional = heistRepository.findById(id);

        if (heistOptional.isEmpty()) {
            throw new ResourceNotFoundException("Heist with given ID does not exist.");
        }
        return heistToHeistDTO.convert(heistOptional.get());
    }

    boolean isEligible(Member member, Heist heist) {
        for(MemberSkill memberSkill : member.getSkills()) {
            for(RequiredSkill requiredSkill : heist.getSkills()) {
                if (memberSkill.getSkill().equals(requiredSkill.getSkill())
                        && memberSkill.getLevel().contains(requiredSkill.getLevel())
                        && isFree(member, heist)
                ) {

                    return true;
                }
            }
        }

        return false;
    }

    boolean isFree(Member member, Heist heist1) {
        for(Heist heist2 : member.getHeists()) {
            if(heist1.getStartTime().before(heist2.getEndTime()) && heist2.getStartTime().before(heist1.getEndTime())) {
                return false;
            }
        }

        return true;
    }
}
