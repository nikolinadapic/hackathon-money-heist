package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.model.Member;
import com.ag04.sbss.hackathon.app.repositories.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member createMember(Member newMember) {
        if (newMember.getId() == null || findMemberById(newMember.getId()).isEmpty()) {
            return memberRepository.save(newMember);
        } else {
            //todo better exception handling
            throw new RuntimeException();
        }
    }

    private Optional<Member> findMemberById(Long memberId) {
        return memberRepository.findById(memberId);
    }

}
