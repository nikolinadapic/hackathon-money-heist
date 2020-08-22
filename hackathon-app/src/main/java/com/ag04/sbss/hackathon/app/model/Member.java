package com.ag04.sbss.hackathon.app.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    //unique
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member")
    private Set<MemberSkill> skills = new HashSet<>();

    @ManyToOne
    private Skill mainSkill;

    @Enumerated(EnumType.STRING)
    private StatusMember status;
}
