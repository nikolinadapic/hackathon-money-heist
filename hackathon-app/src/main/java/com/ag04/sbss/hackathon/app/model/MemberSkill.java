package com.ag04.sbss.hackathon.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

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

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Member member;
}
