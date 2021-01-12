package com.chrisgya.springsecurity;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ContextConfiguration(initializers = AbstractReusableIT.DockerPostgreDataSourceInitializer.class)
@Testcontainers
public abstract class AbstractReusableIT {

    public static PostgreSQLContainer container = (PostgreSQLContainer) new PostgreSQLContainer("postgres:12.3-alpine")
            .withDatabaseName("sp_bp_test")
            .withUsername("sp_bp_test_user")
            .withPassword("sp_bp_test_secret")
            .withReuse(true);

    static {
        container.start();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

}
