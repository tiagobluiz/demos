package com.playground.demo.utils;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgisContainerProvider;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class PostgisSQLContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Container
    public static JdbcDatabaseContainer<?> POSTGIS = new PostgisContainerProvider().newInstance();

    static {
        POSTGIS.start();
    }

    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        TestPropertyValues.of(
                "spring.datasource.url=" + POSTGIS.getJdbcUrl(),
                "spring.datasource.username=" + POSTGIS.getUsername(),
                "spring.datasource.password=" + POSTGIS.getPassword()
        ).applyTo(configurableApplicationContext.getEnvironment());
    }
}
