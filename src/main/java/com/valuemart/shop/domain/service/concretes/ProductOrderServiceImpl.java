package com.valuemart.shop.domain.service.concretes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.AddressModel;
import com.valuemart.shop.domain.models.CartModel;
import com.valuemart.shop.domain.models.OrderModel;
import com.valuemart.shop.domain.models.enums.OrderStatus;
import com.valuemart.shop.domain.service.abstracts.CartService;
import com.valuemart.shop.domain.service.abstracts.ProductOrderService;
import com.valuemart.shop.domain.service.abstracts.UserService;
import com.valuemart.shop.domain.service.abstracts.WalletService;
import com.valuemart.shop.persistence.entity.Orders;
import com.valuemart.shop.persistence.entity.User;
import com.valuemart.shop.persistence.entity.Wallet;
import com.valuemart.shop.persistence.repository.OrdersRepository;
import com.valuemart.shop.persistence.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.TimeZone;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductOrderServiceImpl implements ProductOrderService {

    private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("Africa/Lagos");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final UserService userService;
    private final OrdersRepository ordersRepository;
    private final WalletService walletService;
    private final WalletRepository walletRepository;
    private static final BigDecimal THRESHOLD = BigDecimal.valueOf(1000.00);


    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.setTimeZone(DEFAULT_TIMEZONE);
    }

    private final CartService cartService;

    @Transactional
    @Override
    public ResponseMessage convertCartToOrder(User user, Long addressId, String message){
        
        userService.checkIfBranchHasBeenSet(user);
        CartModel model = cartService.listCartItems(user);
        Wallet wallet = walletService.getOrCreateCoinWalletIfNotExist(user);
        log.info(String.valueOf(model.getTotalCost()));
        log.info(String.valueOf(THRESHOLD));
        if (model.getTotalCost().compareTo(THRESHOLD) == 1){
          wallet.setCount(wallet.getCount()+ 1);
         walletService.addToWallet(wallet);
         walletRepository.save(wallet);
        }

        String details ="";
        AddressModel addressModel = userService.getAddressByAddressId(addressId, user.getId());
        try {
             details = OBJECT_MAPPER.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Orders orders = Orders.builder()
                .details(details)
                .amount(model.getTotalCost())
                .discountedAmount(BigDecimal.ZERO)
                .branchId(Long.valueOf(user.getBranchId()))
                .status(OrderStatus.PENDING)
                .address(buildAddress(addressModel))
                .paymentProvider("RexPay")
                .user(user)
                .message(message)
                .fromCheckout(true)
                .build();

        ordersRepository.save(orders);

        cartService.deleteUserCartItems(user);

        return ResponseMessage.builder().message("Order successfully created").build();

    }


    private OrderModel getOrder(Long orderId,Long branchId){
        return ordersRepository.findFirstByIdAndBranchId(orderId,branchId).map(Orders::toModel).orElseThrow( );
    }

    @Override
    public OrderModel getOrder(Long orderId, Long branchId, User user){
        OrderModel model = ordersRepository.findFirstByIdAndBranchId(orderId,branchId).map(Orders::toModel).orElseThrow( );
        Wallet wallet = walletService.getWallet(user);
        model.setDiscountedAmount(model.getAmount().subtract(wallet.getAmount()));
        return model;
    }

    @Override
    public ResponseMessage updateOrderByAdmin(Long orderId, Long branchId, OrderStatus status,User user){
      OrderModel model = getOrder(orderId, branchId,user);
      model.setStatus(status);
        return ResponseMessage.builder().message("Order has been set to " + status.name()).build();
    }

    private String buildAddress(AddressModel addressModel){
        return addressModel.getStreet() + " " + addressModel.getCity() + " (" + addressModel.getLandmark() + ")";
    }
}
