package com.valuemart.shop.config.security;

import com.valuemart.shop.persistence.entity.User;

import java.util.Map;

public class CustomUserDetails {
    private final User user;

    private Map<String, Object> attributes;

    public CustomUserDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public CustomUserDetails(User user) {
        this.user = user;
    }
    public Long getId() {
        return user.getId();
    }

    public User getUser() {
        return user;
    }

    public String getUsername() {
        return user.getUsername();
    }
}
