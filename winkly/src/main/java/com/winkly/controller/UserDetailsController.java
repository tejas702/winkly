package com.winkly.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winkly.config.JwtUtils;
import com.winkly.dto.*;
import com.winkly.entity.*;
import com.winkly.repository.FeedbackRepository;
import com.winkly.repository.UserRepository;
import com.winkly.service.impl.CloudinaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.util.*;


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

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @PutMapping(value = "/update_socials")
    @ApiOperation("Update User Social Details")
    @Transactional
    public ResponseEntity updateUserDetails(@Valid @RequestPart String profile,
                                            @RequestHeader("Authorization") String token, @RequestPart(value = "file", required = false) MultipartFile file) {
            token = token.replace("Bearer ", "");
            if (jwtUtils.checkExpiryForAccessToken(token)) {
                return ResponseEntity.badRequest().body(new JwtRequestDto(true));
            }
            ObjectMapper objectMapper = new ObjectMapper();
            UpdateUserDetailsDto updateUserDetailsDto;
            try {
                updateUserDetailsDto = objectMapper.readValue(profile, UpdateUserDetailsDto.class);
            } catch (JsonProcessingException e) {
                return ResponseEntity.badRequest().body(new MessageInfoDto("Error Parsing"));
            }
            String instaLink = updateUserDetailsDto.getInstaLink();
            String twitterLink = updateUserDetailsDto.getTwitterLink();
            String name = updateUserDetailsDto.getName();
            String bio = updateUserDetailsDto.getBio();
            Integer age = updateUserDetailsDto.getAge();
            String location = updateUserDetailsDto.getLocation();
            token = token.replace("Bearer ", "");
            String email = jwtUtils.getEmailFromJwtToken(token);
            Optional<UserEntity> user = userRepository.findByEmail(email);
            String username = updateUserDetailsDto.getUsername();
            String userName = user.get().getUsername();
            List<Links> extraLinks = updateUserDetailsDto.getExtraLinksList();
            List<Problems> problemsList = updateUserDetailsDto.getProblemsList();

            if (Objects.nonNull(file)) {
              String extension = file.getOriginalFilename();
              if (extension.contains(".png")
                || extension.contains(".jpg")
                || extension.contains(".jpeg")
                || extension.contains(".gif")
                || extension.contains(".apng")
                || extension.contains(".avif")
                || extension.contains(".jfif")
                || extension.contains(".pjpeg")
                || extension.contains(".pjp")
                || extension.contains(".svg")
                || extension.contains(".webp")) {
              String response = cloudinaryService.upload_profile_pic(token, file);
              if (!response.equals("Successfully Uploaded")) {
                  return ResponseEntity.badRequest().body(new MessageInfoDto(response));
                }
              } else {
                return ResponseEntity.badRequest().body(new MessageInfoDto("Invalid Image"));
              }
            }
            if (!userRepository.existsByUsername(username))
                if (userName == null) {
                    userRepository.updateSocials(twitterLink, instaLink, email, username, name, bio, age, location);

                    user.get().getExtraLinks().clear();

                    if (Objects.nonNull(extraLinks))
                        for (Links link : extraLinks) {
                            user.get().getExtraLinks().add(new Links(link.getLinkName(), link.getUrl()));
                        }

                    if (Objects.nonNull(problemsList))
                        for (Problems problem : problemsList) {
                            if (!user.get().getProblems().stream().anyMatch(ele -> (ele.getEmail().equals(problem.getEmail())))) {
                                user.get().getProblems().add(new Problems(email, problem.getProblem_1(),
                                        problem.getProblem_2(), problem.getProblem_3(), problem.getProblem_4()));
                            }
                        }
                }
                else {
                    return ResponseEntity.badRequest().body("Username already exists!");
                }
            else {
                //can update social links and name only
                if (userName == null)
                    return ResponseEntity.badRequest().body(new MessageInfoDto("Username does not exist!"));
                String emailTemp = userRepository.findByUsername(userName).getEmail();
                if (emailTemp.equals(email)) {
                    userRepository.updateNameAndSocialOnly(twitterLink, instaLink, email, name, bio, age, location);

                    user.get().getExtraLinks().clear();

                    if (Objects.nonNull(extraLinks))
                        for (Links link : extraLinks) {
                                user.get().getExtraLinks().add(new Links(link.getLinkName(), link.getUrl()));
                        }

                    if (Objects.nonNull(problemsList))
                        for (Problems problem : problemsList) {
                            if (!user.get().getProblems().stream().anyMatch(ele -> (ele.getEmail().equals(problem.getEmail())))) {
                                user.get().getProblems().add(new Problems(email, problem.getProblem_1(),
                                        problem.getProblem_2(), problem.getProblem_3(), problem.getProblem_4()));
                            }
                        }
                }
                else
                    return ResponseEntity.badRequest().body(new MessageInfoDto("Username already exists!"));
                return ResponseEntity.ok().body(new MessageInfoDto("Details Updated"));
            }

            return ResponseEntity.ok().body(new MessageInfoDto("Socials Updated"));
    }

    @PutMapping("/update_likes")
    @ApiOperation("Update Likes")
    @Transactional
    public ResponseEntity updateLikes(@Valid @RequestHeader("Authorization") String token, @RequestBody LikeListDto username) {
        token = token.replace("Bearer ", "");
        if (jwtUtils.checkExpiryForAccessToken(token)) {
            return ResponseEntity.badRequest().body(new JwtRequestDto(true));
        }
        UserEntity user = userRepository.findByUsername(username.getUsername());
        String email = user.getEmail();

        token = token.replace("Bearer ", "");
        String attractedEmail = jwtUtils.getEmailFromJwtToken(token);

        Optional<UserEntity> attractedUser = userRepository.findByEmail(attractedEmail);

        boolean liked = true;
        boolean matched = false;

        if (!user.getLikedYou().stream().anyMatch(ele -> (ele.getEmail()).equals(attractedEmail)) &&
                !attractedUser.get().getYouLiked().stream().anyMatch(ele -> (ele.getEmail()).equals(email))) {
            user.getLikedYou().add(new Likes(attractedEmail, attractedUser.get().getUsername(),
                    attractedUser.get().getName(), username.getReason()));
            attractedUser.get().getYouLiked().add(new Likes(email, user.getUsername(), user.getName(), username.getReason()));

            if (user.getYouLiked().stream().anyMatch(ele -> (ele.getEmail()).equals(attractedEmail)) &&
                    attractedUser.get().getLikedYou().stream().anyMatch(ele -> (ele.getEmail()).equals(email))) {
                matched = true;
                user.getMatched().add(attractedEmail);
                attractedUser.get().getMatched().add(email);
                return ResponseEntity.ok().body(new MessageInfoDto("It's a Match!"));
            }

        } else {
            liked = false;
            user.getLikedYou().removeIf(ele -> (ele.getEmail()).equals(attractedEmail));
            attractedUser.get().getYouLiked().removeIf(ele -> (ele.getEmail()).equals((email)));

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
    public ResponseEntity getProfile(@RequestHeader(value = "Authorization", required = false) String token,
                                     @RequestParam String username) {

        if (Objects.nonNull(token) && !token.isEmpty()) {
          token = token.replace("Bearer ", "");
            if (jwtUtils.checkExpiryForAccessToken(token)) {
                return ResponseEntity.badRequest().body(new JwtRequestDto(true));
            }
        }
        if (userRepository.existsByUsername(username)) {
            String email = "";
            UserEntity user = userRepository.findByUsername(username);
            String verifiedStatus = user.getVerifiedStatus();
            String cloudUrl = user.getProfilePicture();
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
                            matchedList.add(new LikeListDto(tempUser.get().getName(), tempUser.get().getUsername(),
                                    tempUser.get().getProfilePicture()));
                        }
                );

                // TODO: existing flow of youLiked and likedYou feature is not clear will revamp in future.

                String youLikedReason = null;
                for (Likes like : user.getLikedYou()) {
                    if (like.getEmail().equals(tokenUser.get().getEmail())) {
                        youLikedReason = like.getReason();
                    }
                    if (!matchedSet.contains(like.getEmail())) {
                        likedYouUsernameList.add(new LikeListDto(like.getName(), like.getUsername(),
                                userRepository.findByEmail(like.getEmail()).get().getProfilePicture(), like.getReason()));
                    }
                }
                String likedYouReason = null;
                for (Likes like : user.getYouLiked()) {
                    if (like.getEmail().equals(tokenUser.get().getEmail())) {
                        likedYouReason = like.getReason();
                    }
                    if (!matchedSet.contains(like.getEmail())) {
                        youLikedUsernameList.add(new LikeListDto(like.getName(), like.getUsername(),
                                userRepository.findByEmail(like.getEmail()).get().getProfilePicture(), like.getReason()));
                    }
                }

                if (user.getEmail().equals(email)) {
                    return ResponseEntity.ok().body(new ProfileDetailsDto(user.getInstaLink(), user.getTwitterLink(),
                            user.getUsername(), user.getEmail(), user.getName(), user.getBio(), user.getAge(),
                            user.getLocation(), likedYouUsernameList, youLikedUsernameList, matchedList, likeStatus,
                            verifiedStatus, user.getExtraLinks(), "", "", cloudUrl));
                }

                likeStatus = tokenUser.get().getYouLiked().stream().anyMatch(ele -> (ele.getEmail().equals(user.getEmail())));

                return ResponseEntity.ok().body(new ProfileDetailsDto(user.getInstaLink(), user.getTwitterLink(),
                        user.getUsername(), user.getEmail(), user.getName(), user.getBio(), user.getAge(),
                        user.getLocation(), likeStatus, verifiedStatus, user.getExtraLinks(), likedYouReason,
                        youLikedReason, cloudUrl));

            } catch (Exception e) {

                return ResponseEntity.ok().body(new ProfileDetailsDto(user.getInstaLink(), user.getTwitterLink(),
                        user.getUsername(), user.getEmail(), user.getName(), user.getBio(), user.getAge(),
                        user.getLocation(), likeStatus, verifiedStatus, user.getExtraLinks(), "",
                        "", cloudUrl));
            }
        }

        return ResponseEntity.badRequest().body(new MessageInfoDto("Username not found"));

    }

    @PutMapping("/update_verified_status")
    @ApiOperation("Update User Verified Status")
    public void updateVerifiedStatus(@RequestBody UsernameDto usernameDto) {
        UserEntity user = userRepository.findByUsername(usernameDto.getUsername());
        userRepository.updateVerifiedStatusEmail(user.getEmail(), "Accepted");
    }

    @PutMapping("/send_feedback")
    @ApiOperation("Send Feedback")
    public ResponseEntity sendFeedback(@RequestBody SendFeedbackDto sendFeedbackDto) {
    FeedbackEntity feedbackEntity = FeedbackEntity.builder()
            .email(sendFeedbackDto.getEmail())
            .name(sendFeedbackDto.getName())
            .response(sendFeedbackDto.getResponse())
            .build();
        feedbackRepository.save(feedbackEntity);
        return ResponseEntity.ok().build();
    }

}
