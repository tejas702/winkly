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
    Optional<UserEntity> findByEmail(String email);

    UserEntity findByUsername(String username);
    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    @Query(
            value = "UPDATE user_table SET password=:password WHERE email=:email",
            nativeQuery = true)
    @Modifying
    @Transactional
    void updatePassword(@Param("password") String password, @Param("email") String email);


    @Query(
            value = "UPDATE user_table SET fb_link=:fbLink, snapchat_link=:snapchatLink, twitter_link=:twitterLink," +
                    "insta_link=:instaLink, linkedin_link=:linkedinLink, linktree_link=:linktreeLink, username=:username, " +
                    "name=:name WHERE email=:email",
            nativeQuery = true)
    @Modifying
    @Transactional
    void updateSocials(@Param("fbLink") String fbLink, @Param("twitterLink") String twitterLink, @Param("snapchatLink")
                       String snapchatLink, @Param("instaLink") String instaLink, @Param("linkedinLink") String
                       linkedinLink, @Param("linktreeLink") String linktreeLink, @Param("email") String email,
                       @Param("username") String username, @Param("name") String name);

    UserEntity findByResetToken(String token);


}
