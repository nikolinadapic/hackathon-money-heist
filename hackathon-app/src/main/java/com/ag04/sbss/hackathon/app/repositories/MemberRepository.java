package com.ag04.sbss.hackathon.app.repositories;

import com.ag04.sbss.hackathon.app.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
