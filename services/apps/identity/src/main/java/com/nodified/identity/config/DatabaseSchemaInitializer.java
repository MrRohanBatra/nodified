package com.nodified.identity.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Slf4j
@Configuration
public class DatabaseSchemaInitializer implements BeanPostProcessor {

    @Value("${spring.jpa.properties.hibernate.default_schema:identity}")
    private String schemaName;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource dataSource) {
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {
                log.info("Ensuring PostgreSQL schema exists: '{}'", schemaName);
                statement.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
                log.info("Schema '{}' verified successfully.", schemaName);
            } catch (Exception e) {
                log.warn("Could not automatically create schema '{}': {}", schemaName, e.getMessage());
            }
        }
        return bean;
    }
}
