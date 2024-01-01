package com.valuemart.shop.config;


//import org.apache.velocity.app.VelocityEngine;
//import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valuemart.shop.exception.AsyncExceptionHandler;
import com.valuemart.shop.providers.flutterwave.FlutterwaveConfig;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;
import java.util.TimeZone;

@Configuration
public class AppConfig {

    @Value("${flutterwave.base.url}")
    private String flutterwaveUrl;

    @Value("${flutterwave.api.key}")
    private String key;

    @Value("${flutterwave.api.secret.key}")
    private String flutterwaveSecretKey;

    @Value("${flutterwave.api.public.key}")
    private String flutterwavePublicKey;

    @Value("${flutterwave.api.domain:test}")
    private String flutterwaveDomain;

    @Value("${flutterwave.api.transfer.callback}")
    private String flutterwaveTransactionCallbackUrl;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

//    @Bean
//    public PaystackConfig paystackConfig() {
//        return PaystackConfig.builder()
//                .baseUrl(paystackUrl)
//                .domain(PaystackConfig.Domain.fromString(paystackDomain))
//                .liveApiKey(paystackLiveApiKey)
//                .testApiKey(paystackTestApiKey)
//                .paymentReason("ValuePlus Payment")
//                .transferCallBackUrl(transferCallbackUrl)
//                .build();
//    }

//    @Bean
//    public VelocityEngine velocityEngine() {
//        VelocityEngine velocityEngine = new VelocityEngine();
//        velocityEngine.setProperty("resource.loader", "class");
//        velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
//        return velocityEngine;
//    }
//
//    @Bean
//    public JavaMailSender javaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//
//        mailSender.setHost("in-v3.mailjet.com");
//        mailSender.setPort(587);
//        mailSender.setUsername("2a3d7ddea97ab7f824b4798d770d5f02");
//        mailSender.setPassword("74ce25c6b7f3bec5802f1c5923f97751");
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.debug.auth", "true");
//        props.put("mail.debug", "true");
//        props.put("mail.smtp.ssl.trust", "*");
//        props.put("mail.smtp.starttls.enable","true");
//
//        return mailSender;
//    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
//
private ObjectMapper defaultObjectMapper(){
    TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("Africa/Lagos");
    ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    OBJECT_MAPPER.setTimeZone(DEFAULT_TIMEZONE);
    return OBJECT_MAPPER;
}

    @Bean
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }
//
    @Bean
    public FlutterwaveConfig flutterwaveConfig() {
        return FlutterwaveConfig.builder()
                .baseUrl(flutterwaveUrl)
                .domain(FlutterwaveConfig.Domain.fromString(flutterwaveDomain))
                .key(key)
                .secretKey(flutterwaveSecretKey)
                .publicKey(flutterwavePublicKey)
                .paymentReason("ValueMart Payment")
                .transactionCallBackUrl(flutterwaveTransactionCallbackUrl)
                .build();
    }



}
