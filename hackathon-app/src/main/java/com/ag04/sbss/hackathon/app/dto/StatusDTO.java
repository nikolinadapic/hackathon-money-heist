package com.ag04.sbss.hackathon.app.dto;

import com.ag04.sbss.hackathon.app.model.StatusHeist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatusDTO {

    private StatusHeist status;

    public StatusDTO(StatusHeist status) {
        this.status = status;
    }
}
