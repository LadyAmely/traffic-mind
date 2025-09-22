package org.shop.traffic.mapper;

import org.mapstruct.Mapper;
import org.shop.traffic.dto.response.IntersectionResponse;
import org.shop.traffic.model.Intersection;

@Mapper(componentModel = "spring")
public interface IntersectionMapper {
    IntersectionResponse toResponse(Intersection intersection);
}
