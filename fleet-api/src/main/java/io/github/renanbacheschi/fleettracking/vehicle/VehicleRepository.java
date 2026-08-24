package io.github.renanbacheschi.fleettracking.vehicle;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    boolean existsByLicensePlate(String licensePlate);

    boolean existsByFleetCode(String fleetCode);

    boolean existsByLicensePlateAndIdNot(String licensePlate, UUID id);

    boolean existsByFleetCodeAndIdNot(String fleetCode, UUID id);
}
