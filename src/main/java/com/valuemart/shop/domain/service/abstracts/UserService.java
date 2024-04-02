package com.valuemart.shop.domain.service.abstracts;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.*;
import com.valuemart.shop.domain.models.dto.AddressDTO;
import com.valuemart.shop.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    ResponseMessage updateProfile(UserUpdate userUpdate);

    User getUser(Long userId);

    User saveUser(User user);


    Optional<User> getUserByEmail(String email);

    ResponseMessage addAddress(AddressDTO address, User user);

    void checkIfBranchHasBeenSet(User user);

    ResponseMessage setPreferredAddress(Long userId, Long addressId);

    AddressModel getAddressByAddressId(Long addressId, Long userId);

    Page<AddressModel> getAllAddresses(Long userId, Pageable pageable);

    ResponseMessage sendResetPassword(String email);

    ResponseMessage resetPassword(NewPassword newPassword);

    User changePassword(Long userId, PasswordChange passwordChange);

    UserModel getUserByRoyaltyCode(String code);

    List<UserModel> getAllStaffs();

    StatsResponse getStats();
}
