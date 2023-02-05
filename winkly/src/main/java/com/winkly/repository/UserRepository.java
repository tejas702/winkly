package com.winkly.repository;

import com.winkly.dto.UpdateUserDetailsDto;
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


    @Query(
            value = "UPDATE user_table SET fb_link=:fbLink, snapchat_link=:snapchatLink, twitter_link=:twitterLink," +
                    "insta_link=:instaLink, linkedin_link=:linkedinLink, linktree_link=:linktreeLink WHERE email=:email",
            nativeQuery = true)
    @Modifying
    @Transactional
    void updateSocials(@Param("fbLink") String fbLink, @Param("twitterLink") String twitterLink, @Param("snapchatLink")
                       String snapchatLink, @Param("instaLink") String instaLink, @Param("linkedinLink") String
                       linkedinLink, @Param("linktreeLink") String linktreeLink, @Param("email") String email);

    UserEntity findByResetToken(String token);
}
