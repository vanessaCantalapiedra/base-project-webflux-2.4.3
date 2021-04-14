package com.myarch.webflux.rest.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Slf4j
public class DefaultWebClientCustomizer implements WebClientCustomizer {
    @Value("${base-webclient.timeouts.connection:3000}")
    private int connectionTimeout;

    @Value("${base-webclient.timeouts.read:3000}")
    private int readTimeOut;

    @Override
    public void customize(final WebClient.Builder webClientBuilder) {
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(tcpClient -> {
                    tcpClient = tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout);
                    tcpClient = tcpClient.doOnConnected(conn -> conn
                            .addHandlerLast(new ReadTimeoutHandler(readTimeOut, TimeUnit.MILLISECONDS)));
                    tcpClient = tcpClient.doOnConnected(conn -> conn
                            .addHandlerLast(new WriteTimeoutHandler(readTimeOut, TimeUnit.MILLISECONDS)));
                    return tcpClient;

                });
        webClientBuilder.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        webClientBuilder.clientConnector(new ReactorClientHttpConnector(httpClient));
        webClientBuilder.filter(logResponse());
        webClientBuilder.filter(logRequest());
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("=========================== response begin ================================================");
            log.info("Status code  : {}", clientResponse.statusCode().toString());
            log.info("Status text  : {}", clientResponse.statusCode().getReasonPhrase());
            log.info("Headers      : {}", clientResponse.headers());
            log.info("=========================== response end   ================================================");
            return Mono.just(clientResponse);
        });
    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            log.info("=========================== request begin  ================================================");
            log.info("URI         : {}", clientRequest.url());
            log.info("Method      : {}", clientRequest.method());
            log.info("Headers     : {}", clientRequest.headers());
            log.info("=========================== request end    ================================================");
            return next.exchange(clientRequest);
        };
    }
}
