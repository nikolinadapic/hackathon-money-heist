package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.converters.HeistToHeistDTO;
import com.ag04.sbss.hackathon.app.converters.MemberToHeistMemberDTO;
import com.ag04.sbss.hackathon.app.converters.RequiredSkillListFormToRequiredSkillSet;
import com.ag04.sbss.hackathon.app.converters.RequiredSkillToRequiredSkillForm;
import com.ag04.sbss.hackathon.app.dto.*;
import com.ag04.sbss.hackathon.app.forms.MemberNamesForm;
import com.ag04.sbss.hackathon.app.forms.RequiredSkillForm;
import com.ag04.sbss.hackathon.app.forms.RequiredSkillListForm;
import com.ag04.sbss.hackathon.app.model.*;
import com.ag04.sbss.hackathon.app.repositories.HeistRepository;
import com.ag04.sbss.hackathon.app.repositories.MemberRepository;
import com.ag04.sbss.hackathon.app.scheduling.ScheduledStatusChange;
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
    private final RequiredSkillToRequiredSkillForm requiredSkillToRequiredSkillForm;
    private final EmailService emailService;
    private final SchedulingService schedulingService;
    private final MemberService memberService;


    public HeistServiceImpl(HeistRepository heistRepository, RequiredSkillListFormToRequiredSkillSet requiredSkillListFormToRequiredSkillSet, RequiredSkillService requiredSkillService, MemberRepository memberRepository, MemberToHeistMemberDTO memberToHeistMemberDTO, HeistToHeistDTO heistToHeistDTO, RequiredSkillToRequiredSkillForm requiredSkillToRequiredSkillForm, EmailService emailService, SchedulingService schedulingService, MemberService memberService) {
        this.heistRepository = heistRepository;
        this.requiredSkillListFormToRequiredSkillSet = requiredSkillListFormToRequiredSkillSet;
        this.requiredSkillService = requiredSkillService;
        this.memberRepository = memberRepository;
        this.memberToHeistMemberDTO = memberToHeistMemberDTO;
        this.heistToHeistDTO = heistToHeistDTO;
        this.requiredSkillToRequiredSkillForm = requiredSkillToRequiredSkillForm;
        this.emailService = emailService;
        this.schedulingService = schedulingService;
        this.memberService = memberService;
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

        if(heist.getStartTime().getTime() - System.currentTimeMillis() <= 0) {
            heist.setStatus(StatusHeist.IN_PROGRESS);
            sendEmail(heist);

        } else {
            schedulingService.getScheduler().schedule(
                    new ScheduledStatusChange(heist.getName(), StatusHeist.IN_PROGRESS, heistRepository,emailService,memberService),
                    new Date(heist.getStartTime().getTime())
            );
        }

        schedulingService.getScheduler().schedule(
                new ScheduledStatusChange(heist.getName(), StatusHeist.FINISHED, heistRepository,emailService,memberService),
                new Date(heist.getEndTime().getTime())
        );

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
        sendEmail(heistOptional.get());

        heistRepository.save(heistOptional.get());
    }

    @Override
    public EligibleMembersDTO getEligibleMembers(Long id) {
        Optional<Heist> heistOptional = heistRepository.findById(id);

        if (heistOptional.isEmpty()) {
            throw new ResourceNotFoundException("Heist with given ID does not exist.");
        }

        if(!heistOptional.get().getStatus().equals(StatusHeist.PLANNING)) {
            throw new MethodNotAllowedException("Heist is already planned.");
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

            if(!canGo(memberOptional.get(), heistOptional.get())) {
                throw new RequestDeniedException("Given member doesn't have any of required skills.");
            }

            members.add(memberOptional.get());
        }

        heistOptional.get().setMembers(members);
        heistOptional.get().setStatus(StatusHeist.READY);

        heistRepository.save(heistOptional.get());
    }

    public HeistDTO findById(Long id) {
        Optional<Heist> heistOptional = heistRepository.findById(id);

        if (heistOptional.isEmpty()) {
            throw new ResourceNotFoundException("Heist with given ID does not exist.");
        }

        return heistToHeistDTO.convert(heistOptional.get());
    }

    @Override
    public List<HeistMemberDTO> findHeistMembers(Long heistId) {
        Optional<Heist> heistOptional = heistRepository.findById(heistId);

        if (heistOptional.isEmpty()) {
            throw new ResourceNotFoundException("Heist with given ID does not exist.");
        }

        if(heistOptional.get().getStatus().equals(StatusHeist.PLANNING)) {
            throw new MethodNotAllowedException("Heist status is PLANNING.");
        }

        List<HeistMemberDTO> heistMemberDTOList = new LinkedList<>();

        for(Member member : heistOptional.get().getMembers()) {
            heistMemberDTOList.add(memberToHeistMemberDTO.convert(member));
        }

        return heistMemberDTOList;
    }

    @Override
    public List<RequiredSkillForm> findHeistSkills(Long heistId) {
        Optional<Heist> heistOptional = heistRepository.findById(heistId);

        if (heistOptional.isEmpty()) {
            throw new ResourceNotFoundException("Heist with given ID does not exist.");
        }

        List<RequiredSkillForm> requiredSkillForms = new LinkedList<>();

        for(RequiredSkill skill : heistOptional.get().getSkills()) {
            requiredSkillForms.add(requiredSkillToRequiredSkillForm.convert(skill));
        }

        return requiredSkillForms;
    }

    @Override
    public StatusDTO findHeistStatus(Long heistId) {
        Optional<Heist> heistOptional = heistRepository.findById(heistId);

        if (heistOptional.isEmpty()) {
            throw new ResourceNotFoundException("Heist with given ID does not exist.");
        }

        return new StatusDTO(heistOptional.get().getStatus());
    }

    @Override
    public OutcomeDTO getHeistOutcome(Long heistId) {
        Optional<Heist> heistOptional = heistRepository.findById(heistId);

        if (heistOptional.isEmpty()) {
            throw new ResourceNotFoundException("Heist with given ID does not exist.");
        }

        if(!heistOptional.get().getStatus().equals(StatusHeist.FINISHED)) {
            throw new MethodNotAllowedException("Heist status is not FINISHED.");
        }

        System.out.println(heistOptional.get().getMembers());
        return new OutcomeDTO(calculateOutcome(heistOptional.get()));
    }

    boolean canGo(Member member, Heist heist) {
        for(MemberSkill memberSkill : member.getSkills()) {
            for(RequiredSkill requiredSkill : heist.getSkills()) {
                if (memberSkill.getSkill().equals(requiredSkill.getSkill())
                        && isFree(member, heist)
                ) {

                    return true;
                }
            }
        }

        return false;
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

    String calculateOutcome(Heist heist) {
        Set<Member> availableMembers = new HashSet<>();
        availableMembers.addAll(heist.getMembers());

        System.out.println(heist.getMembers());
        System.out.println(availableMembers);

        int numMembers = 0;
        int requiredNumMembers = 0;

        TreeMap<RequiredSkill, Integer> mapping = new TreeMap<>(new Comparator<RequiredSkill>() {
            public int compare(RequiredSkill lhs, RequiredSkill rhs) {
                if (lhs.getLevel().contains(rhs.getLevel())) {
                    return lhs.getLevel().equals(rhs.getLevel()) ? 0 : -1;
                }

                return 1;
            }
        });

        for(RequiredSkill skill : heist.getSkills()) {
            mapping.put(skill, 0);
            requiredNumMembers = requiredNumMembers + skill.getMembers();
        }

        for(Map.Entry<RequiredSkill, Integer> entry : mapping.entrySet()) {
            System.out.println(entry.getKey().getSkill().getName());

            List<Member> eligibleMembers = new LinkedList<>();

            for(Member member : availableMembers) {
                System.out.println(member.getName());
                for(MemberSkill skill : member.getSkills()) {
                    System.out.println("got " + skill.getSkill() + " " + skill.getLevel());
                    System.out.println("required " + entry.getKey().getSkill() + "  " + entry.getKey().getLevel());
                    if(skill.getSkill().equals(entry.getKey().getSkill())
                            && skill.getLevel().contains(entry.getKey().getLevel())) {
                        eligibleMembers.add(member);
                    }
                }
            }

            Comparator<Member> byRequiredSkillLevel = new Comparator<Member>() {
                public int compare(Member lhs, Member rhs) {
                    int lhsLevel = 0;
                    for(MemberSkill skill : lhs.getSkills()) {
                        if(skill.getSkill().equals(entry.getKey().getSkill())) {
                            lhsLevel = skill.getLevel().length();
                        }
                    }

                    int rhsLevel  = 0;
                    for(MemberSkill skill : rhs.getSkills()) {
                        if(skill.getSkill().equals(entry.getKey().getSkill())) {
                            rhsLevel = skill.getLevel().length();
                        }
                    }


                    if (lhsLevel > rhsLevel) {
                        return lhsLevel == rhsLevel ? 0 : -1;
                    }

                    return 1;
                }
            };

            Collections.sort(eligibleMembers, byRequiredSkillLevel);

            System.out.println(eligibleMembers);

            for(int i = 0; i < entry.getKey().getMembers(); ++i) {
                if(i < eligibleMembers.size()) {
                    entry.setValue(entry.getValue() + 1);
                    availableMembers.remove(eligibleMembers.get(i));
                    ++numMembers;
                }
            }
        }

        System.out.println(mapping);

        double res = (double) numMembers / requiredNumMembers;

        System.out.println("numMembers " + numMembers);
        System.out.println("requiredNumMembers " + requiredNumMembers);
        System.out.println(res);
        if(res < 0.75) {
            if(res < 0.5) {
                changeMemberStatus(1, false, heist);
                return "FAILED";
            } else {
                if(Math.random() < 0.5) {
                    changeMemberStatus((double) 2 / 3, false, heist);
                    return "FAILED";
                }

                changeMemberStatus((double) 1 / 3, false, heist);
                return "SUCCEEDED";
            }
        }

        if(numMembers == requiredNumMembers) {
            return "SUCCEEDED";
        }

        changeMemberStatus((double) 1 / 3, true, heist);
        return "SUCCEEDED";
    }

    private void changeMemberStatus(double howMany, boolean onlyIncarcerated, Heist heist) {
        int num = (int) Math.ceil(howMany * heist.getMembers().size());

        System.out.println(howMany);
        System.out.println(heist.getMembers().size());
        System.out.println("to change status " + num);

        int i = 0;
        for(Member member : heist.getMembers()) {
            System.out.println(member);
            if(++i > num) {
                break;
            }

            if(onlyIncarcerated) {
                member.setStatus(StatusMember.INCARCERATED);
            } else {
                if(Math.random() < 0.5) {
                    member.setStatus(StatusMember.INCARCERATED);
                } else {
                    member.setStatus(StatusMember.EXPERIED);
                }
            }

            memberRepository.save(member);
        }
    }

    private void sendEmail(Heist heist){
        for(Member heistMember : heist.getMembers() ) {
            emailService.sendMessage(heistMember.getEmail(),
                    "SECRET", "Hello, " + heistMember.getName() + "! " +
                            "You have been confirmed to participate in a heist named '"
                            + heist.getName() + "'.");
        }
    }
}
