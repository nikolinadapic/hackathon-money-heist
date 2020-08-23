package com.ag04.sbss.hackathon.app.controllers;

import com.ag04.sbss.hackathon.app.converters.MemberToMemberDTO;
import com.ag04.sbss.hackathon.app.forms.MemberDTO;
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


    @GetMapping("/{id}")
    private ResponseEntity<MemberDTO> getMember(@PathVariable Long id){
        return new ResponseEntity<>(memberService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<Member> createMember(@RequestBody MemberDTO newMember){
        Member created = memberService.createMember(newMember);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{memberId}/skills")
    private ResponseEntity<String> updateSkills(@PathVariable Long memberId, @RequestBody MemberSkillListForm skills ){
        memberService.updateMemberSkills(memberId, skills);

        return ResponseEntity.noContent().header("Content-Location", "/heist/" + memberId + "/skills").build();
    }

    @DeleteMapping("/{memberId}/skills/{skillName}")
    private ResponseEntity<Object> deleteSkill(@PathVariable Long memberId, @PathVariable String skillName){
        memberService.deleteSkill(memberId,skillName);
        return ResponseEntity.noContent().build();
    }
}
