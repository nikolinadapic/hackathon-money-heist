package com.ag04.sbss.hackathon.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@Entity
@Table(name="member_skill")
public class MemberSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Skill skill;

    @Pattern(regexp = "^[\\*]{1,10}")
    private String level;

    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    private Set<Member> members;
}
