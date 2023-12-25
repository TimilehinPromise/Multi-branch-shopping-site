package com.valuemart.shop.domain.service.concretes;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.LoyaltyModel;
import com.valuemart.shop.domain.models.dto.LoyaltyDTO;
import com.valuemart.shop.domain.service.abstracts.LoyaltyService;
import com.valuemart.shop.persistence.entity.Loyalty;
import com.valuemart.shop.persistence.repository.LoyaltyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoyaltyServiceImpl implements LoyaltyService {

    private final LoyaltyRepository loyaltyRepository;

    @Override
    public ResponseMessage updateLoyaltyFormula(LoyaltyDTO dto){
        Loyalty existingLoyalty = loyaltyRepository.findById(1L).get();

        existingLoyalty.setCoinNo(dto.getCoinNo());
        existingLoyalty.setDiscountValue(dto.getDiscountValue());
        existingLoyalty.setRequiredAmount(dto.getRequiredAmount());

        loyaltyRepository.save(existingLoyalty);

        return ResponseMessage.builder().message("Loyalty formula set successfully").build();
    }

    @Override
    public LoyaltyModel getLoyaltyFormula(){
       return loyaltyRepository.findById(1L).map(Loyalty::toModel).get();
    }


}
