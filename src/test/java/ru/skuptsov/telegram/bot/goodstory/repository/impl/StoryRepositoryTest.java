package ru.skuptsov.telegram.bot.goodstory.repository.impl;

import org.kubek2k.springockito.annotations.SpringockitoAnnotatedContextLoader;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.skuptsov.telegram.bot.goodstory.config.GoodStoryBotConfiguration;
import ru.skuptsov.telegram.bot.goodstory.repository.StoryRepository;

import javax.sql.DataSource;

@ActiveProfiles({"test"})
@ContextConfiguration(
        classes = {
                GoodStoryBotConfiguration.class,
                StoryRepositoryTest.TestDataSourceConfiguration.class
        },
        loader = SpringockitoAnnotatedContextLoader.class
)
public class StoryRepositoryTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private StoryRepository storyRepository;

    @Test
    public void test() {
        storyRepository.selectById();
    }

    @Configuration
    public static class TestDataSourceConfiguration {

        @Bean
        public DataSource getDataSource() {

            PGPoolingDataSource datasource = new PGPoolingDataSource();
            datasource.setDatabaseName("good_story");
            datasource.setUser("good_story");
            datasource.setPassword("good_story");

            return datasource;
        }
    }
}
