package com.valuemart.shop.controller;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.dto.AdminLogisticDto;
import com.valuemart.shop.domain.service.abstracts.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequestMapping(value = "v1/api/admin", produces = APPLICATION_JSON_VALUE)
public class AdminController {


    private final DeliveryService deliveryService;


    public AdminController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping("/logistics")
    public ResponseMessage addLogisticsPricing(@RequestBody AdminLogisticDto dto){
       return deliveryService.addOrUpdateDeliveryAreas(dto);
    }
}
