package service;

import org.junit.jupiter.api.Test;
import org.roadmap.weatherapp.dto.LocationDTO;
import org.roadmap.weatherapp.dto.LocationForecast;
import org.roadmap.weatherapp.exception.ApiNotResponseException;
import org.roadmap.weatherapp.model.Location;
import org.roadmap.weatherapp.model.User;
import org.roadmap.weatherapp.service.ApiService;
import org.roadmap.weatherapp.service.WeatherService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

class ApiTest {

     static final String MOCK_RESPONSE = """
             
             """;
     static final String CITY_FOR_TEST = "New+York";
     static final Location TEST_LOCATION = new Location("Прибузька сільська громада",
            new User("name", "password"),
            new BigDecimal("47.8671228"), new BigDecimal("31.0179572"));

    @Test
    void responseConvertingTest() throws IOException, InterruptedException {
        ApiService apiService = new ApiService();
        List<LocationDTO> location = apiService.getLocationByName(CITY_FOR_TEST);
        location.forEach(System.out::println);
    }

    @Test
    void test() throws IOException, InterruptedException {
        ApiService apiService = new ApiService();
        List<LocationDTO> locationByCoordinates = apiService.getLocationByCoordinates("47.8671228", "31.0179572");
        locationByCoordinates.forEach(System.out::println);
    }

    @Test
    void getWeatherForecastTest() throws ApiNotResponseException {
        WeatherService weatherService = new WeatherService();
        LocationForecast forecast = weatherService.getForecast(TEST_LOCATION);
        System.out.println(forecast);
        System.out.println();
    }

}
