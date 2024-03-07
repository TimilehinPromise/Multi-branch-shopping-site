package com.valuemart.shop.controller;

import com.valuemart.shop.domain.service.abstracts.DeliveryService;
import com.valuemart.shop.domain.service.abstracts.ThresholdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequestMapping(value = "v1/api/admin", produces = APPLICATION_JSON_VALUE)
public class AdminController {


    private final DeliveryService deliveryService;

    private final ThresholdService thresholdService;


    public AdminController(DeliveryService deliveryService, ThresholdService thresholdService) {
        this.deliveryService = deliveryService;
        this.thresholdService = thresholdService;
    }

}
