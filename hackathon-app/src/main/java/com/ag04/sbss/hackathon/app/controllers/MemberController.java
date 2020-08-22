package com.ag04.sbss.hackathon.app.controllers;

import com.ag04.sbss.hackathon.app.model.Member;
import com.ag04.sbss.hackathon.app.services.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    private ResponseEntity<Member> createMember(@RequestBody Member newMember){
        Member created = memberService.createMember(newMember);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
