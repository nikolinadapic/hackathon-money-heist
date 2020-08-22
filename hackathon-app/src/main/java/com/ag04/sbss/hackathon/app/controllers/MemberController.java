package com.ag04.sbss.hackathon.app.controllers;

import com.ag04.sbss.hackathon.app.forms.MemberForm;
import com.ag04.sbss.hackathon.app.forms.MemberSkillListForm;
import com.ag04.sbss.hackathon.app.model.Member;
import com.ag04.sbss.hackathon.app.services.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    private ResponseEntity<Member> createMember(@RequestBody MemberForm newMember){
        Member created = memberService.createMember(newMember);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{memberId}/skills")
    private ResponseEntity<String> updateSkills(@PathVariable Long memberId, @RequestBody MemberSkillListForm skills ){
        memberService.updateMemberSkills(memberId, skills);

        return ResponseEntity.noContent().header("Content-Location", "/heist/" + memberId + "/skills").build();
    }
}
