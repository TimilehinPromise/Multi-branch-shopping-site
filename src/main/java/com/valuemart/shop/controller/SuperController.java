package com.valuemart.shop.controller;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.ThresholdModel;
import com.valuemart.shop.domain.models.UserCreate;
import com.valuemart.shop.domain.models.dto.AdminLogisticDto;
import com.valuemart.shop.domain.models.dto.DisableStaffDTO;
import com.valuemart.shop.domain.service.abstracts.AuthenticationService;
import com.valuemart.shop.domain.service.abstracts.DeliveryService;
import com.valuemart.shop.domain.service.abstracts.ThresholdService;
import com.valuemart.shop.domain.util.UserUtils;
import com.valuemart.shop.persistence.entity.Threshold;
import com.valuemart.shop.persistence.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequestMapping(value = "v1/api/super",  produces = APPLICATION_JSON_VALUE)
public class SuperController {

    private final AuthenticationService authenticationService;
    private final ThresholdService thresholdService;

    private final DeliveryService deliveryService;

    public SuperController(AuthenticationService authenticationService, ThresholdService thresholdService, DeliveryService deliveryService) {
        this.authenticationService = authenticationService;
        this.thresholdService = thresholdService;
        this.deliveryService = deliveryService;
    }


    @PostMapping("/createStaff")
    public ResponseMessage createStaff(@Valid @RequestBody UserCreate userCreate) {
        User user = UserUtils.getLoggedInUser();
        return authenticationService.createStaffByAdmin(userCreate,user);
    }

    @PostMapping("/disable/staff")
    public ResponseMessage disableStaff (@RequestBody DisableStaffDTO disableStaffDTO){
       return authenticationService.disableStaffByAdmin(disableStaffDTO.getStaffId());
    }

    @PostMapping("/logistics")
    public ResponseMessage addLogisticsPricing(@RequestBody AdminLogisticDto dto){
        return deliveryService.addOrUpdateDeliveryAreas(dto);
    }


    @PostMapping("/loyalty")
    public ResponseMessage addOrUpdateLoyaltyThreshold(@RequestBody ThresholdModel model){
        return thresholdService.addOrUpdateThreshold(model);
    }

    @GetMapping(path = "/loyalty", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Threshold> getAllLoyaltyThreshold(){
        return thresholdService.getAllThreshold();
    }


}
