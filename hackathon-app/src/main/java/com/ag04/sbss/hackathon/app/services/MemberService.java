package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.forms.MemberDTO;
import com.ag04.sbss.hackathon.app.forms.MemberSkillListDTO;
import com.ag04.sbss.hackathon.app.model.Member;

public interface MemberService {
    Member createMember(MemberDTO newMember);

    void updateMemberSkills(Long memberId, MemberSkillListDTO skills);

    void deleteSkill(Long memberId, String skillName);

    MemberDTO findById(Long id);

    MemberSkillListDTO findMemberSkills(Long memberId);
}
