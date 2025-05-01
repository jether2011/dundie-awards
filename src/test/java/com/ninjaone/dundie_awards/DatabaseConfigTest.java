package com.ninjaone.dundie_awards;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
abstract class DatabaseConfigTest {
    static PostgreSQLContainer<?> pgsql = new PostgreSQLContainer<>("postgres:latest");

    static void postgreSQLContainerProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", pgsql::getJdbcUrl);
        registry.add("spring.datasource.username", pgsql::getUsername);
        registry.add("spring.datasource.password", pgsql::getPassword);
    }

    @BeforeAll
    static void startPostgreSQLContainer() {
        pgsql.start();
    }

    @AfterAll
    static void stopPostgreSQLContainer() {
        pgsql.stop();
    }
}
