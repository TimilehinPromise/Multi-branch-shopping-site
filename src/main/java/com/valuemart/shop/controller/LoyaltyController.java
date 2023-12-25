package com.valuemart.shop.controller;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.LoyaltyModel;
import com.valuemart.shop.domain.models.dto.CartDto;
import com.valuemart.shop.domain.models.dto.LoyaltyDTO;
import com.valuemart.shop.domain.service.abstracts.LoyaltyService;
import com.valuemart.shop.domain.util.UserUtils;
import com.valuemart.shop.persistence.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequestMapping(value = "v1/api/loyalty", produces = APPLICATION_JSON_VALUE)
public class LoyaltyController {

    @Autowired
    private LoyaltyService loyaltyService;

    @PutMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseMessage updateLoyalty(@RequestBody LoyaltyDTO loyaltyDTO) {
        return loyaltyService.updateLoyaltyFormula(loyaltyDTO);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public LoyaltyModel getLoyaltyFormula() {
        return loyaltyService.getLoyaltyFormula();
    }
}
