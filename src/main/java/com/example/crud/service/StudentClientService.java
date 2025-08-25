package com.example.crud.service;

import com.example.crud.model.AgePrediction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class StudentClientService {

    private final WebClient webClient;

    public StudentClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<AgePrediction> getExampleData() {
        return webClient.get()
                .uri("https://api.agify.io?name=michael")
                .retrieve()
                .onRawStatus(statusCode -> statusCode >= 400, resp -> Mono.error(new RuntimeException("External API error: " + resp.statusCode())))
                .bodyToMono(AgePrediction.class)
                .doOnSubscribe(sub -> System.out.println("Sending request to external API..."))
                .doOnError(error -> System.err.println("Failed: " + error.getMessage()));
    }
}
