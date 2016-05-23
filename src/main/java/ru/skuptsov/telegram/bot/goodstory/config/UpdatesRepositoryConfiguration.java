package ru.skuptsov.telegram.bot.goodstory.config;

import com.google.common.eventbus.EventBus;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
@Configuration
@Getter
public class UpdatesRepositoryConfiguration {

    @Value("${repository.client.pooling.timeout.sec:100}")
    private Integer poolingTimeout;

    @Value("${repository.client.pooling.limit:20}")
    private Integer poolingLimit;

    @Bean
    public EventBus getEventBus(){
        return new EventBus();
    }
}
