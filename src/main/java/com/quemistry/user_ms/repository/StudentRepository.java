package com.quemistry.user_ms.repository;

import com.quemistry.user_ms.repository.entity.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findStudentEntityByUserEntityId(Long userEntityId);

    @EntityGraph(value = "student-entity-graph")
    Optional<Student> findStudentByUserEntityAccountId(String accountId);

    Optional<Student> findStudentByUserEntityEmail(String email);

    @Query("SELECT s FROM Student s JOIN s.userEntity u " +
            "JOIN s.classes c " +
            "JOIN c.tutors t " +
            "JOIN t.userEntity u2 " +
            "WHERE (u.email IN :studentEmails " +
            "OR u.accountId in :studentAccountIds) " +
            "AND u2.email = :tutorEmail")
    List<Student> findStudentByEmailOrAccountId(List<String> studentEmails, List<String> studentAccountIds, String tutorEmail);

}
