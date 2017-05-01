package ru.skuptsov.telegram.bot.goodstory;

import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class TestConfiguration {

    @Bean
    public DataSource getDataSource() {

        PGPoolingDataSource datasource = new PGPoolingDataSource();
        datasource.setDatabaseName("good_story");
        datasource.setUser("good_story");
        datasource.setPassword("good_story");

        return datasource;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() throws Exception {
        final PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        Properties properties = new Properties();

        properties.setProperty("telegram.client.token", "1");

        pspc.setProperties(properties);
        return pspc;
    }
}
