package com.ag04.sbss.hackathon.app.model;

import lombok.Data;
import org.hibernate.annotations.Cascade;

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
    private Date startTime;
    private Date endTime;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "heist")
    private Set<RequiredSkill> skills;

    @Enumerated(EnumType.STRING)
    private StatusHeist status;
}
