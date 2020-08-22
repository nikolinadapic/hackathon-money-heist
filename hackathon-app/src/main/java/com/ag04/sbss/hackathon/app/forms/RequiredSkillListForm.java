package com.ag04.sbss.hackathon.app.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RequiredSkillListForm {

    private List<RequiredSkillForm> skills;
}
