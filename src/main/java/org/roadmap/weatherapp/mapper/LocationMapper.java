package org.roadmap.weatherapp.mapper;

import org.mapstruct.Mapper;
import org.roadmap.weatherapp.dto.LocationDTO;
import org.roadmap.weatherapp.model.Location;
import org.roadmap.weatherapp.model.User;

import java.math.BigDecimal;

@Mapper
public interface LocationMapper {

    default Location fromDTO(LocationDTO locationDTO, User user) {
        BigDecimal latitude = new BigDecimal(locationDTO.getLatitude());
        BigDecimal longitude = new BigDecimal(locationDTO.getLongitude());
        return new Location(
                locationDTO.getName(),
                user,
                latitude,
                longitude
        );
    }


}
