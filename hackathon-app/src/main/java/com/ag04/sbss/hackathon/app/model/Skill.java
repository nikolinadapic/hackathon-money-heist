package com.ag04.sbss.hackathon.app.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Skill(String name) {
        this.name = name;
    }
}
