package com.winkly.controller;

import com.winkly.config.JwtUtils;
import com.winkly.dto.LikeListDto;
import com.winkly.dto.MessageInfoDto;
import com.winkly.dto.ProfileDetailsDto;
import com.winkly.dto.UpdateUserDetailsDto;
import com.winkly.entity.UserEntity;
import com.winkly.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static java.sql.JDBCType.NULL;

@RestController
@RequestMapping(value = "/winkly_update")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "Update Socials Controller")
public class UserDetailsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @PutMapping("/update_socials")
    @ApiOperation("Update User Social Details")
    public ResponseEntity updateUserDetails(@Valid @RequestBody UpdateUserDetailsDto updateUserDetailsDto,
                                            @RequestHeader("Authorization") String token) {
            String fbLink = updateUserDetailsDto.getFbLink();
            String instaLink = updateUserDetailsDto.getInstaLink();
            String linktreeLink = updateUserDetailsDto.getLinktreeLink();
            String linkedinLink = updateUserDetailsDto.getLinkedinLink();
            String snapchatLink = updateUserDetailsDto.getSnapchatLink();
            String twitterLink = updateUserDetailsDto.getTwitterLink();
            String name = updateUserDetailsDto.getName();
            token = token.replace("Bearer ", "");
            String email = jwtUtils.getEmailFromJwtToken(token);
            Optional<UserEntity> user = userRepository.findByEmail(email);
            String username = updateUserDetailsDto.getUsername();
            String userName = user.get().getUsername();
            if (!userRepository.existsByUsername(username))
                if (userName == null)
            userRepository.updateSocials(fbLink, twitterLink, snapchatLink, instaLink, linkedinLink, linktreeLink,
                    email, username, name);
                else {
                    return ResponseEntity.badRequest().body("Username already exists!");
                }
            else {
                //can update social links and name only
                if (userName == null)
                    return ResponseEntity.badRequest().body("Username already exists!");
                String emailTemp = userRepository.findByUsername(userName).getEmail();
                if (emailTemp.equals(email))
                userRepository.updateNameAndSocialOnly(fbLink, twitterLink, snapchatLink, instaLink, linkedinLink,
                        linktreeLink, email, name);
                else
                    return ResponseEntity.badRequest().body("Username already exists!");
                return ResponseEntity.ok().body(new MessageInfoDto("Details Updated"));
            }

            return ResponseEntity.ok().body(new MessageInfoDto("Socials Updated"));
    }

    @PutMapping("/update_likes")
    @ApiOperation("Update Likes")
    @Transactional
    public ResponseEntity updateLikes(@Valid @RequestHeader("Authorization") String token, @RequestBody LikeListDto username) {
        UserEntity user = userRepository.findByUsername(username.getUsername());
        String email = user.getEmail();

        token = token.replace("Bearer ", "");
        String attractedEmail = jwtUtils.getEmailFromJwtToken(token);

        Optional<UserEntity> attractedUser = userRepository.findByEmail(attractedEmail);

        boolean liked = true;
        boolean matched = false;

        if (!user.getLikedYou().contains(attractedEmail) && !attractedUser.get().getYouLiked().contains(email)) {
            user.getLikedYou().add(attractedEmail);
            attractedUser.get().getYouLiked().add(email);

            if (user.getYouLiked().contains(attractedEmail) && attractedUser.get().getLikedYou().contains(email)) {
                matched = true;
                user.getMatched().add(attractedEmail);
                attractedUser.get().getMatched().add(email);
                return ResponseEntity.ok().body(new MessageInfoDto("It's a Match!"));
            }

        } else {
            liked = false;
            user.getLikedYou().remove(attractedEmail);
            attractedUser.get().getYouLiked().remove(email);

            try {
                user.getMatched().remove(attractedEmail);
                attractedUser.get().getMatched().remove(email);
            } catch (Exception e) {
                return ResponseEntity.ok().body("user not present in matched list");
            }
        }
        return ResponseEntity.ok().body(attractedEmail + " " + (liked ? "liked" : "disliked") + " " + email);
    }

    @GetMapping("/get_profile")
    @ApiOperation("Get Profile Details")
    @Transactional
    public ResponseEntity getProfile(@Valid @RequestHeader(value = "Authorization", required = false) String token, @RequestParam String username) {

        if (userRepository.existsByUsername(username)) {
            String email = "";
            UserEntity user = userRepository.findByUsername(username);
            Boolean verifiedStatus = user.getVerifiedStatus();
            Boolean likeStatus = null;
            Optional<UserEntity> tokenUser;
            List<LikeListDto> likedYouUsernameList = new ArrayList<>();
            List<LikeListDto> youLikedUsernameList = new ArrayList<>();
            List<LikeListDto> matchedList = new ArrayList<>();

            try {
                token = token.replace("Bearer ", "");
                email = jwtUtils.getEmailFromJwtToken(token);
                tokenUser = userRepository.findByEmail(email);

                HashSet<String> matchedSet = new HashSet<>();

                user.getMatched().forEach(
                        (String element) -> {
                            Optional<UserEntity> tempUser = userRepository.findByEmail(element);
                            matchedSet.add(tempUser.get().getEmail());
                            matchedList.add(new LikeListDto(tempUser.get().getName(), tempUser.get().getUsername()));
                        }
                );

                user.getLikedYou().forEach(
                                (String element) -> {
                                    Optional<UserEntity> tempUser = userRepository.findByEmail(element);
                                    if (!matchedSet.contains(tempUser.get().getEmail())) {
                                        likedYouUsernameList.add(new LikeListDto(tempUser.get().getName(), tempUser.get().getUsername()));
                                    }
                                }
                );

                user.getYouLiked().forEach(
                        (String element) -> {
                            Optional<UserEntity> tempUser = userRepository.findByEmail(element);
                            if (!matchedSet.contains(tempUser.get().getEmail())) {
                                youLikedUsernameList.add(new LikeListDto(tempUser.get().getName(), tempUser.get().getUsername()));
                            }
                        }
                );

                if (user.getEmail().equals(email)) {
                    return ResponseEntity.ok().body(new ProfileDetailsDto(user.getFbLink(), user.getInstaLink(),
                            user.getLinktreeLink(), user.getLinkedinLink(), user.getSnapchatLink(), user.getTwitterLink(),
                            user.getUsername(), user.getEmail(), user.getName(), likedYouUsernameList, youLikedUsernameList,
                            matchedList, likeStatus, verifiedStatus));
                }

                likeStatus = tokenUser.get().getYouLiked().contains(user.getEmail());

                return ResponseEntity.ok().body(new ProfileDetailsDto(user.getFbLink(), user.getInstaLink(),
                        user.getLinktreeLink(), user.getLinkedinLink(), user.getSnapchatLink(), user.getTwitterLink(),
                        user.getUsername(), user.getEmail(), user.getName(), likeStatus, verifiedStatus));

            } catch (Exception e) {

                return ResponseEntity.ok().body(new ProfileDetailsDto(user.getFbLink(), user.getInstaLink(),
                        user.getLinktreeLink(), user.getLinkedinLink(), user.getSnapchatLink(), user.getTwitterLink(),
                        user.getUsername(), user.getEmail(), user.getName(), likeStatus, verifiedStatus));
            }
        }

        return ResponseEntity.badRequest().body(new MessageInfoDto("Username not found"));

    }

    @PutMapping("/update_verified_status")
    @ApiOperation("Update User Verified Status")
    public void updateVerifiedStatus(@Valid @RequestParam String email, @Valid @RequestParam Boolean verifiedStatus) {
        userRepository.updateVerifiedStatus(email, verifiedStatus);
    }
}
