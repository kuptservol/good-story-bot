package ru.skuptsov.telegram.bot.goodstory.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Sergey Kuptsov
 * @since 06/06/2016
 */
@Configuration
@ComponentScan(value = "ru.skuptsov.telegram.bot.goodstory")
@EnableAsync
public class GoodStoryBotConfiguration implements ApplicationContextAware {

    @Value("${server.time.zone:Europe/Moscow}")
    public String serverTimeZone;

    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        GoodStoryBotConfiguration.applicationContext = applicationContext;
    }
}
