package org.roadmap.weatherapp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.roadmap.weatherapp.dao.LocationDao;
import org.roadmap.weatherapp.dto.LocationDTO;
import org.roadmap.weatherapp.mapper.LocationMapperImpl;
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

    private static final String SECRET_KEY = "22120095a8780ea5616d67f0bab415e7";
    private static final String URL = "https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=5&appid=%s";
    private static final String URL_FOR_REVERS_SEARCH = "https://api.openweathermap.org/geo/1.0/reverse?lat=%s&lon=%s&limit=5&appid=%s";

    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final LocationDao dao = new LocationDao();
    private final LocationMapperImpl mapper = new LocationMapperImpl();


    public List<LocationDTO> searchByName(String cityName)
            throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format(URL, cityName, SECRET_KEY)))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), new TypeReference<>() {});
    }

    public List<LocationDTO> searchByCoordinates(String latitude, String longitude)
            throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(String.format(URL_FOR_REVERS_SEARCH, latitude, longitude, SECRET_KEY)))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), new TypeReference<>() {});
    }

    public LocationDTO chooseLocationToAdd(List<LocationDTO> variants, String toSearch) {
        for (LocationDTO variant : variants) {
            if (variant.getName().equals(toSearch)) {
                return variant;
            }
        }
        return variants.get(0);
    }

    public void addLocationToUser(User user, LocationDTO locationToAdd) {
        Location location = mapper.fromDTO(locationToAdd, user);
        dao.save(location);
    }

    public void deleteLocation(String latitude, String  longitude, User user) {
        dao.delete(latitude, longitude, user);
    }


}
