package com.valuemart.shop.domain.service.abstracts;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.ThresholdModel;
import com.valuemart.shop.domain.models.dto.ThresholdDTO;
import com.valuemart.shop.persistence.entity.Threshold;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface ThresholdService {
    @Transactional
    ResponseMessage addOrUpdateThreshold(ThresholdModel model);

    ThresholdDTO getThresholdByValueOrNearestBelow(BigDecimal value);

    List<Threshold> getAllThreshold();
}
