package com.winkly.service.impl;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import com.winkly.config.JwtUtils;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
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

        public String upload(String authToken, MultipartFile file) {
            String email = jwtUtils.getEmailFromJwtToken(authToken);
            Map config = new HashMap();
            config.put("cloud_name", cloudConfigName);
            config.put("api_key", cloudConfigApi);
            config.put("api_secret", cloudConfigKey);
            Cloudinary cloudinary = new Cloudinary(config);
            if (email != null) {
                try {
                    Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                    String publicId = uploadResult.get("public_id").toString();
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
}
