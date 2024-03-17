package com.valuemart.shop.domain.service.abstracts;

import com.valuemart.shop.domain.models.CartModel;
import com.valuemart.shop.persistence.entity.User;
import org.springframework.scheduling.annotation.Async;

public interface EmailService {
    void sendCustomerCreationEmail(User user) throws Exception;


    @Async
    void sendStaffCreationEmail(User user, User admin, String branchName);

    void sendPasswordReset(User user, String link);

    void passwordResetNotification(User user, String link);

    @Async
    void orderResponseNotification(User user, String link, String message, CartModel cart,String paymentLink);

    void sendEmailVerification(User user, String link) throws Exception;
}
