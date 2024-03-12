package com.valuemart.shop.domain.service.concretes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.valuemart.shop.domain.QrCodeResponse;
import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.*;
import com.valuemart.shop.domain.models.dto.ThresholdDTO;
import com.valuemart.shop.domain.models.enums.OrderStatus;
import com.valuemart.shop.domain.service.abstracts.*;
import com.valuemart.shop.domain.util.PaymentUtils;
import com.valuemart.shop.exception.BadRequestException;
import com.valuemart.shop.exception.NotFoundException;
import com.valuemart.shop.persistence.entity.*;
import com.valuemart.shop.persistence.repository.OrdersRepository;
import com.valuemart.shop.persistence.repository.PaymentRepository;
import com.valuemart.shop.persistence.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    private final EmailService emailService;
    private final ThresholdService thresholdService;
    private final DeliveryService deliveryService;
    private final PaymentRepository paymentRepository;

    private final PaymentProcessorFactory factory;

    private static final BigDecimal THRESHOLD = BigDecimal.valueOf(1000.00);


    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.setTimeZone(DEFAULT_TIMEZONE);
    }

    private final CartService cartService;

    @Transactional
    @Override
    public ResponseMessage convertCartToOrder(User user, Long addressId, String message,Boolean useWallet){
        
        userService.checkIfBranchHasBeenSet(user);
        CartModel model = cartService.listCartItems(user);
        Wallet wallet = walletService.getOrCreateCoinWalletIfNotExist(user);

        AddressModel addressModel = userService.getAddressByAddressId(addressId, user.getId());
        BigDecimal deliveryAmount =  deliveryService.getDeliveryPriceByArea(addressModel.getCity());

        BigDecimal originalAmount = model.getTotalCost();
        BigDecimal amount = model.getTotalCost().add(deliveryAmount);

        log.info(String.valueOf(amount));
        BigDecimal walletAmount = wallet.getAmount();

        DiscountResponse  response = null;
        if (useWallet){
              response = applyDiscountInternal(model.getTotalCost(), walletAmount);
        }

        if (Objects.nonNull(response)) {
            if(response.getSuccessful()){
            amount = response.getAmount().add(deliveryAmount);
            model.setTotalCost(amount);
        }}

        else {
            model.setTotalCost(model.getTotalCost().add(deliveryAmount));
        }

        ThresholdDTO threshold = thresholdService.getThresholdByValueOrNearestBelow(model.getTotalCost());
        if (Objects.nonNull(threshold)){
            wallet.setCount(wallet.getCount()+ 1);
            wallet.setAmount(wallet.getAmount().add(threshold.getMonetaryAmount()));
            walletService.updateWallet(wallet);
        }

        String details ="";
        try {
             details = OBJECT_MAPPER.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Orders orders = Orders.builder()
                .details(details)
                .amount(originalAmount)
                .discountedAmount(amount)
                .branchId(Long.valueOf(user.getBranchId()))
                .status(OrderStatus.PENDING)
                .address(buildAddress(addressModel))
                .paymentProvider("Flutterwave")
                .user(user)
                .message(message)
                .fromCheckout(true)
                .addressId(addressModel.getAddressId())
                .build();

        ordersRepository.save(orders);

        cartService.deleteUserCartItems(user);

        return ResponseMessage.builder().message("Order successfully created").build();

    }



    @Override
    public DiscountResponse applyDiscount(User user) {

        Wallet wallet = walletService.getWallet(user);
        CartModel model = cartService.listCartItems(user);
        BigDecimal walletAmount = wallet.getAmount();
        BigDecimal discountedAmount;

        // Check if wallet amount is greater than total cost
        if (walletAmount.compareTo(model.getTotalCost()) > 0) {
            // If wallet amount is greater, cap the discount at the total cost to prevent negative balance
            discountedAmount = model.getTotalCost();
        } else {
            // Apply the wallet amount as discount
            discountedAmount = walletAmount;
        }

        // Calculate the final amount to be paid after discount
        System.out.println(discountedAmount);
        System.out.println(model.getTotalCost());
        System.out.println(model.getTotalCost().subtract(discountedAmount));
        BigDecimal finalAmountToBePaid = model.getTotalCost().subtract(discountedAmount);

        // Define the minimum amount acceptable by the online processor
        BigDecimal minimumOnlineProcessorAmount = new BigDecimal("1000");

        if (finalAmountToBePaid.compareTo(minimumOnlineProcessorAmount) < 0) {
            return DiscountResponse.builder()
                    .amount(model.getTotalCost())
                    .successful(false)
                    .message("Amount to be paid has to be above 1000")
                    .build();
        }
        else return DiscountResponse.builder()
                .amount(finalAmountToBePaid)
                .successful(true)
                .message("Discount applied successfully")
                .build();
    }

    public DiscountResponse applyDiscountInternal(BigDecimal amount, BigDecimal walletAmount) {
        BigDecimal discountedAmount;
        // Check if wallet amount is greater than total cost
        if (walletAmount.compareTo(amount) > 0) {
            // If wallet amount is greater, cap the discount at the total cost to prevent negative balance
            discountedAmount = amount;
        } else {
            // Apply the wallet amount as discount
            discountedAmount = walletAmount;
        }

        // Calculate the final amount to be paid after discount
        BigDecimal finalAmountToBePaid = amount.subtract(discountedAmount);

        // Define the minimum amount acceptable by the online processor
        BigDecimal minimumOnlineProcessorAmount = new BigDecimal("1000");

        if (finalAmountToBePaid.compareTo(minimumOnlineProcessorAmount) < 0) {
            return DiscountResponse.builder()
                    .amount(amount)
                    .successful(false)
                    .message("Amount to be paid has to be above 1000")
                    .build();
        }
        else return DiscountResponse.builder()
                .amount(finalAmountToBePaid)
                .successful(true)
                .message("Discount applied successfully")
                .build();
    }

    @Override
    public void addDeliveryAmountToOrder(BigDecimal deliveryAmount, Long orderId){
      Orders order = ordersRepository.findById(orderId).get();
        order.setDeliveryAmount(deliveryAmount);
        ordersRepository.save(order);
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
    public List<OrderModel> getAllOrdersByStaff(String status, Long branchId){
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        System.out.println(orderStatus.name());
        if (Objects.nonNull(status)) {
            return ordersRepository.findAllByStatusAndBranchIdOrderByCreatedAtDesc(orderStatus, branchId).stream().map(Orders::toModel).toList();
        }
        else
            return ordersRepository.findAllByStatusAndBranchIdOrderByCreatedAtDesc(OrderStatus.PENDING,branchId).stream().map(Orders::toModel).toList();
    }

    public Orders getOrderInternal(Long orderId, Long branchId, User user){
        Orders model = ordersRepository.findFirstByIdAndBranchId(orderId,branchId).orElseThrow( );
        Wallet wallet = walletService.getWallet(user);
        model.setDiscountedAmount(model.getAmount().subtract(wallet.getAmount()));
        return model;
    }

    @Override
    public OrderModel getOrder(Long branchId, User user) {
        List<OrderStatus> statuses = Arrays.asList(OrderStatus.IN_PROGRESS, OrderStatus.IN_PROGRESS_BUT_DELAYED);
        OrderModel model = ordersRepository.findFirstByUserIdAndStatusInAndBranchIdOrderByCreatedAtDesc(user.getId(), statuses, branchId)
                .map(Orders::toModel)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        Wallet wallet = walletService.getWallet(user);
        BigDecimal walletAmount = wallet.getAmount() != null ? wallet.getAmount() : BigDecimal.ZERO;
        model.setDiscountedAmount(model.getAmount().subtract(walletAmount));
        return model;
    }

    @Override
    public ResponseMessage updateOrderByAdmin(Long orderId, Long branchId, OrderStatus status, User user, String message){
      Orders order = getOrderInternal(orderId, branchId,user);
        order.setStatus(status);
        order.setShopResponse(message);
       String link =  createPayment(user).getLink();
       order.setPaymentLink(link);
      OrderModel orderModel =  ordersRepository.save(order).toModel();
        emailService.orderResponseNotification(user,"emptylinkfornow.com",message,orderModel.getDetails());

        return ResponseMessage.builder().message("Order has been set to " + status.name()).build();
    }


    public ChargeModel createPayment(User user){
        System.out.println("create payment method");

        Payment.PaymentReference reference = new Payment.PaymentReference();
        reference.setUserId(user.getId().toString());
        reference.setReferenceId(PaymentUtils.generateTransRef());


        OrderModel model = getOrder(Long.valueOf(user.getBranchId()),user);

        if (model.getStatus().equals(OrderStatus.IN_PROGRESS) || model.getStatus().equals(OrderStatus.IN_PROGRESS_BUT_DELAYED)){
            System.out.println(model);

            final PaymentProcessor paymentProcessor = factory.getProcessor("Flutterwave");

            Payment payment = new Payment();
            payment.setProvider(paymentProcessor.getName());
            payment.setAmount(model.getDiscountedAmount().compareTo(model.getAmount()) < 0 ? model.getDiscountedAmount() :model.getAmount());
            payment.setPaymentReference(reference);
            payment.setStatus(PaymentStatus.CREATED);
            paymentRepository.save(payment);
            System.out.println(payment);

            ChargeModel chargeModel = paymentProcessor.initiatePayment(model,user,payment);
            System.out.println(chargeModel);
            return chargeModel;
        }
        else {
            throw new BadRequestException("Order not in right status");
        }
    }


    private String buildAddress(AddressModel addressModel){
        return addressModel.getStreet() + " " + addressModel.getCity() + " (" + addressModel.getLandmark() + ")";
    }


    @Override
    public QrCodeResponse qrCodeResponse(String code){
        UserModel userModel =userService.getUserByRoyaltyCode(code);

        Wallet wallet =  walletService.getWallet(userModel);

        return QrCodeResponse.builder()
                .amount(wallet.getAmount())
                .model(userModel)
                .build();
    }
}
