package com.valuemart.shop.domain.service.abstracts;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.dto.AddressDTO;
import com.valuemart.shop.domain.models.AddressModel;
import com.valuemart.shop.domain.models.UserUpdate;
import com.valuemart.shop.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    ResponseMessage updateProfile(UserUpdate userUpdate);

    User getUser(Long userId);

    ResponseMessage addAddress(AddressDTO address, User user);

    void checkIfBranchHasBeenSet(User user);

    ResponseMessage setPreferredAddress(Long userId, Long addressId);

    AddressModel getAddressByAddressId(Long addressId, Long userId);

    Page<AddressModel> getAllAddresses(Long userId, Pageable pageable);
}
