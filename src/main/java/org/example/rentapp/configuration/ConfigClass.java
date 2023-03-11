package org.example.rentapp.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.example.rentapp.converters.AddressDtoToEntityConverter;
import org.example.rentapp.converters.AddressEntityToDtoConverter;
import org.example.rentapp.converters.EntityUserToSecurityConverter;
import org.example.rentapp.converters.FacilityDtoToEntityConverter;
import org.example.rentapp.converters.FacilityEntityToDtoConverter;
import org.example.rentapp.dtos.AddressDto;
import org.example.rentapp.dtos.deserializers.CustomAddressDeserializer;
import org.example.rentapp.dtos.deserializers.CustomCityDeserializer;
import org.example.rentapp.security.SecurityConfig;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(value = "org.example")
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@EnableWebMvc
@Import(SecurityConfig.class)
public class ConfigClass {

    @Value("${entities.package}")
    private String entitiesPackage;

    @Bean
    public ObjectMapper objectMapper() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(AddressDto.class, new CustomAddressDeserializer(customCityDeserializer()));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }

    @Bean
    public CustomCityDeserializer customCityDeserializer() {
        return new CustomCityDeserializer();
    }

    @Bean
    public FacilityDtoToEntityConverter facilityDtoToEntityConverter() {
        return new FacilityDtoToEntityConverter(addressDtoToEntityConverter());
    }

    @Bean
    public FacilityEntityToDtoConverter facilityEntityToDtoConverter() {
        return new FacilityEntityToDtoConverter();
    }

    @Bean
    public AddressDtoToEntityConverter addressDtoToEntityConverter() {
        return new AddressDtoToEntityConverter();
    }

    @Bean
    public AddressEntityToDtoConverter addressEntityToDtoConverter() {
        return new AddressEntityToDtoConverter();
    }

    private EntityUserToSecurityConverter entityUserToSecurityConverter() {
        return new EntityUserToSecurityConverter();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(addressDtoToEntityConverter());
        modelMapper.addConverter(addressEntityToDtoConverter());
        modelMapper.addConverter(facilityDtoToEntityConverter());
        modelMapper.addConverter(facilityEntityToDtoConverter());
        modelMapper.addConverter(entityUserToSecurityConverter());
        return modelMapper;
    }

/*
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setPackagesToScan(entitiesPackage);

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "none");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");

        return properties;
    }*/

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setIgnoreUnresolvablePlaceholders(true);
        configurer.setIgnoreResourceNotFound(true);
        return configurer;
    }
}
