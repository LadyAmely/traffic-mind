package org.shop.traffic.mapper;

import org.mapstruct.Mapper;
import org.shop.traffic.dto.response.VehicleResponse;
import org.shop.traffic.model.Vehicle;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    VehicleResponse toResponse(Vehicle vehicle);
}

