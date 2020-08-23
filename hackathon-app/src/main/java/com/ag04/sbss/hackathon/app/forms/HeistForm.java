package com.ag04.sbss.hackathon.app.forms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HeistForm {

    @NotBlank
    private String name;

    @NotBlank
    private String location;

    @NotBlank
    private Date startTime;

    @NotBlank
    private Date endTime;

    @NotNull
    private List<RequiredSkillForm> skills;
}
