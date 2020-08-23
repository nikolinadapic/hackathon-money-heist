package com.ag04.sbss.hackathon.app.controllers;

import com.ag04.sbss.hackathon.app.forms.MemberDTO;
import com.ag04.sbss.hackathon.app.forms.MemberSkillListDTO;
import com.ag04.sbss.hackathon.app.model.Member;
import com.ag04.sbss.hackathon.app.services.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> getMember(@PathVariable Long id){
        return new ResponseEntity<>(memberService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/{memberId}/skills")
    public ResponseEntity<MemberSkillListDTO> getMemberSkills(@PathVariable Long memberId){
        return new ResponseEntity<>(memberService.findMemberSkills(memberId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody MemberDTO newMember){
        Member created = memberService.createMember(newMember);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{memberId}/skills")
    public ResponseEntity<String> updateSkills(@PathVariable Long memberId, @RequestBody MemberSkillListDTO skills ){
        memberService.updateMemberSkills(memberId, skills);

        return ResponseEntity.noContent().header("Content-Location", "/heist/" + memberId + "/skills").build();
    }

    @DeleteMapping("/{memberId}/skills/{skillName}")
    public ResponseEntity<Object> deleteSkill(@PathVariable Long memberId, @PathVariable String skillName){
        memberService.deleteSkill(memberId,skillName);
        return ResponseEntity.noContent().build();
    }
}
