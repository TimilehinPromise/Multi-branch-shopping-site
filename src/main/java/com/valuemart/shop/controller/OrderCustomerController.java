package com.valuemart.shop.controller;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.OrderModel;
import com.valuemart.shop.domain.models.dto.OrderDTO;
import com.valuemart.shop.domain.service.abstracts.ProductOrderService;
import com.valuemart.shop.domain.util.UserUtils;
import com.valuemart.shop.persistence.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequestMapping(value = "v1/api/order", produces = APPLICATION_JSON_VALUE)
public class OrderCustomerController {

    private final ProductOrderService productOrderService;

    public OrderCustomerController(ProductOrderService productOrderService) {
        this.productOrderService = productOrderService;
    }

    @PostMapping("/checkout")
    private ResponseMessage checkout(@RequestBody OrderDTO orderDTO){
        User user = UserUtils.getLoggedInUser();
       return productOrderService.convertCartToOrder(user,orderDTO.getAddressId(), orderDTO.getMessage());
    }

    @GetMapping("/{orderId}/{branchId}")
    private OrderModel getOrder (@PathVariable Long orderId, @PathVariable Long branchId){
        User user = UserUtils.getLoggedInUser();
       return productOrderService.getOrder(orderId,branchId,user);
    }
}
