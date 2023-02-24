package com.winkly.service.impl;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import com.winkly.config.JwtUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.winkly.entity.UserEntity;
import com.winkly.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class CloudinaryService {

        private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
        private final Cloudinary cloudinary = Singleton.getCloudinary();

        @Autowired
        private JwtUtils jwtUtils;

        @Value("${cloudinary.config.cloud_name}")
        private String cloudConfigName;

        @Value("${cloudinary.config.api_key}")
        private String cloudConfigApi;

        @Value("${cloudinary.config.api_secret}")
        private String cloudConfigKey;

        @Autowired
        private UserRepository userRepository;

        public String upload(String authToken, MultipartFile file) {
            String email = jwtUtils.getEmailFromJwtToken(authToken);
            Optional<UserEntity> user = userRepository.findByEmail(email);
            String username = user.get().getUsername();
            Map config = new HashMap();
            config.put("cloud_name", cloudConfigName);
            config.put("api_key", cloudConfigApi);
            config.put("api_secret", cloudConfigKey);
            Cloudinary cloudinary = new Cloudinary(config);
            if (email != null) {
                try {
                    Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("public_id", username + "-screenshot"));
                    String publicId = uploadResult.get("public_id").toString();
                    String cloudUrl = cloudinary.url().secure(true).publicId(username + "-screenshot").generate();
                    String tempInfo = "Successfully Uploaded";
                    String info = "The user " + email + " successfully uploaded the file: " + publicId;
                    return tempInfo;
                } catch (Exception ex) {
                    String info = "Invalid Image";
                    return info;
                }
            } else {
                String info = "Invalid Image";
                return info;
            }
        }

    public String upload_profile_pic(String authToken, MultipartFile file) {
        String email = jwtUtils.getEmailFromJwtToken(authToken);
        Optional<UserEntity> user = userRepository.findByEmail(email);
        Long id = user.get().getId();
        Map config = new HashMap();
        config.put("cloud_name", cloudConfigName);
        config.put("api_key", cloudConfigApi);
        config.put("api_secret", cloudConfigKey);
        Cloudinary cloudinary = new Cloudinary(config);
        if (email != null) {
            try {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("public_id", "id_" + id + "-profile"));
                String publicId = uploadResult.get("public_id").toString();
                String tempInfo = "Successfully Uploaded";
                String cloudUrl = cloudinary.url().secure(true).publicId("id_" + id + "-profile").generate();
                userRepository.updateProfilePicture(email, cloudUrl);
                String info = "The user " + email + " successfully uploaded the file: " + publicId;
                return tempInfo;
            } catch (Exception ex) {
                String info = "Invalid Image";
                return info;
            }
        } else {
            String info = "Invalid Image";
            return info;
        }
    }
}
