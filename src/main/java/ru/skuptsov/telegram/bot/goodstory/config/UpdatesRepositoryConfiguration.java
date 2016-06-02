package ru.skuptsov.telegram.bot.goodstory.config;

import com.google.common.eventbus.EventBus;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skuptsov.telegram.bot.goodstory.client.TelegramBotApi;
import ru.skuptsov.telegram.bot.goodstory.client.impl.TelegramBotApiImpl;

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

    @Value("${telegram.client.maxConnectionsPerHost:1}")
    private Integer maxConnectionsPerHost;

    @Value("${telegram.client.maxRequestRetry:2}")
    private Integer maxRequestRetry;

    @Value("${telegram.client.readTimeout:60000}")
    private Integer readTimeout;

    @Value("${telegram.client.connectTimeout:10000}")
    private Integer connectTimeout;

    @Value("${telegram.client.allowPoolingConnections:true}")
    private Boolean allowPoolingConnections;

    @Bean
    public EventBus getEventBus() {
        return new EventBus();
    }

    @Autowired
    private TelegramBotClientConfiguration telegramBotClientConfiguration;

    @Bean(name = "updateRepositoryBotApi")
    public TelegramBotApi telegramBotApi() {
        return new TelegramBotApiImpl(telegramBotClientConfiguration.createTelegramBotClient(getClient()));
    }

    private AsyncHttpClient getClient() {
        AsyncHttpClientConfig.Builder asyncHttpClientConfigBuilder = new AsyncHttpClientConfig.Builder()
                .setAllowPoolingConnections(allowPoolingConnections)
                .setConnectTimeout(connectTimeout)
                .setReadTimeout(readTimeout)
                .setMaxRequestRetry(maxRequestRetry)
                .setMaxConnectionsPerHost(maxConnectionsPerHost);

        return new AsyncHttpClient(asyncHttpClientConfigBuilder
                .build());
    }
}
