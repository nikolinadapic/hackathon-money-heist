package com.ag04.sbss.hackathon.app.repositories;

import com.ag04.sbss.hackathon.app.model.Heist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeistRepository extends JpaRepository<Heist, Long> {

    Optional<Heist> findByName(String name);
}
