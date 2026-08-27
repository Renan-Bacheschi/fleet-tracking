package io.github.renanbacheschi.fleettracking.vehicle;

import io.github.renanbacheschi.fleettracking.vehicle.dto.CreateVehicleRequest;
import io.github.renanbacheschi.fleettracking.vehicle.dto.UpdateVehicleRequest;
import io.github.renanbacheschi.fleettracking.vehicle.dto.UpdateVehicleStatusRequest;
import io.github.renanbacheschi.fleettracking.vehicle.dto.VehicleResponse;
import io.github.renanbacheschi.fleettracking.vehicle.exception.DuplicateVehicleException;
import io.github.renanbacheschi.fleettracking.vehicle.exception.VehicleNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final TutorialService tutorialService;

    public VehicleService(VehicleRepository vehicleRepository, TutorialService tutorialService) {
        this.vehicleRepository = vehicleRepository;
        this.tutorialService = tutorialService;
    }

    @Transactional
    public VehicleResponse create(CreateVehicleRequest request) {
        String licensePlate = normalizeLicensePlate(request.licensePlate());
        String fleetCode = normalizeRequiredText(request.fleetCode());
        validateModelYear(request.modelYear());
        ensureUniqueForCreation(licensePlate, fleetCode);

        Vehicle vehicle = Vehicle.create(
                licensePlate,
                fleetCode,
                normalizeRequiredText(request.brand()),
                normalizeRequiredText(request.model()),
                request.modelYear(),
                request.type()
        );

        return toResponse(vehicleRepository.saveAndFlush(vehicle));
    }

    public List<VehicleResponse> findAll() {
        return vehicleRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public VehicleResponse findById(UUID id) {
        return toResponse(findVehicle(id));
    }

    @Transactional
    public VehicleResponse update(UUID id, UpdateVehicleRequest request) {
        Vehicle vehicle = findVehicle(id);
        String licensePlate = normalizeLicensePlate(request.licensePlate());
        String fleetCode = normalizeRequiredText(request.fleetCode());
        validateModelYear(request.modelYear());
        ensureUniqueForUpdate(id, licensePlate, fleetCode);

        vehicle.update(
                licensePlate,
                fleetCode,
                normalizeRequiredText(request.brand()),
                normalizeRequiredText(request.model()),
                request.modelYear(),
                request.type()
        );

        return toResponse(vehicleRepository.saveAndFlush(vehicle));
    }

    @Transactional
    public VehicleResponse updateStatus(UUID id, UpdateVehicleStatusRequest request) {
        Vehicle vehicle = findVehicle(id);
        vehicle.changeStatus(request.status());
        VehicleResponse response = toResponse(vehicleRepository.saveAndFlush(vehicle));

        if (request.status() == VehicleStatus.MAINTENANCE) {
            sendMaintenanceTutorialIfApplicable(vehicle);
        }

        return response;
    }

    private Vehicle findVehicle(UUID id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
    }

    private void ensureUniqueForCreation(String licensePlate, String fleetCode) {
        if (vehicleRepository.existsByLicensePlate(licensePlate)) {
            throw new DuplicateVehicleException("license plate", licensePlate);
        }
        if (vehicleRepository.existsByFleetCode(fleetCode)) {
            throw new DuplicateVehicleException("fleet code", fleetCode);
        }
    }

    private void ensureUniqueForUpdate(UUID id, String licensePlate, String fleetCode) {
        if (vehicleRepository.existsByLicensePlateAndIdNot(licensePlate, id)) {
            throw new DuplicateVehicleException("license plate", licensePlate);
        }
        if (vehicleRepository.existsByFleetCodeAndIdNot(fleetCode, id)) {
            throw new DuplicateVehicleException("fleet code", fleetCode);
        }
    }

    private String normalizeLicensePlate(String licensePlate) {
        return licensePlate.replaceAll("[-\\s]", "").toUpperCase(Locale.ROOT);
    }

    private String normalizeRequiredText(String value) {
        return value.trim();
    }

    private void validateModelYear(Integer modelYear) {
        int maximumYear = Year.now().getValue() + 1;
        if (modelYear == null || modelYear < 1900 || modelYear > maximumYear) {
            throw new IllegalArgumentException("modelYear must be between 1900 and " + maximumYear);
        }
    }

    private void sendMaintenanceTutorialIfApplicable(Vehicle vehicle) {
        if ("Zulaine".equalsIgnoreCase(vehicle.getBrand())
                && "75".equals(vehicle.getModel())) {
            tutorialService.requestTutorial("How to remove the pin from the Zulaine 75 coquilho");
        }
    }

    private VehicleResponse toResponse(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getLicensePlate(),
                vehicle.getFleetCode(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getModelYear(),
                vehicle.getType(),
                vehicle.getStatus(),
                vehicle.getCreatedAt(),
                vehicle.getUpdatedAt()
        );
    }
}
