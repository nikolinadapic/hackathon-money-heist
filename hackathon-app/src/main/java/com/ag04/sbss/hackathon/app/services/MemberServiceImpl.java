package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.converters.MemberFormToMember;
import com.ag04.sbss.hackathon.app.forms.MemberForm;
import com.ag04.sbss.hackathon.app.model.Member;
import com.ag04.sbss.hackathon.app.repositories.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberFormToMember memberConverter;


    public MemberServiceImpl(MemberRepository memberRepository, MemberFormToMember memberConverter) {
        this.memberRepository = memberRepository;
        this.memberConverter = memberConverter;
    }

    @Override
    public Member createMember(MemberForm memberToCreate) {
        Member newMember = memberConverter.convert(memberToCreate);
        if(newMember != null){
            return memberRepository.save(newMember);

        } else {
            //todo better exception handling
            throw new RuntimeException();
        }
    }
}
