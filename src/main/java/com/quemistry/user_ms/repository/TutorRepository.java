package com.quemistry.user_ms.repository;

import com.quemistry.user_ms.repository.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

    Optional<Tutor> findTutorByUserEntityAccountId(String userId);
}
