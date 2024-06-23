package org.roadmap.weatherapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LocationDTO {

    private String name;

    private String region;

    private BigDecimal latitude;

    private BigDecimal longitude;

}
