package com.valuemart.shop.domain.service.concretes;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.*;
import com.valuemart.shop.domain.models.dto.AddressDTO;
import com.valuemart.shop.domain.service.abstracts.EmailService;
import com.valuemart.shop.domain.service.abstracts.ProductsService;
import com.valuemart.shop.domain.service.abstracts.UserService;
import com.valuemart.shop.domain.util.GeneratorUtils;
import com.valuemart.shop.exception.BadRequestException;
import com.valuemart.shop.exception.NotFoundException;
import com.valuemart.shop.persistence.entity.Address;
import com.valuemart.shop.persistence.entity.PasswordResetToken;
import com.valuemart.shop.persistence.entity.PaymentStatus;
import com.valuemart.shop.persistence.entity.User;
import com.valuemart.shop.persistence.repository.AddressRepository;
import com.valuemart.shop.persistence.repository.PasswordResetTokenRepository;
import com.valuemart.shop.persistence.repository.PaymentRepository;
import com.valuemart.shop.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final ProductsService productsService;
    private final PaymentRepository paymentRepository;

    @Value("${app.user.reset-password}")
    String userPasswordResetLink;

    @Value("${app.login.url}")
    String loginUrl;

    @Override
    public ResponseMessage updateProfile(UserUpdate userUpdate){

        if (!userUpdate.getPhone().isBlank()){
        Optional<User> existingUser = userRepository.findById(userUpdate.getUser().getId());
        existingUser.get().setPhone(userUpdate.getPhone());
        existingUser.get().setBranchId(userUpdate.getBranchId());
        userRepository.save(existingUser.get());
        }

        for (AddressDTO address:userUpdate.getAddressList())
              {
                  Optional<Address> foundAddress = addressRepository.findByUserIdAndId(userUpdate.getUser().getId(), address.getAddressId());
                  if (foundAddress.isEmpty()){
                      throw new NotFoundException("address not found");
                  }

                  Address address1 = foundAddress.get();
                  address1.setCity(address.getCity());
                  address1.setLandmark(address.getLandmark());
                  address1.setStreet(address.getStreet());
                  addressRepository.save(address1);

        }
       return ResponseMessage.builder().message("Profile updated successfully").build();
    }

    @Override
    public User getUser(Long userId){
       return userRepository.findById(userId).orElseThrow();
    }


    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }


    @Override
    public ResponseMessage addAddress(AddressDTO address, User user){
        addressRepository.save(Address.builder().user(user).street(address.getStreet())
                .city(address.getCity())
                .landmark(address.getLandmark())
                        .name(address.getName())
                        .build()
                );
        return ResponseMessage.builder().message(address.getName().toLowerCase() +"  Address added successfully").build();
    }

    @Override
    public void checkIfBranchHasBeenSet(User user){
       User user1 = userRepository.findById(user.getId()).orElseThrow();
       if (user1.getBranchId() == 0){
           throw new BadRequestException("Please set preferred branch");
       }
    }


    @Override
    public ResponseMessage setPreferredAddress(Long userId, Long addressId){
        List<Address> addresses = addressRepository.findAllByUserId(userId);
        for (Address address:addresses) {
            boolean state = address.isPreferred();
            if (!address.getId().equals(addressId) && address.isPreferred()){
            address.setPreferred(false);
            }
            else if (address.getId().equals(addressId) && !address.isPreferred()){
                address.setPreferred(true);
            }
            if (state != address.isPreferred()){
                addressRepository.save(address);
            }
        }
        return ResponseMessage.builder().message("Preferred address set successfully").build();
    }

    @Override
    public AddressModel getAddressByAddressId(Long addressId, Long userId){
       return addressRepository.findByUserIdAndId(userId,addressId).map(Address::toModel).get();
    }

    @Override
    public Page<AddressModel> getAllAddresses(Long userId, Pageable pageable){

       return addressRepository.findAllByUserId(userId,pageable).map(Address::toModel);
    }



    @Override
    public ResponseMessage sendResetPassword(String email){
        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new NotFoundException("Invalid Credentials"));

        String token = GeneratorUtils.generateRandomString(16);

        PasswordResetToken resetToken = new PasswordResetToken(user.getId(), token);
        passwordResetTokenRepository.save(resetToken);

        emailService.sendPasswordReset(user, userPasswordResetLink.concat(token));

        return ResponseMessage.builder().message("Reset password sent successfully to "+ email ).build();
    }

    @Override
    public ResponseMessage resetPassword(NewPassword newPassword)  {
        Optional<PasswordResetToken> resetToken = passwordResetTokenRepository.findByResetToken(newPassword.getResetToken());
        if (resetToken.isEmpty()) {
            throw new BadRequestException("expired link");
        }

        Long userId = resetToken.get().getUserId();
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new BadRequestException("expired link");
        }
        String hashedPassword = passwordEncoder.encode(newPassword.getNewPassword());
        User user = userOptional.get();
        user.setPassword(hashedPassword);
        if (user.getRetries()>=5 && !user.isEnabled()){
            user.setEnabled(true);
            user.setRetries(0);
            userRepository.save(user);
        }
        userRepository.save(user);
        passwordResetTokenRepository.deleteById(userId);

        emailService.sendPasswordReset(user, loginUrl);

        return ResponseMessage.builder().message("Reset password successful").build();
    }

    @Override
    public User changePassword(Long userId, PasswordChange passwordChange) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found"));
        if (!passwordEncoder.matches(passwordChange.getOldPassword(), user.getPassword())) {
            throw new BadCredentialsException("wrong password");
        }

        String hashedPassword = passwordEncoder.encode(passwordChange.getNewPassword());
        user.setPassword(hashedPassword);

        var savedEntity = userRepository.save(user);

        emailService.passwordResetNotification(user,loginUrl);

        return savedEntity;
    }

    @Override
    public UserModel getUserByRoyaltyCode(String code){
          UserModel userModel = userRepository.findByRoyaltyCode(code).map(User::toModel).get();
          if (userModel == null){
              throw new NotFoundException("User not found with such royalty code");
          }
          return userModel;
    }

    @Override
    public List<UserModel> getAllStaffs(){
        return userRepository.findAllByRoleId(Long.valueOf(2)).stream().map(User::toModel).toList();
    }

    public Long getAllStaffCount(){
        return userRepository.findAllByRoleId(Long.valueOf(2)).stream().count();
    }


    @Override
    public StatsResponse getStats(){
       Long productCount = productsService.getAllProducts();
       Long staffCount = getAllStaffCount();
       Long paymentCount = paymentRepository.countAllByStatus(PaymentStatus.SUCCESS);

       return StatsResponse.builder()
               .payment(paymentCount)
               .staff(staffCount)
               .product(productCount)
               .build();
    }





}
