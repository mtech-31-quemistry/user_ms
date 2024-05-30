package com.quemistry.user_ms.repository;

import com.quemistry.user_ms.repository.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findUserEntityByAccountId(String accountId);
}
