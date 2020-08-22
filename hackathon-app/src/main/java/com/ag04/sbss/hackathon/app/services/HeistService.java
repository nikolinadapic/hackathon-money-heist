package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.dto.EligibleMembersDTO;
import com.ag04.sbss.hackathon.app.forms.MemberNamesForm;
import com.ag04.sbss.hackathon.app.forms.RequiredSkillListForm;
import com.ag04.sbss.hackathon.app.model.Heist;

public interface HeistService {
    public void addHeist(Heist heist);

    public void updateHeistSkills(Long id, RequiredSkillListForm requiredSkillListForm);

    public void startHeist(Long id);

    public EligibleMembersDTO getEligibleMembers(Long id);

    public void addHeistMembers(Long id, MemberNamesForm memberNames);
}
