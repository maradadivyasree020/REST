package com.example.crud.service;

import com.example.crud.model.AgePrediction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AgePredictionService {

    private final WebClient webClient;

    public AgePredictionService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<AgePrediction> predictAge(String name) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.agify.io")
                        .queryParam("name", name)
                        .build()
                )
                .retrieve()
                .onRawStatus(status -> status >= 400,
                    resp -> Mono.error(new RuntimeException("Error calling agify API: " + resp.statusCode())))
                .bodyToMono(AgePrediction.class)
                .doOnSubscribe(sub -> System.out.println("Calling agify API for name: " + name))
                .doOnError(error -> System.err.println("API call error: " + error.getMessage()));
    }
}
