package org.roadmap.weatherapp.service;

import org.roadmap.weatherapp.dto.LocationForecast;
import org.roadmap.weatherapp.exception.ApiNotResponseException;
import org.roadmap.weatherapp.model.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeatherService {

    private final ApiService apiService = new ApiService();

    public LocationForecast getForecast(Location location) throws ApiNotResponseException {
        try {
            LocationForecast locationForecast = apiService
                    .getLocationForecast(location.getLatitude(), location.getLongitude());
            locationForecast.setId(location.getId());
            locationForecast.setName(location.getName());
            return locationForecast;
        } catch (IOException | InterruptedException e) {
            throw new ApiNotResponseException();
        }
    }

    public List<LocationForecast> getForecasts(List<Location> locations) throws ApiNotResponseException {
        List<LocationForecast> locationForecasts = new ArrayList<>();
        for (Location location : locations) {
            locationForecasts.add(getForecast(location));
        }
        return locationForecasts;
    }



}
