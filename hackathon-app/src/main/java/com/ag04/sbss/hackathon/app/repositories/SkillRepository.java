package com.ag04.sbss.hackathon.app.repositories;

import com.ag04.sbss.hackathon.app.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Vitomir M on 22.8.2020.
 */
public interface SkillRepository extends JpaRepository<Skill, Long> {

    Optional<Skill> findByName(String name);
}
