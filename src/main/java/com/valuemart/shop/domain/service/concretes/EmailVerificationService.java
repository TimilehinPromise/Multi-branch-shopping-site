package com.valuemart.shop.domain.service.concretes;

import com.valuemart.shop.domain.service.abstracts.EmailService;
import com.valuemart.shop.persistence.repository.EmailVerificationTokenRepository;
import com.valuemart.shop.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailVerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private String verifyEmailLink;


}
