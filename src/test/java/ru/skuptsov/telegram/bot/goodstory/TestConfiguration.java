package ru.skuptsov.telegram.bot.goodstory;

import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

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
}
