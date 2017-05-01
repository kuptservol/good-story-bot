package ru.skuptsov.telegram.bot.goodstory.content.parser;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfigBean;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParserConfiguration {

    @Bean
    public AsyncHttpClient getAsyncHttpClient() {
        return new AsyncHttpClient(
                new AsyncHttpClientConfigBean
                        .Builder()
                        .build()
        );
    }

    @Bean
    public OkHttpClient getOkHttpClient() {
        return new OkHttpClient();
    }
}
