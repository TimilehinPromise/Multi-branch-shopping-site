package com.valuemart.shop.controller;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.OrderModel;
import com.valuemart.shop.domain.models.dto.AdminOrderResponseDTO;
import com.valuemart.shop.domain.service.abstracts.ProductOrderService;
import com.valuemart.shop.domain.service.abstracts.UserService;
import com.valuemart.shop.domain.util.UserUtils;
import com.valuemart.shop.persistence.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@Slf4j
@RequestMapping(value = "v1/api/admin/order", produces = APPLICATION_JSON_VALUE)
public class OrderAdminController {

    private final ProductOrderService productOrderService;
    private final UserService userService;

    public OrderAdminController(ProductOrderService productOrderService, UserService userService) {
        this.productOrderService = productOrderService;
        this.userService = userService;
    }


    @GetMapping("/{userId}/{orderId}/{branchId}")
    private OrderModel getOrder (@PathVariable Long orderId, @PathVariable Long branchId, @PathVariable Long userId){
        User user = userService.getUser(userId);
        return productOrderService.getOrder(orderId,branchId,user);
    }

    @PostMapping("/respond")
    private ResponseMessage updateCustomerOrderStatus(@RequestBody AdminOrderResponseDTO dto){
        User user = userService.getUser(dto.getUserId());
      return productOrderService.updateOrderByAdmin(dto.getOrderId(), dto.getBranchId(), dto.getStatus(),user, dto.getMessage());
    }
}
