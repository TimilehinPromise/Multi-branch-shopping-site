package com.valuemart.shop.domain.service.abstracts;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.DeliveryModel;
import com.valuemart.shop.domain.models.dto.AdminLogisticDto;
import com.valuemart.shop.persistence.entity.DeliveryArea;

import java.math.BigDecimal;
import java.util.List;

public interface DeliveryService {

    BigDecimal getDeliveryPriceByArea(String areaName);

    ResponseMessage addOrUpdateDeliveryAreas(AdminLogisticDto adminLogisticDto);

    List<DeliveryModel> getAll();
}
