package com.valuemart.shop.providers.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.valuemart.shop.domain.ResponseMessage;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;


    private final CloudinaryConfig cloudinaryConfig;


    public CloudinaryService(CloudinaryConfig cloudinaryConfig) {
        this.cloudinaryConfig = cloudinaryConfig;
        // Initialize Cloudinary with your account details

        System.out.println(this.cloudinaryConfig.getKey());
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", this.cloudinaryConfig.getName(),
                "api_key", this.cloudinaryConfig.getKey(),
                "api_secret", this.cloudinaryConfig.getSecret()));
    }



    public ResponseMessage storeProductImage(MultipartFile multipartFile) {
        String url = "";
        try {
            Map<String, Object> options = new HashMap<>();
            options.put("resource_type", "auto");

            // For better handling, you can include the original file name
            String originalFilename = multipartFile.getOriginalFilename();
            if (originalFilename != null) {
                options.put("public_id", FilenameUtils.getBaseName(originalFilename)); // Without extension
            }

            Map uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), options);
            url = (String) uploadResult.get("url");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseMessage.builder().message(url).build();
    }
}
