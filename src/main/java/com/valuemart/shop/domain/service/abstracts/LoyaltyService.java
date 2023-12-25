package com.valuemart.shop.domain.service.abstracts;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.LoyaltyModel;
import com.valuemart.shop.domain.models.dto.LoyaltyDTO;

public interface LoyaltyService {
    ResponseMessage updateLoyaltyFormula(LoyaltyDTO dto);

    LoyaltyModel getLoyaltyFormula();
}
