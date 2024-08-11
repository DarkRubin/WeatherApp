package org.roadmap.weatherapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationForecast {

    private Integer id;

    private String name;

    private List<Weather> weather = new ArrayList<>();

    @JsonSetter("sys")
    private Info info = new Info();

    @JsonSetter("main")
    private Main main = new Main();

    @JsonSetter("coord")
    private Coordinates coordinates = new Coordinates();

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Info {

        private String country;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {

        private String description;

        private String icon;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {

        private int temp;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Coordinates {

        @JsonSetter("lat")
        private BigDecimal latitude;

        @JsonSetter("lon")
        private BigDecimal longitude;

    }

}


