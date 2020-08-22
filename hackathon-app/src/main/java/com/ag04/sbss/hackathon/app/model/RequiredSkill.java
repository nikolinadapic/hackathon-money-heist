package com.ag04.sbss.hackathon.app.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;
import java.util.logging.Level;

@Data
@Entity
@Table(name="required_skill")
public class RequiredSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;


    @Pattern(regexp = "^[\\*]{1,10}")
    private String level;

    private Integer members;

    @ManyToMany(mappedBy = "skills")
    private Set<Heist> heists;


}
