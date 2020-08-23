package com.ag04.sbss.hackathon.app.dto;

import com.ag04.sbss.hackathon.app.forms.RequiredSkillForm;
import com.ag04.sbss.hackathon.app.model.StatusHeist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HeistDTO {

    private String name;
    private String location;
    private Date startTime;
    private Date endTime;
    private List<RequiredSkillForm> skills;
    private StatusHeist status;
}
