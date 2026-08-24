package io.github.renanbacheschi.fleettracking.vehicle.exception;

import java.util.UUID;

public class VehicleNotFoundException extends RuntimeException {

    public VehicleNotFoundException(UUID id) {
        super("Vehicle with id %s was not found".formatted(id));
    }
}
