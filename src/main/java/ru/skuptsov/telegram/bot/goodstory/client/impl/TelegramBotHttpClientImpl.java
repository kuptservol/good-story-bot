package ru.skuptsov.telegram.bot.goodstory.client.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ning.http.client.*;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.skuptsov.telegram.bot.goodstory.client.TelegramBotHttpClient;
import ru.skuptsov.telegram.bot.goodstory.client.exception.TelegramBotApiException;
import ru.skuptsov.telegram.bot.goodstory.model.ExecutionResult;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.util.Optional.ofNullable;

/**
 * @author Sergey Kuptsov
 * @since 22/05/2016
 */
public class TelegramBotHttpClientImpl implements TelegramBotHttpClient {
    private static final Logger log = LoggerFactory.getLogger(TelegramBotHttpClientImpl.class);
    private final static String BOT_PREFIX = "/bot";

    private final ObjectMapper jsonMapper;
    private final AsyncHttpClient httpClient;
    private final String apiToken;
    private final String baseUrl;
    private final String apiUrl;

    public TelegramBotHttpClientImpl(
            ObjectMapper jsonMapper,
            AsyncHttpClient httpClient,
            String apiToken,
            String baseUrl) {
        this.jsonMapper = jsonMapper;
        this.httpClient = httpClient;
        this.apiToken = apiToken;
        this.baseUrl = baseUrl;
        this.apiUrl = formBaseApiUrl(apiToken, baseUrl);
    }

    @Override
    public <T> T executeGet(@NotNull String method,
                            @Nullable Map<String, String> params,
                            @NotNull JavaType returnType) throws TelegramBotApiException {

        AsyncHttpClient.BoundRequestBuilder requestBuilder =
                httpClient.prepareGet(formUrl(method));

        setParams(params, requestBuilder);

        return execute(requestBuilder.build(), returnType);
    }

    @Override
    public void executeGetAsync(@NotNull String method,
                                @Nullable Map<String, String> params) {

        AsyncHttpClient.BoundRequestBuilder requestBuilder = httpClient.prepareGet(formUrl(method));

        setParams(params, requestBuilder);

        executeRequestAsync(requestBuilder.build());
    }

    @Override
    public <T, V> T executePost(
            @NotNull String method,
            @Nullable V requestObject,
            @NotNull JavaType returnType) throws TelegramBotApiException {

        AsyncHttpClient.BoundRequestBuilder requestBuilder;

        requestBuilder = httpClient.preparePost(formUrl(method));

        setBody(requestObject, requestBuilder);

        return execute(requestBuilder.build(), returnType);
    }

    @Override
    public <V> void executePostAsync(
            @NotNull String method,
            @Nullable V requestObject) {

        AsyncHttpClient.BoundRequestBuilder requestBuilder;

        requestBuilder = httpClient.preparePost(formUrl(method));

        setBody(requestObject, requestBuilder);

        executeRequestAsync(requestBuilder.build());
    }

    private <T> void setBody(@Nullable T requestObject, AsyncHttpClient.BoundRequestBuilder requestBuilder) {
        if (requestObject != null) {
            try {
                requestBuilder.setBody(jsonMapper.writeValueAsBytes(requestObject));
            } catch (JsonProcessingException e) {
                throw new TelegramBotApiException("Error while serializing request object " + requestObject, e);
            }
        }
    }

    private String formBaseApiUrl(String apiToken, String baseUrl) {
        return baseUrl + BOT_PREFIX + apiToken;
    }

    private String formUrl(@NotNull String method) {
        return apiUrl + "/" + method;
    }

    private void setParams(@Nullable Map<String, String> params, AsyncHttpClient.BoundRequestBuilder requestBuilder) {
        ofNullable(params)
                .ifPresent(parameters ->
                        parameters.entrySet().stream()
                                .forEach((param -> requestBuilder.addQueryParam(param.getKey(), param.getValue()))));
    }

    private <T> T execute(Request httpRequest, JavaType returnType)
            throws TelegramBotApiException {

        Response httpResponse = executeRequest(httpRequest);

        checkHttpStatuses(httpResponse);

        ExecutionResult<T> executionResult;
        try {
            executionResult = jsonMapper.readValue(
                    httpResponse.getResponseBodyAsBytes(),
                    jsonMapper.getTypeFactory().constructParametrizedType(
                            ExecutionResult.class,
                            ExecutionResult.class,
                            returnType));
        } catch (IOException e) {
            throw new TelegramBotApiException("Error while deserializing request object from response", e);
        }

        if (executionResult == null) {
            throw new TelegramBotApiException("Response has empty result with return value");
        }

        log.debug("Got execution result : {}", executionResult);

        checkExecutionResult(executionResult);

        return executionResult.getResult();
    }

    private <T> void checkExecutionResult(ExecutionResult<T> executionResult) {
        if (!executionResult.isOk()) {
            throw new TelegramBotApiException("Request not succeeded");
        }
    }

    private Response executeRequest(Request httpRequest)
            throws TelegramBotApiException {

        log.debug("Executing request : {}", httpRequest);

        Response httpResponse;

        try {
            httpResponse = httpClient.executeRequest(
                    httpRequest,
                    new AsyncCompletionHandler<Response>() {
                        @Override
                        public Response onCompleted(Response response) throws Exception {
                            log.debug("Response : {}", response);
                            return response;
                        }
                    }).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new TelegramBotApiException("Error while sending request", e);
        }

        checkHttpStatuses(httpResponse);

        try {
            log.debug("Got response : {}", httpResponse.getResponseBody());
        } catch (IOException e) {
            log.error("Error while getting response body", e);
        }

        return httpResponse;
    }

    private void executeRequestAsync(Request httpRequest)
            throws TelegramBotApiException {

        log.debug("Executing request : {}", httpRequest);

        ListenableFuture<Response> responseFuture = httpClient.executeRequest(
                httpRequest,
                new AsyncCompletionHandler<Response>() {
                    @Override
                    public Response onCompleted(Response response) throws Exception {
                        log.debug("Response : {}", response);
                        return response;
                    }
                });
    }

    private void checkHttpStatuses(Response httpResponse) throws TelegramBotApiException {
        if (httpResponse.getStatusCode() != HttpStatus.SC_OK) {
            throw new TelegramBotApiException("Error while requesting url: " + httpResponse.getUri() +
                    " response code: " + httpResponse.getStatusCode() + " reason: " + httpResponse.getStatusText());
        }
    }
}
