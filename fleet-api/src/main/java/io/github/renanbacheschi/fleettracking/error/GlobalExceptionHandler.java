package io.github.renanbacheschi.fleettracking.error;

import io.github.renanbacheschi.fleettracking.vehicle.exception.DuplicateVehicleException;
import io.github.renanbacheschi.fleettracking.vehicle.exception.VehicleNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VehicleNotFoundException.class)
    ProblemDetail handleVehicleNotFound(VehicleNotFoundException exception) {
        return problem(HttpStatus.NOT_FOUND, "Vehicle not found", exception.getMessage());
    }

    @ExceptionHandler(DuplicateVehicleException.class)
    ProblemDetail handleDuplicateVehicle(DuplicateVehicleException exception) {
        return problem(HttpStatus.CONFLICT, "Duplicate vehicle", exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleValidation(MethodArgumentNotValidException exception) {
        Map<String, List<String>> errors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.computeIfAbsent(error.getField(), ignored -> new java.util.ArrayList<>())
                        .add(error.getDefaultMessage())
        );
        exception.getBindingResult().getGlobalErrors().forEach(error ->
                errors.computeIfAbsent(error.getObjectName(), ignored -> new java.util.ArrayList<>())
                        .add(error.getDefaultMessage())
        );

        ProblemDetail detail = problem(
                HttpStatus.BAD_REQUEST,
                "Invalid request",
                "One or more request fields are invalid"
        );
        detail.setProperty("errors", errors);
        return detail;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ProblemDetail handleUnreadableMessage(HttpMessageNotReadableException exception) {
        return problem(HttpStatus.BAD_REQUEST, "Invalid request", "Malformed JSON or invalid field value");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail handleIllegalArgument(IllegalArgumentException exception) {
        return problem(HttpStatus.BAD_REQUEST, "Invalid request", exception.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        return problem(
                HttpStatus.CONFLICT,
                "Duplicate vehicle",
                "A vehicle with the same license plate or fleet code already exists"
        );
    }

    private ProblemDetail problem(HttpStatus status, String title, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        return problemDetail;
    }
}
