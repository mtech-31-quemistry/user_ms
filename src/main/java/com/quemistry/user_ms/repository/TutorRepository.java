package com.quemistry.user_ms.repository;

import com.quemistry.user_ms.repository.entity.Tutor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

    @EntityGraph(value = "tutor-entity-graph")
    Optional<Tutor> findTutorByUserEntityAccountId(String accountId);
}
