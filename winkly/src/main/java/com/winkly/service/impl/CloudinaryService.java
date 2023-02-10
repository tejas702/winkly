package com.winkly.service.impl;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import com.winkly.config.JwtUtils;
import com.winkly.dto.UpdateUserDetailsDto;
import com.winkly.entity.UserEntity;
import com.winkly.repository.UserRepository;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudinaryService {

        private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
        private final Cloudinary cloudinary = Singleton.getCloudinary();

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private JwtUtils jwtUtils;

        public String upload(String authToken, UpdateUserDetailsDto updateUserDetailsDto, MultipartFile file) {
            UserEntity userTemp = userRepository.findByUsername(updateUserDetailsDto.getUsername());
            String email = jwtUtils.getEmailFromJwtToken(authToken);
            logger.trace("Called CloudinaryService.upload with args: " + authToken + ", " + email + " and the multipart file");
            if (userTemp != null) {
                try {
                    Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                    String publicId = uploadResult.get("public_id").toString();
                    logger.info("The user " + email + " successfully uploaded the file: " + publicId);
                    return publicId;
                } catch (Exception ex) {
                    logger.error("The user " + email + " failed to load to Cloudinary the image file: " + file.getName());
                    logger.error(ex.getMessage());
                    return null;
                }
            } else {
                logger.error("Error: a not authenticated user tried to upload a file (email: " + email + ", authToken: " + authToken + ")");
                return null;
            }
        }
}
