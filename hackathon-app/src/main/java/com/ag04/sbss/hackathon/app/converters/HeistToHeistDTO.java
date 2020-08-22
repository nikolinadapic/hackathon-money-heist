package com.ag04.sbss.hackathon.app.converters;

import com.ag04.sbss.hackathon.app.dto.HeistDTO;
import com.ag04.sbss.hackathon.app.forms.RequiredSkillForm;
import com.ag04.sbss.hackathon.app.model.Heist;
import com.ag04.sbss.hackathon.app.model.RequiredSkill;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class HeistToHeistDTO implements Converter<Heist, HeistDTO> {

    private final RequiredSkillToRequiredSkillForm requiredSkillToRequiredSkillForm;

    public HeistToHeistDTO(RequiredSkillToRequiredSkillForm requiredSkillToRequiredSkillForm) {
        this.requiredSkillToRequiredSkillForm = requiredSkillToRequiredSkillForm;
    }

    @Override
    public HeistDTO convert(Heist source) {
        HeistDTO heistDTO = new HeistDTO();

        heistDTO.setName(source.getName());
        heistDTO.setLocation(source.getLocation());
        heistDTO.setStartTime(source.getStartTime());
        heistDTO.setEndTime(source.getEndTime());

        List<RequiredSkillForm> requiredSkillFormList = new LinkedList<>();
        for(RequiredSkill skill : source.getSkills()) {
            requiredSkillFormList.add(requiredSkillToRequiredSkillForm.convert(skill));
        }

        heistDTO.setSkills(requiredSkillFormList);
        heistDTO.setStatus(source.getStatus());

        return heistDTO;
    }
}
