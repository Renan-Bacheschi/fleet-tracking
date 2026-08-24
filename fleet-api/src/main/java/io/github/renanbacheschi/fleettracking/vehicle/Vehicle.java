package io.github.renanbacheschi.fleettracking.vehicle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "vehicles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vehicle {

    @Id
    private UUID id;

    @Column(name = "license_plate", nullable = false, unique = true, length = 7)
    private String licensePlate;

    @Column(name = "fleet_code", nullable = false, unique = true, length = 50)
    private String fleetCode;

    @Column(nullable = false, length = 100)
    private String brand;

    @Column(nullable = false, length = 100)
    private String model;

    @Column(name = "model_year", nullable = false)
    private Integer modelYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VehicleType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VehicleStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    static Vehicle cadastrar(
            String licensePlate,
            String fleetCode,
            String brand,
            String model,
            Integer modelYear,
            VehicleType type
    ) {
        Vehicle vehicle = new Vehicle();
        vehicle.id = UUID.randomUUID();
        vehicle.licensePlate = licensePlate;
        vehicle.fleetCode = fleetCode;
        vehicle.brand = brand;
        vehicle.model = model;
        vehicle.modelYear = modelYear;
        vehicle.type = type;
        vehicle.status = VehicleStatus.ACTIVE;
        Instant now = Instant.now();
        vehicle.createdAt = now;
        vehicle.updatedAt = now;
        return vehicle;
    }

    void atualizar(
            String licensePlate,
            String fleetCode,
            String brand,
            String model,
            Integer modelYear,
            VehicleType type
    ) {
        this.licensePlate = licensePlate;
        this.fleetCode = fleetCode;
        this.brand = brand;
        this.model = model;
        this.modelYear = modelYear;
        this.type = type;
        this.updatedAt = Instant.now();
    }

    void alterarStatus(VehicleStatus status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }

    @PrePersist
    void prepararPersistencia() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (updatedAt == null) {
            updatedAt = createdAt;
        }
    }

    @PreUpdate
    void prepararAtualizacao() {
        updatedAt = Instant.now();
    }
}
