package com.valuemart.shop.domain.service.concretes;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.dto.AdminLogisticDto;
import com.valuemart.shop.domain.models.dto.LogisticsDto;
import com.valuemart.shop.domain.service.abstracts.DeliveryService;
import com.valuemart.shop.exception.DeliveryAreaNotFoundException;
import com.valuemart.shop.persistence.entity.DeliveryArea;
import com.valuemart.shop.persistence.repository.DeliveryAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    private DeliveryAreaRepository deliveryAreaRepository;

    @Override
    public BigDecimal getDeliveryPriceByArea(String areaName) {
        System.out.println("service by area");
        System.out.println(areaName);
        System.out.println(deliveryAreaRepository.findByAreaName(areaName));
        return deliveryAreaRepository.findByAreaName(areaName)
                .map(DeliveryArea::getDeliveryPrice)
                .orElseThrow(() -> new DeliveryAreaNotFoundException("We can't deliver to this location"));
    }

    @Override
    public ResponseMessage addOrUpdateDeliveryAreas(AdminLogisticDto adminLogisticDto) {
        List<DeliveryArea> updatedAreas = new ArrayList<>();
        for (LogisticsDto dto : adminLogisticDto.getData()) {
            DeliveryArea area = deliveryAreaRepository.findByAreaName(dto.getAreaName())
                    .orElse(new DeliveryArea());
            area.setAreaName(dto.getAreaName());
            area.setDeliveryPrice(dto.getDeliveryPrice());
            updatedAreas.add(deliveryAreaRepository.save(area));
        }
        return ResponseMessage.builder().message( "Locations updated").build();
    }
}
