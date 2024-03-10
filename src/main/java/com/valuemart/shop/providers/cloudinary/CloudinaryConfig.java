package com.valuemart.shop.providers.cloudinary;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Component
@Builder
@ConfigurationProperties(prefix = "cloudinary")
public class CloudinaryConfig {


    private String name;
    private String key;

    private String secret;
}
