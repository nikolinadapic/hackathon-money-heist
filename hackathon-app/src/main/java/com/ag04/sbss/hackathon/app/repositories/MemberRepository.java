package com.ag04.sbss.hackathon.app.repositories;

import com.ag04.sbss.hackathon.app.forms.RequiredSkillForm;
import com.ag04.sbss.hackathon.app.model.Member;
import com.ag04.sbss.hackathon.app.model.RequiredSkill;
import com.ag04.sbss.hackathon.app.model.StatusMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByName(String name);

    Optional<Member> findByEmail(String email);

/*
    @Query(value = "select distinct m from Member m join m.status s where s in (:status)", nativeQuery = true)
    List<Member> findByStatus(@Param("status") List<StatusMember> status);
*/

    List<Member> findByStatusIn(List<StatusMember> statusList);
}
