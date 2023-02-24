package com.winkly.repository;

import com.winkly.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
            value = "UPDATE user_table SET twitter_link=:twitterLink," +
                    "insta_link=:instaLink, username=:username, " +
                    "name=:name, bio=:bio WHERE email=:email",
            nativeQuery = true)
    @Modifying
    @Transactional
    void updateSocials(@Param("twitterLink") String twitterLink, @Param("instaLink") String instaLink,
                       @Param("email") String email, @Param("username") String username, @Param("name") String name,
                       @Param("bio") String bio);

    @Query(
            value = "UPDATE user_table SET twitter_link=:twitterLink," +
                    "insta_link=:instaLink," +
                    "name=:name, bio=:bio WHERE email=:email",
            nativeQuery = true)
    @Modifying
    @Transactional
    void updateNameAndSocialOnly(@Param("twitterLink") String twitterLink, @Param("instaLink") String instaLink,
                                 @Param("email") String email, @Param("name") String name, @Param("bio") String bio);

    UserEntity findByResetToken(String token);

    @Query(
            value = "UPDATE user_table SET verified_status=:verifiedStatus WHERE username=:username",
            nativeQuery = true)
    @Modifying
    @Transactional
    void updateVerifiedStatus(@Param("username") String username, @Param("verifiedStatus") String verifiedStatus);

    @Query(
            value = "UPDATE user_table SET verified_status=:verifiedStatus WHERE email=:email",
            nativeQuery = true)
    @Modifying
    @Transactional
    void updateVerifiedStatusEmail(@Param("email") String email, @Param("verifiedStatus") String verifiedStatus);

    @Query(
            value = "SELECT * FROM user_table WHERE (name REGEXP :regexp)",
            nativeQuery = true)
    @Modifying
    @Transactional
    List<UserEntity> getSearchRegex(@Param("regexp") String regexp);

    @Query(
            value = "UPDATE user_table SET profile_picture=:profilePicture WHERE email=:email",
            nativeQuery = true)
    @Modifying
    @Transactional
    void updateProfilePicture(@Param("email") String email, @Param("profilePicture") String profilePicture);

    @Query(
            value = "UPDATE user_table SET username=:username WHERE email=:email",
            nativeQuery = true)
    @Modifying
    @Transactional
    void updateUsername(@Param("email") String email, @Param("username") String username);

}
