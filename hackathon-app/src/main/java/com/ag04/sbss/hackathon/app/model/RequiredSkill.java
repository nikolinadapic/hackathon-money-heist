package com.ag04.sbss.hackathon.app.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

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

    @ManyToMany(mappedBy = "skills")
    private Set<Heist> heists;
}
