package com.example.rest_api.gallery;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.rest_api.gallery.database.repository",
        entityManagerFactoryRef = "galleryDbEntityManagerFactory",
        transactionManagerRef = "galleryDbTransactionManager"
)
@EnableTransactionManagement
public class GalleryDbConfig {

    @Bean(name = "galleryDbDataSource")
    @ConfigurationProperties(prefix="spring.datasource.gallery")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "galleryDbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean galleryDbEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("galleryDbDataSource") DataSource dataSource
    ) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.example.rest_api.gallery.database.model");
        factory.setPersistenceUnitName("gallery_db");
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // Optional configuration for naming strategy
        factory.setJpaPropertyMap(Collections.singletonMap(
                "hibernate.naming.physical-strategy",
                "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl"
        ));

        return factory;
    }

    @Bean(name = "galleryDbTransactionManager")
    public PlatformTransactionManager galleryDbTransactionManager(
            @Qualifier("galleryDbEntityManagerFactory") EntityManagerFactory galleryDbEntityManagerFactory) {
        return new JpaTransactionManager(galleryDbEntityManagerFactory);
    }
}
