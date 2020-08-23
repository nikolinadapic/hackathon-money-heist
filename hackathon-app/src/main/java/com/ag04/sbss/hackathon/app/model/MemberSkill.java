package com.ag04.sbss.hackathon.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
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

    @Override
    public boolean equals(Object obj) {

        if(obj ==this){
            return true;
        }

        if(!(obj instanceof MemberSkill)){
            return false;
        }

        return (this.getSkill().getName().equals(((MemberSkill) obj).getSkill().getName()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(skill.getName());
    }
}
