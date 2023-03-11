package org.example.rentapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@Profile("test")
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
})
public class TestConfig {

}
