package org.shop.traffic.unit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shop.traffic.dto.request.CreateIntersectionRequest;
import org.shop.traffic.dto.response.IntersectionResponse;
import org.shop.traffic.mapper.IntersectionMapper;
import org.shop.traffic.model.Intersection;
import org.shop.traffic.repository.IntersectionRepository;
import org.shop.traffic.service.IntersectionService;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IntersectionServiceTest {

    private static final UUID ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final String NAME = "Liberty Circle";
    private static final String LOCATION = "USA";
    private static final double LATITUDE = 50.065;
    private static final double LONGITUDE = 19.945;

    @Mock
    private IntersectionRepository intersectionRepository;

    @Mock
    private IntersectionMapper intersectionMapper;

    @InjectMocks
    private IntersectionService intersectionService;

    @Test
    @DisplayName("should create traffic intersection successfully")
    void shouldCreateTrafficIntersection() {
        // given
        CreateIntersectionRequest request = applyBaseData(CreateIntersectionRequest.builder()).build();
        Intersection entity = applyBaseData(Intersection.builder()).build();
        Intersection savedEntity = applyBaseData(Intersection.builder().id(ID)).build();
        IntersectionResponse expectedResponse = applyBaseData(IntersectionResponse.builder().id(ID)).build();

        when(intersectionRepository.save(any(Intersection.class))).thenReturn(savedEntity);
        when(intersectionMapper.toResponse(savedEntity)).thenReturn(expectedResponse);

        // when
        IntersectionResponse result = intersectionService.createTrafficIntersection(request);

        // then
        assertEquals(expectedResponse, result);
        verify(intersectionRepository).save(any(Intersection.class));
        verify(intersectionMapper).toResponse(savedEntity);
    }

    private CreateIntersectionRequest.CreateIntersectionRequestBuilder applyBaseData(CreateIntersectionRequest.CreateIntersectionRequestBuilder builder) {
        return builder
                .name(NAME)
                .location(LOCATION)
                .latitude(BigDecimal.valueOf(LATITUDE))
                .longitude(BigDecimal.valueOf(LONGITUDE));
    }

    private Intersection.IntersectionBuilder applyBaseData(Intersection.IntersectionBuilder builder) {
        return builder
                .name(NAME)
                .location(LOCATION)
                .latitude(BigDecimal.valueOf(LATITUDE))
                .longitude(BigDecimal.valueOf(LONGITUDE));
    }

    private IntersectionResponse.IntersectionResponseBuilder applyBaseData(IntersectionResponse.IntersectionResponseBuilder builder) {
        return builder
                .name(NAME)
                .location(LOCATION)
                .latitude(BigDecimal.valueOf(LATITUDE))
                .longitude(BigDecimal.valueOf(LONGITUDE));
    }
}
