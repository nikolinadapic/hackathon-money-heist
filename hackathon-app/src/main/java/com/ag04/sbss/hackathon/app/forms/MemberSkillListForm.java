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
public class MemberSkillListForm {

    private List<MemberSkillForm> skills;

    private String mainSkill;

}
