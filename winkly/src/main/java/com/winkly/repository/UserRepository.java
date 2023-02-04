package com.winkly.repository;

import com.winkly.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserName(String username);
    Optional<UserEntity> findByEmail(String email);
    Boolean existsByUserName(String username);
    Boolean existsByEmail(String email);

    @Query(
            value = "UPDATE user_table SET password=:password WHERE user_name=:username",
            nativeQuery = true)
    @Modifying
    @Transactional
    void updatePassword(@Param("password") String password, @Param("username") String username);

    UserEntity findByResetToken(String token);
}
