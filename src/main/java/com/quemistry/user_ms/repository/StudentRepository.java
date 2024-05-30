package com.quemistry.user_ms.repository;

import com.quemistry.user_ms.repository.model.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

    StudentEntity findStudentEntityByUserEntityId(Long userEntityId);
}
