package com.quemistry.user_ms.repository;

import com.quemistry.user_ms.repository.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findStudentEntityByUserEntityId(Long userEntityId);

    Optional<Student> findStudentEntityByUserEntityAccountId(String accountId);
}
