package com.ag04.sbss.hackathon.app.model;

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

    public void setStartTime(Date startTime) {
        this.startTime = startTime;

        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();

        long milis = startTime.getTime() - System.currentTimeMillis();

        System.out.println("start in " + milis);

        if(milis <= 0) {
            status = StatusHeist.IN_PROGRESS;
        }

        scheduler.schedule(
                new ScheduledStatusChange(this, StatusHeist.IN_PROGRESS),
                new Date(startTime.getTime())
        );
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;

        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();

        long milis = endTime.getTime() - System.currentTimeMillis();

        System.out.println("end in " + milis);

        scheduler.schedule(
                new ScheduledStatusChange(this, StatusHeist.FINISHED),
                new Date(endTime.getTime())
        );
    }
}
