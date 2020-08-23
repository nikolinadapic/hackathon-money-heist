package com.ag04.sbss.hackathon.app.scheduling;

import com.ag04.sbss.hackathon.app.model.Heist;
import com.ag04.sbss.hackathon.app.model.Member;
import com.ag04.sbss.hackathon.app.model.StatusHeist;
import com.ag04.sbss.hackathon.app.repositories.HeistRepository;
import com.ag04.sbss.hackathon.app.services.EmailService;
import com.ag04.sbss.hackathon.app.services.MemberService;

import java.util.Optional;

public class ScheduledStatusChange implements Runnable {
    private String heistName;
    private StatusHeist newStatus;
    private HeistRepository heistRepository;
    private EmailService emailService;
    private MemberService memberService;

    public ScheduledStatusChange(String heistName,
                                 StatusHeist newStatus,
                                 HeistRepository heistRepository,
                                 EmailService emailService,
                                 MemberService memberService){
        this.heistName = heistName;
        this.newStatus = newStatus;
        this.heistRepository = heistRepository;
        this.emailService = emailService;
        this.memberService = memberService;
    }

    @Override
    public void run() {
        Optional<Heist> heistOptional = heistRepository.findByName(heistName);

        if(heistOptional.isPresent()) {
            heistOptional.get().setStatus(newStatus);

            for(Member member : heistOptional.get().getMembers()){
                emailService.sendMessage(
                member.getEmail(),
                        "SECRET", "Hello, " + member.getName() + "! " +
                        "The heist named '"
                        + heistOptional.get().getName() + "' changed its status to '"+ newStatus +"'.");
            }

            if(newStatus == StatusHeist.FINISHED){
                memberService.incrementSkills(heistOptional.get());
            }


            heistRepository.save(heistOptional.get());
        }
    }
}
