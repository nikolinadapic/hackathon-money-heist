package com.ag04.sbss.hackathon.app.model;

import com.ag04.sbss.hackathon.app.scheduling.ScheduledStatusChange;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
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

    @ManyToMany
    @JoinTable(name = "heist_member",
            joinColumns = @JoinColumn(name = "heist_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private Set<Member> members;

    public void setSkills(Set<RequiredSkill> skills) {
        this.skills = skills;

        for(RequiredSkill skill : skills) {
            skill.setHeist(this);
        }
    }
}
