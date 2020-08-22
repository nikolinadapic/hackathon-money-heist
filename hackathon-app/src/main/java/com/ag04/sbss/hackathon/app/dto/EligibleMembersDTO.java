package com.ag04.sbss.hackathon.app.dto;

import com.ag04.sbss.hackathon.app.forms.RequiredSkillForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EligibleMembersDTO {

    private List<RequiredSkillForm> skills;

    private List<HeistMemberDTO> members;
}
