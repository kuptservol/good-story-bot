package ru.skuptsov.telegram.bot.goodstory.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skuptsov.telegram.bot.goodstory.client.TelegramBotHttpClient;
import ru.skuptsov.telegram.bot.goodstory.client.impl.TelegramBotHttpHttpClientImpl;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
@Configuration
public class TelegramBotClientConfiguration {

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

    @Value("${telegram.client.token}")
    private String clientToken;

    @Value("${telegram.client.baseUrl:https://api.telegram.org}")
    private String baseUrl;

    @Bean
    public TelegramBotHttpClient getTelegramBotClient() {
        return new TelegramBotHttpHttpClientImpl(getJsonMapper(), getClient(), clientToken, baseUrl);
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

    private ObjectMapper getJsonMapper() {
        return new ObjectMapper();
    }
}
