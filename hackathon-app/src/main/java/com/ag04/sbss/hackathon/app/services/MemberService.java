package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.forms.MemberForm;
import com.ag04.sbss.hackathon.app.forms.MemberSkillListForm;
import com.ag04.sbss.hackathon.app.model.Member;

public interface MemberService {
    Member createMember(MemberForm newMember);

    void updateMemberSkills(Long memberId, MemberSkillListForm skills);

    void deleteSkill(Long memberId, String skillName);
}
