package org.roadmap.weatherapp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.roadmap.weatherapp.dao.LocationDao;
import org.roadmap.weatherapp.dto.LocationDTO;
import org.roadmap.weatherapp.model.Location;
import org.roadmap.weatherapp.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class LocationService {

    private final static String secretKey = "22120095a8780ea5616d67f0bab415e7";
    private final static String url = "https://api.openweathermap.org/geo/1.0/direct?q=%s&appid=%s";
    public final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final LocationDao dao = new LocationDao();

    public List<LocationDTO> searchLocation(String cityName) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format(url, cityName, secretKey)))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), new TypeReference<>(){});
    }

    public List<Location> getUserLocation(User user) {
        return dao.searchUserLocation(user);
    }

}
