package org.roadmap.weatherapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.roadmap.weatherapp.dto.LocationDTO;
import org.roadmap.weatherapp.model.Location;
import org.roadmap.weatherapp.model.User;

@Mapper
public interface LocationMapper {

//    default Location fromDTO(LocationDTO locationDTO, User user) {
//        BigDecimal latitude = new BigDecimal(locationDTO.getLatitude());
//        BigDecimal longitude = new BigDecimal(locationDTO.getLongitude());
//        return new Location(
//                locationDTO.getName(),
//                user,
//                latitude,
//                longitude
//        );
//    }

    @Mapping(target = "latitude", expression = "java(new java.math.BigDecimal(locationDTO.getLatitude()))")
    @Mapping(target = "longitude", expression = "java(new java.math.BigDecimal(locationDTO.getLongitude()))")
    @Mapping(target = "user", expression = "java(user)")
    @Mapping(target = "id", ignore = true)
    Location fromDTO(LocationDTO locationDTO, User user);


}
