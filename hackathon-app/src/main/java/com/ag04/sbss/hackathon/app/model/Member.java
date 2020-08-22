package com.ag04.sbss.hackathon.app.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    //unique
    private String email;

    @ManyToMany
    @JoinTable(name = "member_skill",
        joinColumns = @JoinColumn(name = "member_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<MemberSkill> skills;

    @ManyToOne
    private MemberSkill mainSkill;

    @Enumerated(EnumType.STRING)
    private StatusMember status;
}
