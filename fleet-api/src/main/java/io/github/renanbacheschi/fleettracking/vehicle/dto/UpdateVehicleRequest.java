package io.github.renanbacheschi.fleettracking.vehicle.dto;

import io.github.renanbacheschi.fleettracking.vehicle.VehicleType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Year;

public record UpdateVehicleRequest(
        @NotBlank
        @Pattern(
                regexp = "(?i)^\\s*[A-Z]{3}[\\s-]?(?:\\d{4}|\\d[A-Z]\\d{2})\\s*$",
                message = "must be a valid Brazilian license plate"
        )
        String licensePlate,

        @NotBlank
        @Size(max = 50)
        String fleetCode,

        @NotBlank
        @Size(max = 100)
        String brand,

        @NotBlank
        @Size(max = 100)
        String model,

        @NotNull
        @Min(1900)
        Integer modelYear,

        @NotNull
        VehicleType type
) {

    @AssertTrue(message = "modelYear must not be later than next year")
    public boolean isModelYearWithinAllowedRange() {
        return modelYear == null || modelYear <= Year.now().getValue() + 1;
    }
}
