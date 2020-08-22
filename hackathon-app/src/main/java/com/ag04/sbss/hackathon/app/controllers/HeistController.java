package com.ag04.sbss.hackathon.app.controllers;

import com.ag04.sbss.hackathon.app.converters.HeistFormToHeist;
import com.ag04.sbss.hackathon.app.forms.HeistForm;
import com.ag04.sbss.hackathon.app.model.Heist;
import com.ag04.sbss.hackathon.app.services.HeistService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/heist")
public class HeistController {

    private final HeistService heistService;
    private final HeistFormToHeist heistFormToHeist;

    public HeistController(HeistService heistService, HeistFormToHeist heistFormToHeist) {
        this.heistService = heistService;
        this.heistFormToHeist = heistFormToHeist;
    }

    @PostMapping()
    public ResponseEntity<String> addHeist(@RequestBody HeistForm heistForm) {
        Heist heist = heistFormToHeist.convert(heistForm);
        heistService.addHeist(heist);

        URI location = URI.create(String.format("/heist/%s", heist.getId()));

        return ResponseEntity.created(location).build();
    }
}
