package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.forms.MemberSkillForm;
import com.ag04.sbss.hackathon.app.forms.RequiredSkillForm;
import com.ag04.sbss.hackathon.app.model.MemberSkill;
import com.ag04.sbss.hackathon.app.model.RequiredSkill;

import java.util.List;
import java.util.Set;

/**
 * Created by Vitomir M on 22.8.2020.
 */
public interface MemberSkillService {
    Set<MemberSkill> convertToSkillSet(List<MemberSkillForm> forms);

}
