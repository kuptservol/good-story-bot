package ru.skuptsov.telegram.bot.goodstory.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Sergey Kuptsov
 * @since 21/05/2016
 */
@Configuration
@Import(value = {
        UpdatesRepositoryConfiguration.class,
        TelegramBotClientConfiguration.class,
        NextOffSetStrategyConfiguration.class,
        UpdatesWorkerRepositoryConfiguration.class
})
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class,})
@ComponentScan(value = "ru.skuptsov.telegram.bot.goodstory")
public class ApplicationConfiguration {
}
