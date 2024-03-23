package com.valuemart.shop.providers.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.valuemart.shop.domain.ResponseMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;
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



    public ResponseMessage storeProductImage(Object file) {
        String url = "";
        try {
            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            url = (String) uploadResult.get("url");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseMessage.builder().message(url).build();
    }
}
