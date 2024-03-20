package com.valuemart.shop.controller;

import com.valuemart.shop.domain.QrCodeResponse;
import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.OrderModel;
import com.valuemart.shop.domain.models.dto.AdminOrderResponseDTO;
import com.valuemart.shop.domain.models.dto.CaptureOrder;
import com.valuemart.shop.domain.service.abstracts.ProductOrderService;
import com.valuemart.shop.domain.service.abstracts.UserService;
import com.valuemart.shop.domain.util.UserUtils;
import com.valuemart.shop.persistence.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    private void acceptQrCode( @PathVariable String loyaltyCode){

    }


    @GetMapping("/{userId}/{orderId}/{branchId}")
    private OrderModel getOrder (@PathVariable Long orderId, @PathVariable Long branchId, @PathVariable Long userId){
        User user = userService.getUser(userId);
        return productOrderService.getOrder(orderId,branchId,user);
    }

    @GetMapping("/{status}")
    private List<OrderModel> getOrdersByStaff(@PathVariable String status){
        User user = UserUtils.getLoggedInUser();
       return productOrderService.getAllOrdersByStaff(status, Long.valueOf(user.getBranchId()));
    }

    @GetMapping("/qrCode")
    private QrCodeResponse getQrCodeResponse(@RequestParam String qrCode){
       return productOrderService.qrCodeResponse(qrCode);
    }


    @PostMapping("/respond")
    private ResponseMessage updateCustomerOrderStatus(@RequestBody AdminOrderResponseDTO dto){
        User user = userService.getUser(dto.getUserId());
      return productOrderService.updateOrderByAdmin(dto.getOrderId(), dto.getBranchId(), dto.getStatus(),user, dto.getMessage());
    }

    @PostMapping("/capture-order")
    private ResponseMessage captureInStoreOrder(@RequestBody CaptureOrder order){
        User user = userService.getUser(order.getUserId());
        User staffUser = UserUtils.getLoggedInUser();
        return productOrderService.captureOrder(order,user,staffUser);
    }
}
