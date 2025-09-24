package org.shop.traffic.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.shop.traffic.agent.TrafficSignalDQNAgent;
import org.shop.traffic.dto.request.*;
import org.shop.traffic.dto.response.CycleResponse;
import org.shop.traffic.dto.response.IntersectionResponse;
import org.shop.traffic.dto.response.SimulationResponse;
import org.shop.traffic.dto.response.VehicleResponse;
import org.shop.traffic.mapper.IntersectionMapper;
import org.shop.traffic.mapper.VehicleMapper;
import org.shop.traffic.model.*;
import org.shop.traffic.repository.CommandLogRepository;
import org.shop.traffic.repository.IntersectionRepository;
import org.shop.traffic.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TrafficSimulationService {

    private final IntersectionRepository intersectionRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final IntersectionMapper intersectionMapper;
    private final CommandLogRepository commandLogRepository;

    private final Map<Direction, Queue<Vehicle>> roadQueues = new EnumMap<>(Direction.class);
    private Direction currentGreenLight = Direction.NORTH;
    private static final int DEFAULT_SIGNAL_DURATION = 20;
    private static final int ZERO_GREEN_DURATION = 0;

    @Autowired
    private TrafficSignalDQNAgent agent;

    /** Adding a vehicle to a specific road **/
    @Transactional
    public VehicleResponse addVehicle(VehicleRequest request, UUID id){
        var intersection = intersectionRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Intersection with id: " + id + "not found."));

        var entity = Vehicle.builder()
                .id(intersection.getId())
                .vehicleType(request.vehicleType())
                .speed(request.speed())
                .length(request.length())
                .arrivalTime(request.arrivalTime())
                .priority(request.priority())
                .turn(request.intendedTurn())
                .build();

        var saved = vehicleRepository.save(entity);
        return vehicleMapper.toResponse(saved);
    }

    /**
     * Creates a new intersection based on the request data and returns its DTO representation.
     */
    @Transactional
    public IntersectionResponse createTrafficIntersection(CreateIntersectionRequest request){

        var entity = Intersection.builder()
                .name(request.name())
                .location(request.location())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .build();
        var saved = intersectionRepository.save(entity);
        return intersectionMapper.toResponse(saved);
    }

    /** Simulates standard traffic light cycles **/
    @Transactional
    public CycleResponse updateTrafficLightsCycle(CycleRequest request){
        CycleData cycleData = initCycleData();
        String strategy;

        strategy = request.forceUpdate() ? "manual override" : "adaptive";
        Direction greenDirection = request.forceUpdate()
                ? request.priorityDirection()
                : getBestDirection(request.priorityDirection());

        for (Direction dir : Direction.values()) {
            boolean isGreen = dir == greenDirection;
            cycleData.signals().put(dir, isGreen ? Light.GREEN : Light.RED);
            cycleData.greenDurations().put(dir, isGreen ? DEFAULT_SIGNAL_DURATION : ZERO_GREEN_DURATION);
            cycleData.redDurations().put(dir, isGreen ? ZERO_GREEN_DURATION : DEFAULT_SIGNAL_DURATION);
        }

        return buildCycle(
                cycleData.signals(),
                cycleData.greenDurations(),
                cycleData.redDurations(),
                strategy
        );
    }

    @Transactional
    public SimulationResponse runSimulation(SimulationRequest request) {
            List<StepStatus> stepStatuses = new ArrayList<>();

            for (Direction dir : Direction.values()) {
                roadQueues.put(dir, new LinkedList<>());
            }

            for (Command command : request.commands()) {
                commandLogRepository.save(CommandLog.builder()
                        .commandType(command.type())
                        .vehicleId(command.vehicleId())
                        .startRoad(command.startRoad())
                        .endRoad(command.endRoad())
                        .build());

                if ("addVehicle".equalsIgnoreCase(command.type())) {

                    Vehicle vehicle = Vehicle.builder()
                            .vehicleType(VehicleType.CAR)
                            .arrivalTime(LocalDateTime.now())
                            .hasPassed(false)
                            .priority(0)
                            .waitingTime(0f)
                            .turn(Turn.STRAIGHT)
                            .lane(null)
                            .build();

                    vehicleRepository.save(vehicle);
                    roadQueues.get(command.startRoad()).add(vehicle);

                } else if ("step".equalsIgnoreCase(command.type())) {
                    List<String> leftVehicles = new ArrayList<>();

                    Queue<Vehicle> queue = roadQueues.get(currentGreenLight);
                    if (!queue.isEmpty()) {
                        Vehicle vehicle = queue.poll();
                        vehicle.setHasPassed(true);
                        vehicleRepository.save(vehicle);

                        leftVehicles.add(vehicle.getId().toString());
                    }

                    currentGreenLight = nextDirection(currentGreenLight);
                    stepStatuses.add(new StepStatus(leftVehicles));
                }
            }
            return new SimulationResponse(stepStatuses);
        }

    /**
     * Retrieves an existing traffic intersection by its ID and maps it to a response DTO.
     */
    public IntersectionResponse getTrafficIntersection(UUID id){
        var entity = intersectionRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Intersection with id: " + id + "not found."));
        return intersectionMapper.toResponse(entity);
    }

    private Direction nextDirection(Direction current) {
        Direction[] directions = Direction.values();
        int nextIndex = (current.ordinal() + 1) % directions.length;
        return directions[nextIndex];
    }

    /**
     * Returns the traffic direction selected by the agent based on the given priority.
     */
    private Direction getBestDirection(Direction priorityDirection) {;
        TrafficSignalDQNAgent.Decision decision = agent.decide(priorityDirection);
        return decision.direction();
    }

    /**
     * Builds a CycleResponse object containing updated traffic signals, green/red durations, and the strategy used.
     */
    private CycleResponse buildCycle(
            Map<Direction, Light> signals,
            Map<Direction, Integer> green,
            Map<Direction, Integer> red,
            String strategy){
        return CycleResponse.builder()
                .updatedSignals(signals)
                .greenDurations(green)
                .redDurations(red)
                .strategyUsed(strategy)
                .build();
    }

    /**
     * Initializes an empty CycleData object with maps for signals, green durations, and red durations.
     */
    private CycleData initCycleData(){
        return new CycleData(
                new EnumMap<>(Direction.class),
                new EnumMap<>(Direction.class),
                new EnumMap<>(Direction.class)
        );
    }
}
