package com.quemistry.user_ms.repository;

import com.quemistry.user_ms.repository.entity.ClassInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassInvitationRepository extends JpaRepository<ClassInvitation, Long> {

    Optional<ClassInvitation> findByCode(String invitationCode);
}
