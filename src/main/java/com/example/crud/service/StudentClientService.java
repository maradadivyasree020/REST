package com.example.crud.service;

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

    public Mono<String> getExampleData() {
    return webClient.get()
      .uri("https://jsonplaceholder.typicode.com/posts/1")
      .retrieve()
      .onRawStatus(statusCode -> statusCode >= 400, resp -> {
          System.out.println("Request failed with status " + resp.statusCode());
          return Mono.error(new RuntimeException("External API error: " + resp.statusCode()));
      })
      .bodyToMono(String.class)
      .doOnSubscribe(sub -> System.out.println("Sending request to external API..."))
      .doOnError(error -> System.err.println("Failed: " + error.getMessage()));
}


    // Add other WebClient calls here as needed
}
