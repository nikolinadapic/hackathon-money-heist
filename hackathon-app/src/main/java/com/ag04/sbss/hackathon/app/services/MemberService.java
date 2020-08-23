package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.forms.MemberDTO;
import com.ag04.sbss.hackathon.app.forms.MemberSkillListForm;
import com.ag04.sbss.hackathon.app.model.Member;

public interface MemberService {
    Member createMember(MemberDTO newMember);

    void updateMemberSkills(Long memberId, MemberSkillListForm skills);

    void deleteSkill(Long memberId, String skillName);

    MemberDTO findById(Long id);
}
