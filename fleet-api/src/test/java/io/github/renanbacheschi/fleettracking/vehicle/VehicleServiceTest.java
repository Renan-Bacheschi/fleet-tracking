package io.github.renanbacheschi.fleettracking.vehicle;

import io.github.renanbacheschi.fleettracking.vehicle.dto.CreateVehicleRequest;
import io.github.renanbacheschi.fleettracking.vehicle.dto.UpdateVehicleRequest;
import io.github.renanbacheschi.fleettracking.vehicle.dto.UpdateVehicleStatusRequest;
import io.github.renanbacheschi.fleettracking.vehicle.dto.VehicleResponse;
import io.github.renanbacheschi.fleettracking.vehicle.exception.DuplicateVehicleException;
import io.github.renanbacheschi.fleettracking.vehicle.exception.VehicleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {
// TODO alterar e padronizar nomes dos testes com test...
    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private TutorialService tutorialService;

    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        vehicleService = new VehicleService(vehicleRepository, tutorialService);
    }

    @Test
    void deveCadastrarVeiculoComPlacaNormalizadaEStatusAtivo() {
        when(vehicleRepository.saveAndFlush(any(Vehicle.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        VehicleResponse response = vehicleService.create(createRequest("abc-1d23", " FLEET-001 "));

        ArgumentCaptor<Vehicle> vehicleCaptor = ArgumentCaptor.forClass(Vehicle.class);
        verify(vehicleRepository).saveAndFlush(vehicleCaptor.capture());
        Vehicle savedVehicle = vehicleCaptor.getValue();

        assertThat(response.id()).isNotNull();
        assertThat(response.licensePlate()).isEqualTo("ABC1D23");
        assertThat(response.fleetCode()).isEqualTo("FLEET-001");
        assertThat(response.status()).isEqualTo(VehicleStatus.ACTIVE);
        assertThat(response.createdAt()).isNotNull();
        assertThat(savedVehicle.getLicensePlate()).isEqualTo("ABC1D23");
    }

    @Test
    void deveRejeitarPlacaDuplicada() {
        when(vehicleRepository.existsByLicensePlate("ABC1234")).thenReturn(true);

        assertThatThrownBy(() -> vehicleService.create(createRequest("abc-1234", "FLEET-001")))
                .isInstanceOf(DuplicateVehicleException.class)
                .hasMessageContaining("license plate")
                .hasMessageContaining("ABC1234");

        verify(vehicleRepository, never()).saveAndFlush(any());
    }

    @Test
    void deveRejeitarCodigoDeFrotaDuplicado() {
        when(vehicleRepository.existsByFleetCode("FLEET-001")).thenReturn(true);

        assertThatThrownBy(() -> vehicleService.create(createRequest("ABC1234", "FLEET-001")))
                .isInstanceOf(DuplicateVehicleException.class)
                .hasMessageContaining("fleet code")
                .hasMessageContaining("FLEET-001");

        verify(vehicleRepository, never()).saveAndFlush(any());
    }

    @Test
    void deveLancarExcecaoAoBuscarVeiculoInexistente() {
        UUID id = UUID.randomUUID();
        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.findById(id))
                .isInstanceOf(VehicleNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void deveAtualizarOsDadosDoVeiculo() {
        UUID id = UUID.randomUUID();
        Vehicle vehicle = createVehicle("ABC1234", "FLEET-001");
        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.saveAndFlush(vehicle)).thenReturn(vehicle);
        UpdateVehicleRequest request = new UpdateVehicleRequest(
                "def-2g34",
                "FLEET-002",
                "Mercedes-Benz",
                "Actros",
                2025,
                VehicleType.TRUCK
        );

        VehicleResponse response = vehicleService.update(id, request);

        assertThat(response.licensePlate()).isEqualTo("DEF2G34");
        assertThat(response.fleetCode()).isEqualTo("FLEET-002");
        assertThat(response.brand()).isEqualTo("Mercedes-Benz");
        assertThat(response.model()).isEqualTo("Actros");
        assertThat(response.modelYear()).isEqualTo(2025);
        assertThat(response.type()).isEqualTo(VehicleType.TRUCK);
        assertThat(response.status()).isEqualTo(VehicleStatus.ACTIVE);
        verify(vehicleRepository).existsByLicensePlateAndIdNot("DEF2G34", id);
        verify(vehicleRepository).existsByFleetCodeAndIdNot("FLEET-002", id);
        verify(vehicleRepository).saveAndFlush(vehicle);
    }

    @Test
    void deveAlterarOStatusDoVeiculo() {
        UUID id = UUID.randomUUID();
        Vehicle vehicle = createVehicle("ABC1234", "FLEET-001");
        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.saveAndFlush(vehicle)).thenReturn(vehicle);

        VehicleResponse response = vehicleService.updateStatus(
                id,
                new UpdateVehicleStatusRequest(VehicleStatus.MAINTENANCE)
        );

        assertThat(response.status()).isEqualTo(VehicleStatus.MAINTENANCE);
        verify(vehicleRepository).saveAndFlush(vehicle);
        verify(tutorialService, never()).requestTutorial(anyString());
    }

    @Test
    void deveEnviarTutorialAoIniciarManutencaoDoZulaine75() {
        UUID id = UUID.randomUUID();
        Vehicle vehicle = createVehicle("ZUL0075", "FLEET-075", "Zulaine", "75");
        when(vehicleRepository.findById(id)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.saveAndFlush(vehicle)).thenReturn(vehicle);

        VehicleResponse response = vehicleService.updateStatus(
                id,
                new UpdateVehicleStatusRequest(VehicleStatus.MAINTENANCE)
        );

        assertThat(response.status()).isEqualTo(VehicleStatus.MAINTENANCE);
        verify(vehicleRepository).saveAndFlush(vehicle);
        verify(tutorialService).requestTutorial("How to remove the pin from the Zulaine 75 coquilho");
    }

    private CreateVehicleRequest createRequest(String licensePlate, String fleetCode) {
        return new CreateVehicleRequest(
                licensePlate,
                fleetCode,
                "Volkswagen",
                "Delivery",
                2024,
                VehicleType.TRUCK
        );
    }

    private Vehicle createVehicle(String licensePlate, String fleetCode) {
        return createVehicle(licensePlate, fleetCode, "Volkswagen", "Delivery");
    }

    private Vehicle createVehicle(String licensePlate, String fleetCode, String brand, String model) {
        return Vehicle.create(
                licensePlate,
                fleetCode,
                brand,
                model,
                2024,
                VehicleType.TRUCK
        );
    }
}
