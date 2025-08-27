package com.example.crud.service;

import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import com.example.crud.model.AgePrediction;

@Service
public class AgePredictionService {

    private final RestTemplate restTemplate;

    public AgePredictionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AgePrediction predictAge(String name) {
        String url = "https://api.agify.io?name=" + UriUtils.encode(name, StandardCharsets.UTF_8);
        return restTemplate.getForObject(url, AgePrediction.class);
    }
}
