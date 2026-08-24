package io.github.renanbacheschi.fleettracking.vehicle.dto;

import io.github.renanbacheschi.fleettracking.vehicle.VehicleStatus;
import io.github.renanbacheschi.fleettracking.vehicle.VehicleType;

import java.time.Instant;
import java.util.UUID;

public record VehicleResponse(
        UUID id,
        String licensePlate,
        String fleetCode,
        String brand,
        String model,
        Integer modelYear,
        VehicleType type,
        VehicleStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
