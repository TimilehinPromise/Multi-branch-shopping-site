package com.valuemart.shop.domain.service.concretes;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.CartListModel;
import com.valuemart.shop.domain.models.CartModel;
import com.valuemart.shop.domain.models.dto.CartDto;
import com.valuemart.shop.domain.service.abstracts.CartService;
import com.valuemart.shop.exception.BadRequestException;
import com.valuemart.shop.exception.CartItemNotExistException;
import com.valuemart.shop.persistence.entity.Cart;
import com.valuemart.shop.persistence.entity.Product;
import com.valuemart.shop.persistence.entity.User;
import com.valuemart.shop.persistence.repository.CartRepository;
import com.valuemart.shop.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    @Override
    public ResponseMessage addToCart(CartDto dto, User user){

        productExistInCart(dto.getSkuId(), user);

       Optional<Product> productOptional = productRepository.findFirstBySkuIdAndDeletedFalseAndBranches_Id(dto.getSkuId(), Long.valueOf(user.getBranchId()));
       if (productOptional.isEmpty()){
           throw new BadRequestException("Product Does Not Exist or Not Available for branch");
       }
       Product product = productOptional.get();
        Cart cart = Cart.builder()
                .user(user)
                .price(product.getPrice())
                .quantity(dto.getQuantity())
                .product(product)
                .build();
        cartRepository.save(cart);

        return ResponseMessage.builder().message( product.getName() +" Added to Cart").build();
    }


    public void productExistInCart(String skuId, User user) {
        boolean productExists = cartRepository.findAllByUserOrderByCreatedAtDesc(user).stream()
                .anyMatch(cart -> skuId.equals(cart.getProduct().getSkuId()));

        if (productExists) {
            throw new BadRequestException("Product Already In the Cart");
        }
    }


    @Override
    public CartModel listCartItems(User user) {
        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedAtDesc(user);
        List<CartListModel> cartItems = new ArrayList<>();
        for (Cart cart:cartList){
            CartListModel cartListModel = getDtoFromCart(cart);
            cartItems.add(cartListModel);
        }
        BigDecimal totalCost = BigDecimal.ZERO;
        for (CartListModel cartListModel :cartItems){
            BigDecimal itemPrice = cartListModel.getProduct().getPrice();
            BigDecimal productQuantity = BigDecimal.valueOf(cartListModel.getQuantity());
            BigDecimal presentCost = itemPrice.multiply(productQuantity);
            totalCost = totalCost.add(presentCost);
        }
        return new CartModel(cartItems,totalCost);
    }

    @Override
    public ResponseMessage updateCartQuantity(CartDto cartDto){
        Cart cart = cartRepository.getOne(cartDto.getId());
        cart.setQuantity(cartDto.getQuantity());
        cartRepository.save(cart);

        return ResponseMessage.builder().message( "Cart Quantity updated to " + cart.getQuantity()).build();
    }

    @Override
    public ResponseMessage deleteCartItem(Long id,User user)  {
        if (!cartRepository.existsByIdAndUser(id,user))
            throw new CartItemNotExistException("Cart id is invalid : " + id);
        cartRepository.deleteById(id);

        return ResponseMessage.builder().message( "Cart Item deleted").build();
    }

    @Override
    public ResponseMessage deleteUserCartItems(User user) {
        cartRepository.deleteByUser(user);
        return ResponseMessage.builder().message( "Cart Cleared").build();
    }


    private CartListModel getDtoFromCart(Cart cart) {
        return CartListModel.builder()
                .id(cart.getId())
                .price(cart.getProduct().getPrice())
                .quantity(cart.getQuantity())
                .product(cart.getProduct().toModel())
                .build();
    }
}
