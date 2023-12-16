package com.valuemart.shop.domain.service.concretes;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.AddressDTO;
import com.valuemart.shop.domain.models.AddressModel;
import com.valuemart.shop.domain.models.UserUpdate;
import com.valuemart.shop.domain.service.abstracts.UserService;
import com.valuemart.shop.exception.NotFoundException;
import com.valuemart.shop.persistence.entity.Address;
import com.valuemart.shop.persistence.entity.User;
import com.valuemart.shop.persistence.repository.AddressRepository;
import com.valuemart.shop.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    public ResponseMessage updateProfile(UserUpdate userUpdate){

        if (!userUpdate.getPhone().isBlank()){
        Optional<User> existingUser = userRepository.findById(userUpdate.getUser().getId());
        existingUser.get().setPhone(userUpdate.getPhone());
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
    public ResponseMessage addAddress(AddressDTO address, User user){
        addressRepository.save(Address.builder().user(user).street(address.getStreet())
                .city(address.getCity())
                .landmark(address.getLandmark())
                        .build()
                );
        return ResponseMessage.builder().message("Address added successfully").build();
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


}
