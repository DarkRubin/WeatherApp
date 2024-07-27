package org.roadmap.weatherapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.roadmap.weatherapp.dto.LocationForecast;
import org.roadmap.weatherapp.model.Location;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherService {

    private static final String URL = "https://api.openweathermap.org/data/2.5/weather?lang=en&units=metric&lat=%s&lon=%s&appid=%s";
    private final static String SECRET_KEY = "22120095a8780ea5616d67f0bab415e7";
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private String getJsonFromApi(BigDecimal latitude, BigDecimal longitude) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format(URL, latitude, longitude, SECRET_KEY)))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private LocationForecast deserializeJson(String json) throws JsonProcessingException {
        LocationForecast result = new LocationForecast();
        JsonNode jsonNode = objectMapper.readTree(json);

        JsonNode weather = jsonNode.get("weather");
        for (JsonNode node : weather) {
            weather = node;
            break;
        }
        JsonNode info = jsonNode.get("main");
        JsonNode sys = jsonNode.get("sys");


        result.setName(jsonNode.get("name").asText());
        result.setWeather(weather.get("description").asText());
        result.setCountry(sys.get("country").asText());
        result.setTemp(info.get("temp").asInt());

        return result;
    }

    public LocationForecast getForecastForNow(Location location) throws URISyntaxException, IOException, InterruptedException {
        String json = getJsonFromApi(location.getLatitude(), location.getLongitude());
        LocationForecast locationForecast = deserializeJson(json);
        locationForecast.setLatitude(location.getLatitude());
        locationForecast.setLongitude(location.getLongitude());
        return locationForecast;
    }

}
