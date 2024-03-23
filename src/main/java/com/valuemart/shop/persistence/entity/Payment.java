package com.valuemart.shop.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "payment")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
//@JsonIgnoreProperties({"lastupdatedAt", "paymentReference", "providerExtraInfo"})
public class Payment extends BasePersistentEntity{

    @EmbeddedId
    private PaymentReference paymentReference;

    public String getReferenceId() {
        return (paymentReference == null) ? "" : paymentReference.getReferenceId();
    }

    public String getClientRef() {
        return (paymentReference == null) ? "" : paymentReference.getUserId();
    }

    @NotNull
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PaymentStatus status;

    @NotNull
    private String provider;

    private String providerResponse;

    private Long orderId;

    @Data
    @Embeddable
    public static class PaymentReference implements Serializable {

        @NotNull
        private String referenceId;

        @NotNull
        private String userId;
    }
}
