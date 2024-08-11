package org.roadmap.weatherapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDTO {

    private String name;

    @JsonSetter("state")
    private String region;

    private String country;

    @JsonSetter("lat")
    private String latitude;

    @JsonSetter("lon")
    private String longitude;

}
