package com.valuemart.shop.controller;


import com.valuemart.shop.domain.models.RedirectResponse;
import com.valuemart.shop.domain.models.dto.RedirectDTO;
import com.valuemart.shop.domain.service.abstracts.ProductOrderService;
import com.valuemart.shop.domain.util.UserUtils;
import com.valuemart.shop.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "v1/api/payment", produces = APPLICATION_JSON_VALUE)
public class PaymentController {

    private final ProductOrderService orderService;

    @PostMapping("/check")
    private RedirectResponse checkout(@RequestBody RedirectDTO dto){
        User user = UserUtils.getLoggedInUser();
        return orderService.handleRedirect(dto,user);
    }

}
