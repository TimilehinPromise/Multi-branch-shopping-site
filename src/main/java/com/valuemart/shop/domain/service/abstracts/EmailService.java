package com.valuemart.shop.domain.service.abstracts;

import com.valuemart.shop.persistence.entity.User;

public interface EmailService {
    void sendPasswordReset(User user, String link);

    void sendPinReset(User user, String link) throws Exception;

    void sendPinNotification(User user) throws Exception;

    void sendEmailVerification(User user, String link) throws Exception;
}
