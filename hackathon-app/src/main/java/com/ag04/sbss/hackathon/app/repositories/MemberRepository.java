package com.ag04.sbss.hackathon.app.repositories;

import com.ag04.sbss.hackathon.app.model.Member;
import com.ag04.sbss.hackathon.app.model.StatusMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByName(String name);

    Optional<Member> findByEmail(String email);

    List<Member> findByStatusIn(List<StatusMember> statusList);
}
