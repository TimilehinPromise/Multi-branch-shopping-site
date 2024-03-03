package com.valuemart.shop.providers.flutterwave;

import com.valuemart.shop.config.HttpApiClient;
import com.valuemart.shop.domain.models.ChargeModel;
import com.valuemart.shop.domain.models.OrderModel;
import com.valuemart.shop.domain.service.abstracts.PaymentProcessor;
import com.valuemart.shop.exception.BadRequestException;
import com.valuemart.shop.persistence.entity.Payment;
import com.valuemart.shop.persistence.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class FlutterwaveFacade extends HttpApiClient implements PaymentProcessor  {


    @Autowired
    private FlutterwaveConfig config;

    public FlutterwaveFacade(RestTemplate restTemplate) {
        super(restTemplate);
    }


    private FlwChargeRequest buildFlRequest(OrderModel model, User user, Payment payment){
        return FlwChargeRequest.builder()
                .currency("NGN")
                .amount(model.getAmount())
                .customer(FlwCustomer.builder()
                        .email("ncsksdsd@gmail.com")
                        .name(user.getFirstName() + " " + user.getLastName())
                .build())
                .redirectUrl("https://webhook.site/96b8b2ad-c9bc-4604-8c8c-d69f7f3a7123")
                .transactionRef(payment.getPaymentReference().getReferenceId())
                .build();
    }

    @Override
    public String getName() {
        return "Flutterwave";
    }


    public ChargeModel initiatePayment(OrderModel model, User user,Payment payment) {
        FlwChargeRequest request = buildFlRequest(model, user,payment);
        String url = config.getBaseUrl().concat(config.getCreatePaymentUrl());

        var type = new ParameterizedTypeReference<FlwResponseModel<FlwChargeResponse>>() {};

        FlwResponseModel<FlwChargeResponse>  response=  sendRequest(HttpMethod.POST,url,request,getHeaders(),type);
        System.out.println(response.getStatus() + " status");
        System.out.println(response.getMessage().toString());
        System.out.println(response.getData().getLink());
        ChargeModel responseModel ;
        if ("success".equals(response.getStatus())){
            responseModel = ChargeModel.builder()
                    .link(response.getData().getLink())
                    .build();
            return responseModel;
        } else {
            throw new BadRequestException(response.getMessage());
        }
    }

    public  Map<String, String> getHeaders(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization",config.getSecretKey());
        return headers;
    }
}
