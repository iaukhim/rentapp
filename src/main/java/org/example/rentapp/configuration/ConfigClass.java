package org.example.rentapp.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import net.kaczmarzyk.spring.data.jpa.web.SpecificationArgumentResolver;
import org.example.rentapp.converters.address.AddressDtoToEntityConverter;
import org.example.rentapp.converters.address.AddressEntityToDtoConverter;
import org.example.rentapp.converters.city.CityDtoToEntityConverter;
import org.example.rentapp.converters.city.CityEntityToDtoConverter;
import org.example.rentapp.converters.facility.FacilityDtoToEntityConverter;
import org.example.rentapp.converters.facility.FacilityEntityToDtoConverter;
import org.example.rentapp.converters.order.OrderEntityToDtoConverter;
import org.example.rentapp.converters.review.ReviewEntityToDtoConverter;
import org.example.rentapp.converters.user.EntityUserToSecurityConverter;
import org.example.rentapp.converters.user.UserDtoToEntityConverter;
import org.example.rentapp.converters.user.UserEntityToDtoConverter;
import org.example.rentapp.security.SecurityConfig;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.List;
import java.util.Locale;

@Configuration
@ComponentScan(value = "org.example")
@PropertySource("classpath:application.yml")
@EnableTransactionManagement
@EnableWebMvc
@EnableAutoConfiguration
@Import(SecurityConfig.class)
public class ConfigClass implements WebMvcConfigurer {

    @Value("${entities.package}")
    private String entitiesPackage;

    @Bean
    public ObjectMapper objectMapper() {
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }

    @Bean
    public FacilityDtoToEntityConverter facilityDtoToEntityConverter() {
        return new FacilityDtoToEntityConverter();
    }

    @Bean
    public FacilityEntityToDtoConverter facilityEntityToDtoConverter() {
        return new FacilityEntityToDtoConverter();
    }

    @Bean
    public AddressDtoToEntityConverter addressDtoToEntityConverter() {
        return new AddressDtoToEntityConverter(cityDtoToEntityConverter());
    }

    @Bean
    public AddressEntityToDtoConverter addressEntityToDtoConverter() {
        return new AddressEntityToDtoConverter();
    }

    @Bean
    public CityDtoToEntityConverter cityDtoToEntityConverter() {
        return new CityDtoToEntityConverter();
    }

    @Bean
    public CityEntityToDtoConverter cityEntityToDtoConverter() {
        return new CityEntityToDtoConverter();
    }

    @Bean
    public EntityUserToSecurityConverter entityUserToSecurityConverter() {
        return new EntityUserToSecurityConverter();
    }


    @Bean
    public UserEntityToDtoConverter userEntityToDtoConverter() {
        return new UserEntityToDtoConverter();
    }

    @Bean
    public UserDtoToEntityConverter userDtoToEntityConverter() {
        return new UserDtoToEntityConverter();
    }

    @Bean
    public OrderEntityToDtoConverter orderEntityToDtoConverter() {
        return new OrderEntityToDtoConverter();
    }

    @Bean
    public ReviewEntityToDtoConverter reviewEntityToDtoConverter() {
        return new ReviewEntityToDtoConverter();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setPropertyCondition(new Condition<>() {
            public boolean applies(MappingContext<Object, Object> context) {
                return !(context.getSource() instanceof HibernateProxy || context.getSource() instanceof PersistentCollection<?>);
            }
        });

        modelMapper.addConverter(facilityDtoToEntityConverter());
        modelMapper.addConverter(facilityEntityToDtoConverter());
        modelMapper.addConverter(addressEntityToDtoConverter());
        modelMapper.addConverter(addressDtoToEntityConverter());
        modelMapper.addConverter(cityDtoToEntityConverter());
        modelMapper.addConverter(cityEntityToDtoConverter());
        modelMapper.addConverter(entityUserToSecurityConverter());
        modelMapper.addConverter(userEntityToDtoConverter());
        modelMapper.addConverter(userDtoToEntityConverter());
        modelMapper.addConverter(orderEntityToDtoConverter());
        modelMapper.addConverter(reviewEntityToDtoConverter());

        return modelMapper;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.ENGLISH);
        return slr;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SpecificationArgumentResolver());
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
