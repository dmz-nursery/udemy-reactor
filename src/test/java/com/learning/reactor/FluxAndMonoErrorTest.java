package com.learning.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Slf4j
public class FluxAndMonoErrorTest {

  @Test
  public void fluxErrorHandling(){
    Flux<String> stream = Flux.just("A", "B", "C")
      .concatWith(Flux.error(new RuntimeException("Error occurred")))
      .concatWith(Flux.just("D"))
      .onErrorResume(e -> {
        log.error("{}", e);
        return Flux.just("default 1", "default 2");
      });

    StepVerifier.create(stream)
      .expectSubscription()
      .expectNext("A", "B", "C")
      //      .expectError(RuntimeException.class)
      //      .verify();
      .expectNext("default 1", "default 2")
      .verifyComplete();
  }
}
