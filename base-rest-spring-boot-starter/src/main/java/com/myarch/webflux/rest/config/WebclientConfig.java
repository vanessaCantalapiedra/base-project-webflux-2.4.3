package com.myarch.webflux.rest.config;


import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class WebclientConfig {

    @Bean
    public DefaultWebClientCustomizer defaultWebClientCustomizer() {
        return new DefaultWebClientCustomizer();
    }

    @Bean
    public WebClient baseWebClient(final WebClient.Builder webClientBuilder) {
        return webClientBuilder.build();
    }
}
