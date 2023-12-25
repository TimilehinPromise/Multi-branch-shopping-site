package com.valuemart.shop.controller;


import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.CartModel;
import com.valuemart.shop.domain.models.dto.CartDto;
import com.valuemart.shop.domain.service.abstracts.CartService;
import com.valuemart.shop.domain.util.UserUtils;
import com.valuemart.shop.persistence.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequestMapping(value = "v1/api/cart", produces = APPLICATION_JSON_VALUE)
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMessage addToCart(@RequestBody CartDto addToCartDto) {
        User user = UserUtils.getLoggedInUser();
       return cartService.addToCart(addToCartDto, user);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public CartModel getCartItems()  {
        User user = UserUtils.getLoggedInUser();
        return cartService.listCartItems(user);
    }

    @PatchMapping("/update")
    public ResponseMessage updateCartItem(@RequestBody @Valid CartDto dto)  {
        return cartService.updateCartQuantity(dto);
    }

    @PostMapping("/remove/product")
    public ResponseMessage removeProductFromCart( @RequestParam(name = "cartId") Long cartId  )  {
        User user = UserUtils.getLoggedInUser();
        return cartService.deleteCartItem(cartId,user);
    }

    @PostMapping("/clear")
    public ResponseMessage clearCart( )  {
        User user = UserUtils.getLoggedInUser();
        return cartService.deleteUserCartItems(user);
    }


}
