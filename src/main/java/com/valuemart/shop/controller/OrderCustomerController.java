package com.valuemart.shop.controller;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.DiscountResponse;
import com.valuemart.shop.domain.models.OrderModel;
import com.valuemart.shop.domain.models.dto.OrderDTO;
import com.valuemart.shop.domain.service.abstracts.ProductOrderService;
import com.valuemart.shop.domain.util.UserUtils;
import com.valuemart.shop.persistence.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
       return productOrderService.convertCartToOrder(user,orderDTO.getAddressId(), orderDTO.getMessage(),orderDTO.isUseWallet());
    }

    @GetMapping("/{orderId}/{branchId}")
    private OrderModel getOrder (@PathVariable Long orderId, @PathVariable Long branchId){
        User user = UserUtils.getLoggedInUser();
       return productOrderService.getOrder(orderId,branchId,user);
    }

    @PostMapping("/discountCheck")
    private DiscountResponse discountCheck (){
        User user = UserUtils.getLoggedInUser();
       return productOrderService.applyDiscount(user);
    }

    @GetMapping("/generateLink")
    private ResponseMessage generateLink(@RequestParam String code){
        User user = UserUtils.getLoggedInUser();
        return productOrderService.getGenerateLink(user,code);
    }


    @GetMapping("/all")
    private List<OrderModel> getAllOrders(){
        User user = UserUtils.getLoggedInUser();
        return productOrderService.getOrderByCustomer(user);
    }
}
