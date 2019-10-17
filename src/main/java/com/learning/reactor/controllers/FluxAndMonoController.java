package com.learning.reactor.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class FluxAndMonoController {

  @GetMapping("/flux-json")
  public Flux<Integer> getFluxAsJson(){
    return Flux.range(1, 5).delayElements(Duration.ofSeconds(1));
  }

  @GetMapping(value = "/flux-stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Flux<Integer> getFluxAsStream(){
    return Flux.range(1, 5).delayElements(Duration.ofSeconds(1));
  }
}
