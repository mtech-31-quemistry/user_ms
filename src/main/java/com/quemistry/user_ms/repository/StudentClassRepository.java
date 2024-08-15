package com.quemistry.user_ms.repository;

import com.quemistry.user_ms.repository.entity.StudentClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentClassRepository extends JpaRepository<StudentClass, StudentClass.StudentClassKey> {
}
