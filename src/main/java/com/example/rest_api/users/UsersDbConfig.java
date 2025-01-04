package com.example.rest_api.users;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "usersDbEntityManagerFactory",
        transactionManagerRef = "usersDbTransactionManager",
        basePackages = "com.example.rest_api.users.database.repository"
)
@EnableTransactionManagement
public class UsersDbConfig {

    @Primary
    @Bean(name = "usersDbDataSource")
    @ConfigurationProperties(prefix="spring.datasource.users")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "usersDbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean usersDbEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("usersDbDataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.example.rest_api.users.database.model");
        factory.setPersistenceUnitName("users_db");
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // Optional configuration for naming strategy
//        factory.setJpaPropertyMap(Collections.singletonMap(
//                "hibernate.naming.physical-strategy",
//                "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl"
//        ));
        factory.setJpaPropertyMap(Map.of(
                "hibernate.naming.physical-strategy", "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl",
                "hibernate.hbm2ddl.auto", "update"
        ));

        return factory;
    }

    @Primary
    @Bean(name = "usersDbTransactionManager")
    public PlatformTransactionManager usersDbTransactionManager(
            @Qualifier("usersDbEntityManagerFactory") EntityManagerFactory usersDbEntityManagerFactory) {
        return new JpaTransactionManager(usersDbEntityManagerFactory);
    }
}
