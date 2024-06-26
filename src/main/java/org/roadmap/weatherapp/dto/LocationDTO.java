package org.roadmap.weatherapp.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LocationDTO {

    private String name;

    private String region;

    private String country;

    @JsonSetter("lat")
    private BigDecimal latitude;

    @JsonSetter("lon")
    private BigDecimal longitude;

}
