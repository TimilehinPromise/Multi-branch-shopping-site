package com.valuemart.shop.domain.service.concretes;

import com.valuemart.shop.domain.ResponseMessage;
import com.valuemart.shop.domain.models.ThresholdModel;
import com.valuemart.shop.domain.models.dto.ThresholdDTO;
import com.valuemart.shop.domain.service.abstracts.ThresholdService;
import com.valuemart.shop.persistence.entity.Threshold;
import com.valuemart.shop.persistence.repository.ThresholdRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
public class ThresholdServiceImpl implements ThresholdService {

    private final ThresholdRepository thresholdRepository;

    public ThresholdServiceImpl(ThresholdRepository thresholdRepository) {
        this.thresholdRepository = thresholdRepository;
    }

    @Transactional
    @Override
    public ResponseMessage addOrUpdateThreshold(ThresholdModel model) {
        List<ThresholdDTO> dtos = model.getData();

        for (ThresholdDTO dto : dtos) {
            // Check if an entry with the same value already exists
            Optional<Threshold> existingThreshold = thresholdRepository.findByValue(dto.getValue());

            if (existingThreshold.isPresent()) {
                // Update existing entry
                Threshold threshold = existingThreshold.get();
                threshold.setMonetaryAmount(dto.getMonetaryAmount());
                thresholdRepository.save(threshold);
            } else {
                // Create a new entry
                Threshold threshold = Threshold.builder()
                        .value(dto.getValue())
                        .monetaryAmount(dto.getMonetaryAmount())
                        .build();
                thresholdRepository.save(threshold);
            }
        }

        return ResponseMessage.builder().message("Threshold updated").build();
    }

    @Override
    public ThresholdDTO getThresholdByValueOrNearestBelow(BigDecimal value) {
        List<Threshold> thresholds = thresholdRepository.findTopByValueLessThanEqualOrderByValueDesc(value);
        if (!thresholds.isEmpty()) {
            Threshold nearestThreshold = thresholds.get(0);
            return convertToDTO(nearestThreshold);
        }
        return null;
    }

    @Override
    public List<Threshold> getAllThreshold(){
        return thresholdRepository.findAll();
    }

    private ThresholdDTO convertToDTO(Threshold threshold) {
        ThresholdDTO dto = new ThresholdDTO();
        dto.setId(threshold.getId());
        dto.setValue(threshold.getValue());
        dto.setMonetaryAmount(threshold.getMonetaryAmount());
        return dto;
    }



}
