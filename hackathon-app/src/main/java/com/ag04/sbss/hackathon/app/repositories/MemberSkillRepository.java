package com.ag04.sbss.hackathon.app.repositories;

import com.ag04.sbss.hackathon.app.model.MemberSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Vitomir M on 22.8.2020.
 */
public interface MemberSkillRepository extends JpaRepository<MemberSkill, Long> {

}
