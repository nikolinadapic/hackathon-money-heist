package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.forms.MemberSkillDTO;
import com.ag04.sbss.hackathon.app.model.MemberSkill;

import java.util.List;
import java.util.Set;

/**
 * Created by Vitomir M on 22.8.2020.
 */
public interface MemberSkillService {
    Set<MemberSkill> convertToSkillSet(List<MemberSkillDTO> forms);

}
