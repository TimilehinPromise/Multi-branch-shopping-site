package com.valuemart.shop.domain.service.concretes;

import com.valuemart.shop.domain.service.abstracts.EmailService;
import com.valuemart.shop.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.StringWriter;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final EmailClient emailClient;
    public static final String PASSWORD_SUBJECT = "Value Plus Forgot Password";
    public static final String PIN_SUBJECT = "Value Plus Reset Pin";
    public static final String VERIFY_EMAIL_SUBJECT = "Value Mart Verify Email";



    public static final String USER_CREATION_SUBJECT = "Welcome to ValueMart - Your Shopping Adventure Begins!";

    public static final String LOGIN_URL = "localhost:9010/v1/api/auth/login";

    public static final String PIN_UPDATE = "Value Mart Pin Update";

    private final VelocityEngine velocityEngine;
    public static final String PRODUCT_ORDER_CREATION_SUBJECT = "Value Plus Product Order Creation Notification";


    @Override
    @Async
    public void sendCustomerCreationEmail(User user) throws Exception {
        Template template = velocityEngine.getTemplate("/templates/customercreate.vm");
        VelocityContext context = new VelocityContext();
        context.put("CustomerName", user.getFirstName());
        context.put("loginurl",LOGIN_URL);
        context.put("MallName","ValueMart");
        context.put("Year", LocalDateTime.now().getYear());
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
        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);

        emailClient.sendSimpleMessage(user.getEmail(), PASSWORD_SUBJECT, stringWriter.toString());
    }

    public void testSendPasswordReset(User user, String link) throws Exception {
        Template template = velocityEngine.getTemplate("/templates/v2/passwordreset.vm");
        VelocityContext context = new VelocityContext();
        context.put("name", user.getFirstName() + " " + user.getLastName());
        context.put("link", link);
        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);

        emailClient.testSendSimpleMessage(user.getEmail(), PASSWORD_SUBJECT, stringWriter.toString());
    }

    @Override
    public void sendPinReset(User user, String link) throws Exception {
        Template template = velocityEngine.getTemplate("/templates/v2/pinreset.vm");
        VelocityContext context = new VelocityContext();
        context.put("name", user.getFirstName() + " " + user.getLastName());
        context.put("link", link);
        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);

        emailClient.sendSimpleMessage(user.getEmail(), PIN_SUBJECT, stringWriter.toString());
    }

    @Override
    public void sendPinNotification(User user) throws Exception {
        Template template = velocityEngine.getTemplate("/templates/v2/pinupdate.vm");
        VelocityContext context = new VelocityContext();
        context.put("name", user.getFirstName() + " " + user.getLastName());
        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);

        emailClient.sendSimpleMessage(user.getEmail(), PIN_UPDATE, stringWriter.toString());
    }

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
