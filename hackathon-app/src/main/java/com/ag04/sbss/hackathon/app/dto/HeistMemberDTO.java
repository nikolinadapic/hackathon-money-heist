package com.ag04.sbss.hackathon.app.dto;

import com.ag04.sbss.hackathon.app.forms.MemberSkillDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HeistMemberDTO {

    private String name;

    private List<MemberSkillDTO> skills;
}
