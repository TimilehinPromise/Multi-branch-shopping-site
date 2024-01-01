package com.valuemart.shop.domain.service.concretes;

import com.valuemart.shop.domain.service.abstracts.PaymentProcessor;
import com.valuemart.shop.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
public class PaymentProcessorFactory {

    private final List<PaymentProcessor> paymentProcessors = Lists.newArrayList();
    private static final String INVALID_PROCESSOR = "processor is invalid";
    private static final String PROVIDER = "Rexpay";

    @Autowired
    public void setProxyProviders(List<PaymentProcessor> providers) {
        this.paymentProcessors.addAll(providers);
    }

    public PaymentProcessor getProcessor() {


        return filterPaymentProcessor(PROVIDER);
    }

    public PaymentProcessor getProcessor(final String providerName) {

        return filterPaymentProcessor(providerName);
    }

    private PaymentProcessor filterPaymentProcessor(String providerName) {
        return paymentProcessors.stream()
                .filter(p -> p.getName().equalsIgnoreCase(providerName))
                .map(PaymentProcessor.class::cast).findFirst()
                .orElseThrow(() -> new BadRequestException(INVALID_PROCESSOR));
    }
}
