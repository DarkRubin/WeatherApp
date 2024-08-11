package org.roadmap.weatherapp.service;

import org.roadmap.weatherapp.dao.LocationDao;
import org.roadmap.weatherapp.dto.LocationDTO;
import org.roadmap.weatherapp.exception.ApiNotResponseException;
import org.roadmap.weatherapp.mapper.LocationMapperImpl;
import org.roadmap.weatherapp.model.Location;
import org.roadmap.weatherapp.model.User;

import java.io.IOException;
import java.util.List;

public class LocationService {

    private final LocationDao dao = new LocationDao();
    private final ApiService service = new ApiService();
    private final LocationMapperImpl mapper = new LocationMapperImpl();

    public LocationDTO searchByCoordinates(String latitude, String longitude, String toSearch)
            throws ApiNotResponseException {
        try {
            List<LocationDTO> variants = service.getLocationByCoordinates(latitude, longitude);
            LocationDTO result = variants.stream()
                    .filter(location -> location.getName().equalsIgnoreCase(toSearch))
                    .findFirst().orElseGet(() -> variants.get(0));
            result.setName(toSearch);
            return result;
        } catch (IOException | InterruptedException e) {
            throw new ApiNotResponseException();
        }
    }

    public List<LocationDTO> searchAllByName(String name) throws ApiNotResponseException {
        try {
            return service.getLocationByName(name);
        } catch (IOException | InterruptedException e) {
            throw new ApiNotResponseException();
        }
    }

    public void addLocationToUser(User user, LocationDTO locationToAdd) {
        Location location = mapper.fromDTO(locationToAdd, user);
        dao.save(location);
    }
}
