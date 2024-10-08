package com.quemistry.user_ms.repository;

import com.quemistry.user_ms.repository.entity.ClassInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ClassInvitationRepository extends JpaRepository<ClassInvitation, Long> {

    Optional<ClassInvitation> findByCode(String invitationCode);

    @Query("SELECT i FROM ClassInvitation i JOIN i.classEntity c WHERE c.id IN :classId")
    List<ClassInvitation> findByClassId(Long classId);

    @Transactional
    @Modifying
    void deleteByUserEmailAndClassEntityId(String userEmail, Long classId);

    @Transactional
    @Modifying
    int deleteByUserEmailInAndClassEntityId(List<String> userEmails, Long classId);
}
