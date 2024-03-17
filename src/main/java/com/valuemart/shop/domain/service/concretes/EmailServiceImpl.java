package com.valuemart.shop.domain.service.concretes;

import com.valuemart.shop.domain.models.CartListModel;
import com.valuemart.shop.domain.models.CartModel;
import com.valuemart.shop.domain.models.ProductModel;
import com.valuemart.shop.domain.service.abstracts.EmailService;
import com.valuemart.shop.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EmailServiceImpl implements EmailService {

    private final EmailClient emailClient;
    public static final String PASSWORD_SUBJECT = "Action Required: Password Reset for Value Mart";
    public static final String PIN_SUBJECT = "Value Plus Reset Pin";
    public static final String VERIFY_EMAIL_SUBJECT = "Value Mart Verify Email";



    public static final String USER_CREATION_SUBJECT = "Welcome to ValueMart - Your Shopping Adventure Begins!";

    public static final String LOGIN_URL = "localhost:9010/v1/api/auth/login";

    public static final String PIN_UPDATE = "Password Update Alert for Your ValueMart Account";

    public static final String NAME = "ValueMart";

    private final VelocityEngine velocityEngine;


    @Override
    @Async
    public void sendCustomerCreationEmail(User user)  {
        Template template = velocityEngine.getTemplate("/templates/customercreate.vm");
        VelocityContext context = new VelocityContext();
        context.put("CustomerName", user.getFirstName() + " " + user.getLastName());
        context.put("loginurl",LOGIN_URL);
        context.put("MallName",NAME);
        context.put("Year", LocalDateTime.now().getYear());
        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);

        emailClient.sendSimpleMessage(user.getEmail(), USER_CREATION_SUBJECT, stringWriter.toString());
    }

    @Override
    @Async
    public void sendStaffCreationEmail(User user, User admin,String branchName)  {
        Template template = velocityEngine.getTemplate("/templates/staffcreate.vm");
        VelocityContext context = new VelocityContext();
        context.put("CustomerName", user.getFirstName() + " " + user.getLastName());
        context.put("AdminName",admin.getFirstName() + " " + admin.getLastName());
        context.put("passsword",user.getPassword());
        context.put("loginurl",LOGIN_URL);
        context.put("MallName",NAME);
        context.put("Year", LocalDateTime.now().getYear());
        context.put("branch",branchName);
        context.put("email",user.getEmail());
        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);

        emailClient.sendSimpleMessage(user.getEmail(), USER_CREATION_SUBJECT, stringWriter.toString());
    }

    @Override
    public void sendPasswordReset(User user, String link)  {
        Template template = velocityEngine.getTemplate("/templates/passwordreset.vm");
        VelocityContext context = new VelocityContext();
        context.put("name", user.getFirstName() + " " + user.getLastName());
        context.put("link", link);
        context.put("Mall_Name",NAME);
        context.put("Year", LocalDateTime.now().getYear());
        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);

        emailClient.sendSimpleMessage(user.getEmail(), PASSWORD_SUBJECT, stringWriter.toString());
    }

    @Override
    public void passwordResetNotification(User user, String link)  {
        Template template = velocityEngine.getTemplate("/templates/passwordresetnotification.vm");
        VelocityContext context = new VelocityContext();
        context.put("name", user.getFirstName() + " " + user.getLastName());
        context.put("link", link);
        context.put("Mall_Name",NAME);
        context.put("Year", LocalDateTime.now().getYear());
        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);

        emailClient.sendSimpleMessage(user.getEmail(), PIN_UPDATE, stringWriter.toString());
    }

    @Override
    @Async
    public void orderResponseNotification(User user, String link, String message, CartModel cart,String paymentLink) {
        Template template = velocityEngine.getTemplate("/templates/orderResponseNotification.vm");
        VelocityContext context = new VelocityContext();
        context.put("name", user.getFirstName() + " " + user.getLastName());
        context.put("link", link);
        context.put("Mall_Name", NAME);
        context.put("Year", LocalDateTime.now().getYear());
        context.put("OrderStatusMessage", message);
        context.put("Payment_Link",paymentLink);

        List<Map<String, Object>> productDetailsList = new ArrayList<>();

        for (CartListModel item : cart.getCartItems()) {
            Map<String, Object> productDetails = new HashMap<>();
            productDetails.put("name", item.getProduct().getName());
            productDetails.put("quantity", item.getQuantity());
            productDetails.put("price", item.getPrice());
            productDetailsList.add(productDetails);
        }

        context.put("OrderDetails", productDetailsList);

        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);

        emailClient.sendSimpleMessage(user.getEmail(), "Order Update From ValueMart", stringWriter.toString());
    }




//
//
//    @Override
//    public void sendPinNotification(User user) throws Exception {
//        Template template = velocityEngine.getTemplate("/templates/v2/pinupdate.vm");
//        VelocityContext context = new VelocityContext();
//        context.put("name", user.getFirstName() + " " + user.getLastName());
//        StringWriter stringWriter = new StringWriter();
//        template.merge(context, stringWriter);
//
//        emailClient.sendSimpleMessage(user.getEmail(), PIN_UPDATE, stringWriter.toString());
//    }

    @Override
    public void sendEmailVerification(User user, String link) throws Exception {
        Template template = velocityEngine.getTemplate("/templates/v2/verifyemail.vm");
        VelocityContext context = new VelocityContext();
        context.put("name", user.getFirstName() + " " + user.getLastName());
        context.put("link", link);
        context.put("email", user.getEmail());
        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);

        emailClient.sendSimpleMessage(user.getEmail(), VERIFY_EMAIL_SUBJECT, stringWriter.toString());
    }
//
//    @Override
//    public void sendAdminUserCreationEmail(User user, String password) throws Exception {
//        Template template = velocityEngine.getTemplate("/templates/v2/admincreation.vm");
//        VelocityContext context = new VelocityContext();
//        context.put("username", user.getEmail());
//        context.put("name", user.getFirstName() + " " + user.getLastName());
//        context.put("password", password);
//        StringWriter stringWriter = new StringWriter();
//        template.merge(context, stringWriter);
//
//        emailClient.sendSimpleMessage(user.getEmail(), USER_CREATION_SUBJECT, stringWriter.toString());
//    }
//
//    @Override
//    public void sendSubAdminUserCreationEmail(User user, String password) throws Exception {
//        Template template = velocityEngine.getTemplate("/templates/v2/subadmincreation.vm");
//        VelocityContext context = new VelocityContext();
//        context.put("username", user.getEmail());
//        context.put("name", user.getFirstName() + " " + user.getLastName());
//        context.put("password", password);
//        StringWriter stringWriter = new StringWriter();
//        template.merge(context, stringWriter);
//
//        emailClient.sendSimpleMessage(user.getEmail(), USER_CREATION_SUBJECT, stringWriter.toString());
//    }



}
