package com.ag04.sbss.hackathon.app.services;

import com.ag04.sbss.hackathon.app.forms.RequiredSkillForm;
import com.ag04.sbss.hackathon.app.model.RequiredSkill;

import java.util.List;
import java.util.Set;

public interface RequiredSkillService {
    Set<RequiredSkill> convertToSkillSet(List<RequiredSkillForm> forms);
    List<RequiredSkillForm> convertToSkillFormList(Set<RequiredSkill> skills);
}
