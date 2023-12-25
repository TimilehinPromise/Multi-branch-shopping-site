package com.valuemart.shop.domain.service.abstracts;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.CartModel;
import com.valuemart.shop.domain.models.dto.CartDto;
import com.valuemart.shop.persistence.entity.User;

public interface CartService {
    ResponseMessage addToCart(CartDto dto, User user);

    CartModel listCartItems(User user);

    ResponseMessage updateCartQuantity(CartDto cartDto);

    ResponseMessage deleteCartItem(Long id, User user);

    ResponseMessage deleteUserCartItems(User user);
}
