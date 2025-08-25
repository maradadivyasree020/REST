package com.example.crud.controller;

import com.example.crud.model.AgePrediction;
import com.example.crud.service.AgePredictionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/age-prediction")
public class AgePredictionController {

    private final AgePredictionService service;

    public AgePredictionController(AgePredictionService service) {
        this.service = service;
    }

    @GetMapping
    public Mono<AgePrediction> getAgePrediction(@RequestParam String name) {
        return service.predictAge(name);
    }
}
