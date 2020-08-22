package com.ag04.sbss.hackathon.app.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name="heist")
public class Heist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private Date startDate;
    private Date endDate;

    @ManyToMany
    @JoinTable(name = "heist_skill",
            joinColumns = @JoinColumn(name = "heist_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<RequiredSkill> skills;

    @Enumerated(EnumType.STRING)
    private StatusHeist status;
}
