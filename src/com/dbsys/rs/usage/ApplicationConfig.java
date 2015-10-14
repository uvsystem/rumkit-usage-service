package com.dbsys.rs.usage;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.ejb.HibernatePersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate3.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Konfigurasi aplikasi (sistem), 'persistence provider',
 * 'interceptor mapping' dan 'component scan'.
 * 
 * @author Deddy Christoper Kakunsi
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.dbsys.rs.usage.repository", "com.dbsys.rs.security.repository"})
@EnableAspectJAutoProxy
@EnableTransactionManagement
@ComponentScan({
	"com.dbsys.rs.usage.service",
	"com.dbsys.rs.lib.interceptor"
})
public class ApplicationConfig {
    
    @Bean
    public HibernateJpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public HibernatePersistence persistenceProvider() {
        return new HibernatePersistence();
    }

    @Bean
    public DataSource dataSource() {
    	DriverManagerDataSource dataSource = new DriverManagerDataSource();

    	dataSource.setDriverClassName(DbConnectionConfig.PROPERTY_NAME_DATABASE_DRIVER);
    	dataSource.setUrl(DbConnectionConfig.PROPERTY_NAME_DATABASE_URL);
    	dataSource.setUsername(DbConnectionConfig.PROPERTY_NAME_DATABASE_USERNAME);
    	dataSource.setPassword(DbConnectionConfig.PROPERTY_NAME_DATABASE_PASSWORD);
    	
    	return dataSource;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
    	LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
    	entityManagerFactoryBean.setPackagesToScan(DbConnectionConfig.PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN);
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPersistenceProvider(persistenceProvider());
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        entityManagerFactoryBean.setJpaPropertyMap(propertiesMap());
        entityManagerFactoryBean.afterPropertiesSet();

        return entityManagerFactoryBean.getObject();
    }

    @Bean
    public JpaTransactionManager transactionManager() {
    	JpaTransactionManager transactionManager = new JpaTransactionManager();
    	
    	transactionManager.setEntityManagerFactory(entityManagerFactory());
    	
    	return transactionManager;
    }
    
    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }
    
    private Map<String, String> propertiesMap() {
        Map<String, String> propertiesMap = new HashMap<>();
        propertiesMap.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        propertiesMap.put("hibernate.show_sql", "false");
        propertiesMap.put("hibernate.format_sql", "true");
        return propertiesMap;
    }
}
