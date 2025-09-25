package org.shop.traffic.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    private final Map<Direction, Set<Direction>> conflictMap = Map.of(
            Direction.NORTH, Set.of(Direction.EAST, Direction.WEST),
            Direction.SOUTH, Set.of(Direction.EAST, Direction.WEST),
            Direction.EAST,  Set.of(Direction.NORTH, Direction.SOUTH),
            Direction.WEST,  Set.of(Direction.NORTH, Direction.SOUTH)
    );

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
    public CycleResponse updateTrafficLightsCycle(CycleRequest request, Map<Direction, Map<Integer, Integer>> laneData) {
        CycleData cycleData = initCycleData();
        String strategy = request.forceUpdate() ? "manual override" : "lane-adaptive";

        Direction selectedDirection = request.forceUpdate()
                ? request.priorityDirection()
                : getMostCongestedDirection(laneData);

        for (Direction dir : Direction.values()) {
            boolean isGreen = dir == selectedDirection;
            if (isGreen && isConflict(dir, cycleData.signals())) {
                isGreen = false;
            }
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

    /**
     * Simulates traffic based on a list of commands.
     * Supports adding vehicles, traffic light cycle steps, and the yellow phase.
     */
    @Transactional
    public SimulationResponse runSimulation(SimulationRequest request) {
            List<StepStatus> stepStatuses = new ArrayList<>();
            resetRoadQueues();

            for (Command command : request.commands()) {
                    logCommand(command);
                if (isAddVehicle(command)) {
                    addVehicleToQueue(command);
                } else if (isStep(command)) {
                    List<String> leftVehicles = processGreenPhase();
                    insertYellowPhase(currentGreenLight);
                    currentGreenLight = nextDirection(currentGreenLight);
                    stepStatuses.add(new StepStatus(leftVehicles));
                }
            }
            return new SimulationResponse(stepStatuses);
        }

    /** Checks if the command is about adding a vehicle. */
    private boolean isAddVehicle(Command command) {
        return "addVehicle".equalsIgnoreCase(command.type());
    }

    /** Checks if the command applies to a simulation step. */
    private boolean isStep(Command command) {
        return "step".equalsIgnoreCase(command.type());
    }

    /** Writes a command to the simulation log repository. */
    private void logCommand(Command command) {
        commandLogRepository.save(CommandLog.builder()
                .commandType(command.type())
                .vehicleId(command.vehicleId())
                .startRoad(command.startRoad())
                .endRoad(command.endRoad())
                .build());
    }

    /** Creates a new vehicle and adds it to the queue on the specified road. */
    private void addVehicleToQueue(Command command) {
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
    }

    /** Lets the first vehicle from the currently green direction through and returns its ID. */
    private List<String> processGreenPhase() {
        List<String> leftVehicles = new ArrayList<>();
        Queue<Vehicle> queue = roadQueues.get(currentGreenLight);

        if (!queue.isEmpty()) {
            Vehicle vehicle = queue.poll();
            vehicle.setHasPassed(true);
            vehicleRepository.save(vehicle);
            leftVehicles.add(vehicle.getId().toString());
        }

        return leftVehicles;
    }

    /** resetting queues **/
    private void resetRoadQueues() {
        for (Direction dir : Direction.values()) {
            roadQueues.put(dir, new LinkedList<>());
        }
    }

    /**
     * Retrieves an existing traffic intersection by its ID and maps it to a response DTO.
     */
    public IntersectionResponse getTrafficIntersection(UUID id){
        var entity = intersectionRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Intersection with id: " + id + "not found."));
        return intersectionMapper.toResponse(entity);
    }

    /**
     * Returns the next direction in the enum order, cyclically moving to the beginning.
     */
    private Direction nextDirection(Direction current) {
        Direction[] directions = Direction.values();
        int nextIndex = (current.ordinal() + 1) % directions.length;
        return directions[nextIndex];
    }

    /**
     * Returns the direction with the highest total number of vehicles in all its lanes.
     */
    private Direction getMostCongestedDirection(Map<Direction, Map<Integer, Integer>> laneData) {
        return laneData.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().values().stream().mapToInt(Integer::intValue).sum()
                ))
                .entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(Direction.NORTH);
    }

    /**  Ensure safety by avoiding conflict situations **/
    private boolean isConflict(Direction selected, Map<Direction, Light> currentSignals) {
        Set<Direction> conflicts = conflictMap.getOrDefault(selected, Set.of());
        for (Direction dir : conflicts) {
            if (currentSignals.getOrDefault(dir, Light.RED) == Light.GREEN) {
                return true;
            }
        }
        return false;
    }

    /** Sets a yellow light for directions that were previously green. */
    private void insertYellowPhase(Direction direction) {
        CycleData yellowCycleData = initCycleData();
        yellowCycleData.signals().put(direction, Light.YELLOW);
        yellowCycleData.greenDurations().put(direction, 0);
        yellowCycleData.redDurations().put(direction, 0);
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
