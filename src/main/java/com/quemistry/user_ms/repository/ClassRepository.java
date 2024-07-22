package com.quemistry.user_ms.repository;

import com.quemistry.user_ms.repository.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, Long> {
    Optional<Class> findClassByCode(String classCode);
}
