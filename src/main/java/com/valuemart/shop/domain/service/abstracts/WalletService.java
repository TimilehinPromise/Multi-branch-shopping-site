package com.valuemart.shop.domain.service.abstracts;

import com.valuemart.shop.persistence.entity.User;
import com.valuemart.shop.persistence.entity.Wallet;

import java.math.BigDecimal;

public interface WalletService {

    Wallet getOrCreateCoinWalletIfNotExist(User user);

    Wallet getWallet (User user);

    boolean meetCriteria(User user);

    void addToWallet(Wallet wallet);
}
