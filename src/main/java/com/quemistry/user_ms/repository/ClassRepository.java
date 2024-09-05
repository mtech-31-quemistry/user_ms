package com.quemistry.user_ms.repository;

import com.quemistry.user_ms.repository.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, Long> {
    Optional<Class> findByCode(String classCode);

    @Query("SELECT c FROM Class c JOIN c.tutors t JOIN t.userEntity u WHERE c.id = :classId AND u.accountId = :tutorId")
    Optional<Class> findByClassIdAndTutorAccountId(@Param("classId") Long classId, @Param("tutorId") String tutorAccountId);
}
