package com.valuemart.shop.controller;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.*;
import com.valuemart.shop.domain.models.dto.AddressDTO;
import com.valuemart.shop.domain.service.abstracts.UserService;
import com.valuemart.shop.domain.service.abstracts.WalletService;
import com.valuemart.shop.persistence.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.valuemart.shop.domain.util.UserUtils.getLoggedInUser;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping(path = "v1/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;
    private final WalletService walletService;


    @PostMapping("/update")
    public ResponseMessage updateProfile(@Valid @RequestBody UserUpdate request) {

      User principal =  getLoggedInUser();
      request.setUser(principal);
      return userService.updateProfile(request);
    }

    @GetMapping("")
    public UserModel getUser(){
        return getLoggedInUser().toModel();
    }

    @PostMapping("/addAddress")
    public ResponseMessage addAddress(@Valid @RequestBody AddressDTO request) {
        User principal =  getLoggedInUser();
        return userService.addAddress(request,principal);
    }

    @PostMapping("/setPreferredAddress")
    public ResponseMessage setPreferredAddress(@RequestParam Long addressId) {
        User principal = getLoggedInUser();
        return userService.setPreferredAddress(principal.getId(), addressId);
    }

    @GetMapping("/getAllAddress")
    public Page<AddressModel> getAllAddress(@PageableDefault(sort = "id", direction = DESC) Pageable pageable){
        User principal =  getLoggedInUser();
        return userService.getAllAddresses(principal.getId(),pageable);
    }


    @GetMapping("/getAddress/{addressId}")
    public AddressModel getAddress(@PathVariable Long addressId){
        User principal =  getLoggedInUser();
        return userService.getAddressByAddressId(addressId,principal.getId());
    }

    @GetMapping("/amount")
    public ResponseMessage getWalletAmount(){
        User principal = getLoggedInUser();
        return walletService.getWalletAmount(principal);
    }

}
