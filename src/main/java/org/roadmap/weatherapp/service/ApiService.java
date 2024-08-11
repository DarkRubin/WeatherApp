package org.roadmap.weatherapp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.roadmap.weatherapp.dto.LocationDTO;
import org.roadmap.weatherapp.dto.LocationForecast;
import org.roadmap.weatherapp.exception.PropertiesFileNotFoundException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class ApiService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Properties properties = new Properties();
    private final String appid;

    public ApiService() {
        InputStream stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("api.properties");
        try (stream) {
            properties.load(stream);
            this.appid = properties.getProperty("key");
        } catch (IOException e) {
            throw new PropertiesFileNotFoundException();
        }
    }

    private String sendGetRequest(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public List<LocationDTO> getLocationByName(String cityName) throws IOException, InterruptedException {
        String url = String.format(properties.getProperty("url_for_name_search"), cityName, appid);
        String responseBody = sendGetRequest(url);
        return objectMapper.readValue(responseBody, new TypeReference<>() {});
    }

    public List<LocationDTO> getLocationByCoordinates(String latitude, String longitude)
            throws IOException, InterruptedException {
        String url = String.format(properties.getProperty("url_for_coord_search"), latitude, longitude, appid);
        String responseBody = sendGetRequest(url);
        return objectMapper.readValue(responseBody, new TypeReference<>() {});
    }

    public LocationForecast getLocationForecast(BigDecimal latitude, BigDecimal longitude)
            throws IOException, InterruptedException {
        String url = String.format(properties.getProperty("url_for_forecast"), latitude, longitude, appid);
        String responseBody = sendGetRequest(url);
        return objectMapper.readValue(responseBody, new TypeReference<>() {});
    }
}
