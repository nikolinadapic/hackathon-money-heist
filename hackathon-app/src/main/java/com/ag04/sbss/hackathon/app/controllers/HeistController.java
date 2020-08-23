package com.ag04.sbss.hackathon.app.controllers;

import com.ag04.sbss.hackathon.app.converters.HeistFormToHeist;
import com.ag04.sbss.hackathon.app.dto.*;
import com.ag04.sbss.hackathon.app.forms.HeistForm;
import com.ag04.sbss.hackathon.app.forms.RequiredSkillForm;
import com.ag04.sbss.hackathon.app.forms.RequiredSkillListForm;
import com.ag04.sbss.hackathon.app.forms.MemberNamesForm;
import com.ag04.sbss.hackathon.app.model.Heist;
import com.ag04.sbss.hackathon.app.services.HeistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
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

    @PatchMapping("/{heistId}/skills")
    public ResponseEntity<String> updateSkills(@PathVariable("heistId") Long heistId, @RequestBody RequiredSkillListForm skills) {
        heistService.updateHeistSkills(heistId, skills);

        return ResponseEntity.noContent().header("Content-Location", "/heist/" + heistId + "/skills").build();
    }

    @PutMapping("/{heistId}/start")
    public ResponseEntity<String> startHeist(@PathVariable("heistId") Long heistId) {
        heistService.startHeist(heistId);

        return ResponseEntity.ok().header("Location", "/heist/" + heistId + "/status").build();
    }

    @GetMapping("/{heistId}/eligible_members")
    public ResponseEntity<EligibleMembersDTO> getEligibleMembers(@PathVariable("heistId") Long heistId) {
        return new ResponseEntity<>(heistService.getEligibleMembers(heistId), HttpStatus.OK);
    }

    @PutMapping("/{heistId}/members")
    public ResponseEntity<String> setHeistMembers(@PathVariable("heistId") Long heistId, @RequestBody MemberNamesForm memberNames) {
        heistService.addHeistMembers(heistId, memberNames);

        return ResponseEntity.noContent().header("Content-Location", "/heist/" + heistId + "/members").build();
    }

    @GetMapping("/{heistId}")
    public ResponseEntity<HeistDTO> getHeist(@PathVariable("heistId") Long heistId) {
        return new ResponseEntity<>(heistService.findById(heistId), HttpStatus.OK);
    }

    @GetMapping("/{heistId}/members")
    public ResponseEntity<List<HeistMemberDTO>> getHeistMembers(@PathVariable("heistId") Long heistId) {
        return new ResponseEntity<>(heistService.findHeistMembers(heistId), HttpStatus.OK);
    }

    @GetMapping("/{heistId}/skills")
    public ResponseEntity<List<RequiredSkillForm>> getHeistSkills(@PathVariable("heistId") Long heistId) {
        return new ResponseEntity<>(heistService.findHeistSkills(heistId), HttpStatus.OK);
    }

    @GetMapping("/{heistId}/status")
    public ResponseEntity<StatusDTO> getHeistStatus(@PathVariable("heistId") Long heistId) {
        return new ResponseEntity<>(heistService.findHeistStatus(heistId), HttpStatus.OK);
    }

    @GetMapping("/{heistId}/outcome")
    public ResponseEntity<OutcomeDTO> getHeistOutcome(@PathVariable("heistId") Long heistId) {
        return new ResponseEntity<>(heistService.getHeistOutcome(heistId), HttpStatus.OK);
    }
}
