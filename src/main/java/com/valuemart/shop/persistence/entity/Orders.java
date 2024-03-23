package com.valuemart.shop.persistence.entity;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valuemart.shop.domain.models.CartModel;
import com.valuemart.shop.domain.models.OrderModel;
import com.valuemart.shop.domain.models.enums.OrderStatus;
import com.valuemart.shop.domain.util.MapperUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.TimeZone;


@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
@Accessors(chain = true)
public class Orders extends BasePersistentEntity implements ToModel{

    private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("Africa/Lagos");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    private BigDecimal discountedAmount;
    private String address;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String paymentProvider;
    private String details;
    private boolean fromCheckout;
    private String message;
    private Long branchId;
    private BigDecimal deliveryAmount;
    private String shopResponse;
    private Long addressId;
    private String paymentLink;
    private Long paymentId;

    @Override
    public OrderModel toModel() {
        CartModel cartModel = new CartModel();
        if (Objects.nonNull(details)){
        try {
          cartModel = MapperUtil.MAPPER.readValue(details, CartModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }}
        return
                OrderModel.builder()
                        .address(address)
                        .amount(amount)
                        .message(message)
                        .branchId(branchId)
                        .details(cartModel)
                        .paymentProvider(paymentProvider)
                        .discountedAmount(discountedAmount)
                        .status(status)
                        .user(user.toModel())
                        .deliveryAmount(deliveryAmount)
                        .orderId(id)
                        .shopResponse(shopResponse)
                        .createdAt(createdAt)
                        .build();
    }
}
