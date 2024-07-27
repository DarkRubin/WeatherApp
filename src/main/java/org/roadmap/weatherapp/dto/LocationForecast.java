package org.roadmap.weatherapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LocationForecast {

    private String name;

    private String country;

    private String weather;

    private int temp;

    private BigDecimal latitude;

    private BigDecimal longitude;

}
