package com.valuemart.shop.domain.service.concretes;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.UserModel;
import com.valuemart.shop.domain.service.abstracts.WalletService;
import com.valuemart.shop.exception.BadRequestException;
import com.valuemart.shop.persistence.entity.Loyalty;
import com.valuemart.shop.persistence.entity.User;
import com.valuemart.shop.persistence.entity.Wallet;
import com.valuemart.shop.persistence.repository.LoyaltyRepository;
import com.valuemart.shop.persistence.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final LoyaltyRepository loyaltyRepository;
    private final static BigDecimal THRESHOLD = BigDecimal.valueOf(500);


    @Override
    public Wallet getOrCreateCoinWalletIfNotExist(User user){
        Wallet wallet ;

     Optional<Wallet> existingWallet = walletRepository.findFirstByUserId(user.getId());
     if (existingWallet.isPresent()){
         return existingWallet.get();
     }
         wallet = Wallet.builder()
                .amount(BigDecimal.ZERO)
                 .count(0L)
                .user(user)
                .build();

       return walletRepository.save(wallet);

    }

    @Override
    public Wallet getWallet(User user){
        Optional<Wallet> existingWallet = walletRepository.findFirstByUserId(user.getId());
        if (existingWallet.isEmpty()){
            throw new BadRequestException("Wallet not found, contact admin");
        }
        return existingWallet.get();
    }

    @Override
    public ResponseMessage getWalletAmount(User user){
        Optional<Wallet> existingWallet = walletRepository.findFirstByUserId(user.getId());
        if (existingWallet.isEmpty()){
            throw new BadRequestException("Wallet not found, contact admin");
        }

        return ResponseMessage.builder().message(String.valueOf(existingWallet.get().getAmount())).build();

    }

    @Override
    public Wallet getWallet(UserModel user){
        System.out.println(user);
        Optional<Wallet> existingWallet = walletRepository.findFirstByUserId(user.getUserId());
        if (existingWallet.isEmpty()){
            throw new BadRequestException("Wallet not found, contact admin");
        }
        System.out.println(existingWallet);
        return existingWallet.get();
    }

    @Override
    public boolean meetCriteria(User user){
        Wallet wallet = getWallet(user);
        if (wallet.getAmount().doubleValue() >= THRESHOLD.doubleValue()){
            return true;
        }
        else return false;
    }

//    public void convertWalletCoinToDiscountAmount(Wallet wallet){
//      Loyalty loyalty = loyaltyRepository.findById(1L).get();
//
//      Long walletCoin = wallet.getCoinNo();
//
//
//    }

    @Override
    public void updateWallet(Wallet wallet){
        walletRepository.save(wallet);
    }

    @Override
    public void addToWallet(Wallet wallet){
        log.info(wallet.toString());
      Optional<Loyalty> loyalty = loyaltyRepository.findFirstByCount(wallet.getCount());
      if (loyalty.isPresent()){
//          return loyalty.get().getDiscountValue();
          wallet.setAmount(wallet.getAmount().add(loyalty.get().getDiscountValue()));
      }
    }
}
