package com.ag04.sbss.hackathon.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OutcomeDTO {

    private String outcome;

    public OutcomeDTO(String outcome) {
        this.outcome = outcome;
    }
}
