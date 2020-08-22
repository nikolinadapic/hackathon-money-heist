package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.dto.EligibleMembersDTO;
import com.ag04.sbss.hackathon.app.dto.HeistDTO;
import com.ag04.sbss.hackathon.app.dto.HeistMemberDTO;
import com.ag04.sbss.hackathon.app.dto.StatusDTO;
import com.ag04.sbss.hackathon.app.forms.MemberNamesForm;
import com.ag04.sbss.hackathon.app.forms.RequiredSkillForm;
import com.ag04.sbss.hackathon.app.forms.RequiredSkillListForm;
import com.ag04.sbss.hackathon.app.model.Heist;

import java.util.List;

public interface HeistService {
    void addHeist(Heist heist);

    void updateHeistSkills(Long id, RequiredSkillListForm requiredSkillListForm);

    void startHeist(Long id);

    EligibleMembersDTO getEligibleMembers(Long id);

    void addHeistMembers(Long id, MemberNamesForm memberNames);

    HeistDTO findById(Long id);

    List<HeistMemberDTO> findHeistMembers(Long heistId);

    List<RequiredSkillForm> findHeistSkills(Long heistId);

    StatusDTO findHeistStatus(Long heistId);
}
