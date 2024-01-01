package com.valuemart.shop.providers.flutterwave;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
@Builder
@Configuration
public class FlutterwaveConfig {
    @Value("${flutterwave.base.url}")
    private final String baseUrl;
    @Value("${flutterwave.api.domain}")
    private final Domain domain;
    @Value("${flutterwave.api.key}")
    private final String key;
    @Value("${flutterwave.api.secret.key}")
    private final String secretKey;
    @Value("${flutterwave.api.public.key}")
    private final String publicKey;
    @Value("${flutterwave.api.transfer.callback}")
    private final String transactionCallBackUrl;
    private final String paymentReason;

    @Value("${flutterwave.api.create.url}")
    private final String createPaymentUrl;

    public enum Domain {
        TEST("test"), LIVE("live");

        private final String domain;

        Domain(String domain) {
            this.domain = domain;
        }

        public static Domain fromString(String domain) {
            for (Domain b : Domain.values()) {
                if (b.domain.equalsIgnoreCase(domain)) {
                    return b;
                }
            }
            return TEST;
        }
    }
}
