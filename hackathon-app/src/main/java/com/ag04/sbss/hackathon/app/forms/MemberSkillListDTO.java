package com.ag04.sbss.hackathon.app.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by Vitomir M on 22.8.2020.
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberSkillListDTO {

    private List<MemberSkillDTO> skills;

    private String mainSkill;

}
