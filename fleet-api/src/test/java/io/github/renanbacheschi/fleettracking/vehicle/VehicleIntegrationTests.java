package io.github.renanbacheschi.fleettracking.vehicle;

import io.github.renanbacheschi.fleettracking.TestcontainersConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.sql.Connection;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VehicleIntegrationTests {

    @Autowired
    private RestTestClient restTestClient;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void limparBancoDeDados() {
        vehicleRepository.deleteAll();
    }
// TODO  criar novos testes
    @Test
    void deveRetornarVeiculoCriadoAoRealizarPostValido() {
        restTestClient.post()
                .uri("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .body(criarRequisicaoValida("abc-1d23", "FLEET-001"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().valueMatches("Location", "/api/v1/vehicles/[0-9a-f-]+")
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.licensePlate").isEqualTo("ABC1D23")
                .jsonPath("$.status").isEqualTo("ACTIVE")
                .jsonPath("$.createdAt").isNotEmpty();
    }

    @Test
    void devePermitirConsultarVeiculoCadastrado() {
        restTestClient.post()
                .uri("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .body(criarRequisicaoValida("ABC1234", "FLEET-002"))
                .exchange()
                .expectStatus().isCreated();

        UUID id = vehicleRepository.findAll().getFirst().getId();

        restTestClient.get()
                .uri("/api/v1/vehicles/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(id.toString())
                .jsonPath("$.fleetCode").isEqualTo("FLEET-002")
                .jsonPath("$.type").isEqualTo("TRUCK");
    }

    @Test
    void deveRetornarBadRequestAoReceberDadosInvalidos() {
        String request = """
                {
                  "licensePlate": "invalid",
                  "fleetCode": "",
                  "brand": "",
                  "model": "Delivery",
                  "modelYear": 1899,
                  "type": "TRUCK"
                }
                """;

        restTestClient.post()
                .uri("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Invalid request")
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.errors.licensePlate").isArray()
                .jsonPath("$.errors.fleetCode").isArray();
    }

    @Test
    void deveRetornarConflitoParaPlacaNormalizadaDuplicada() {
        restTestClient.post()
                .uri("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .body(criarRequisicaoValida("ABC-1234", "FLEET-003"))
                .exchange()
                .expectStatus().isCreated();

        restTestClient.post()
                .uri("/api/v1/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .body(criarRequisicaoValida("abc1234", "FLEET-004"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("$.title").isEqualTo("Duplicate vehicle")
                .jsonPath("$.status").isEqualTo(409);
    }

    @Test
    void deveRetornarNotFoundQuandoOVeiculoNaoExistir() {
        UUID id = UUID.randomUUID();

        restTestClient.get()
                .uri("/api/v1/vehicles/{id}", id)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Vehicle not found")
                .jsonPath("$.status").isEqualTo(404);
    }

    @Test
    void deveAplicarMigrationDeVeiculoNoPostgreSql() throws Exception {
        Integer successfulMigrations = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM flyway_schema_history WHERE version = '1' AND success",
                Integer.class
        );
        String databaseProduct;
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            databaseProduct = connection.getMetaData().getDatabaseProductName();
        }

        assertThat(successfulMigrations).isEqualTo(1);
        assertThat(databaseProduct).isEqualTo("PostgreSQL");
    }

    private String criarRequisicaoValida(String licensePlate, String fleetCode) {
        return """
                {
                  "licensePlate": "%s",
                  "fleetCode": "%s",
                  "brand": "Volkswagen",
                  "model": "Delivery",
                  "modelYear": 2024,
                  "type": "TRUCK"
                }
                """.formatted(licensePlate, fleetCode);
    }
}
