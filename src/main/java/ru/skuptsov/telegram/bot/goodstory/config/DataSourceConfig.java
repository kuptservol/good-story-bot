package ru.skuptsov.telegram.bot.goodstory.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Sergey Kuptsov
 * @since 03/08/2016
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    @Autowired
    private DataSource dataSource;

    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory() {
        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);

        sessionBuilder.scanPackages("ru.skuptsov.telegram.bot.goodstory.model");

        sessionBuilder.addProperties(loadPropertiesFromFile());

        return sessionBuilder.buildSessionFactory();
    }

    private Properties loadPropertiesFromFile() {
        Properties properties = new Properties();

        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream("hibernate.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot load resource");
        }

        return properties;
    }

    @Autowired
    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {

        return new HibernateTransactionManager(sessionFactory);
    }
}
