package com.valuemart.shop.domain.models;

import com.valuemart.shop.domain.models.dto.ThresholdDTO;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class ThresholdModel {

    List<ThresholdDTO> data;
}
