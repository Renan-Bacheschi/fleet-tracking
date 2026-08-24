package io.github.renanbacheschi.fleettracking.vehicle.dto;

import io.github.renanbacheschi.fleettracking.vehicle.VehicleStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateVehicleStatusRequest(
        @NotNull VehicleStatus status
) {
}
