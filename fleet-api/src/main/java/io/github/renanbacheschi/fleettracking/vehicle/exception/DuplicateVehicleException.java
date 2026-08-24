package io.github.renanbacheschi.fleettracking.vehicle.exception;

public class DuplicateVehicleException extends RuntimeException {

    public DuplicateVehicleException(String field, String value) {
        super("Vehicle with %s '%s' already exists".formatted(field, value));
    }
}
