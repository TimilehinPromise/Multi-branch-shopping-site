package com.valuemart.shop.domain.service.abstracts;

import com.valuemart.shop.persistence.entity.User;

public interface EmailService {
    void sendEmailVerification(User user, String link) throws Exception;
}
