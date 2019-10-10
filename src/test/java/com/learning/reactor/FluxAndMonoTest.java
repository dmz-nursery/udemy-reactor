package com.learning.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
public class FluxAndMonoTest {

  @Test
  public void fluxPractice(){
    Flux.just("one", "two", "three")
      .concatWith(Flux.error(new RuntimeException("Something happened!")))
      .concatWith(Flux.just("four"))
      .log()
      .subscribe(log::info,
                 (e) -> log.error("Something happened (handler)", e),
                 () -> log.info("All done with the flux!"));
  }

  @Test
  public void testFluxElementsWithoutError(){
    Flux<String> stream = Flux.just("message 1", "message 2", "message 3");

    StepVerifier.create(stream)
      .expectNext("message 1")
      .expectNext("message 2")
      .expectNext("message 3")
      .verifyComplete();

    StepVerifier.create(stream)
      .expectNext("message 1", "message 2", "message 3")
      .verifyComplete();
  }

  @Test
  public void testFluxElementsWithError(){
    Flux<String> stream = Flux.just("message 1", "message 2", "message 3")
      .concatWith(Flux.error(new RuntimeException("Bang!")))
      .log();

    StepVerifier.create(stream)
      .expectNext("message 1")
      .expectNext("message 2")
      .expectNext("message 3")
      .expectError(RuntimeException.class)
      //      .expectErrorMessage("Bang!")
      .verify();
  }

  @Test
  public void testFluxElementsCount(){
    Flux<String> stream = Flux.just("message 1", "message 2", "message 3")
      .concatWith(Flux.error(new RuntimeException("Bang!")))
      .log();

    StepVerifier.create(stream)
      .expectNextCount(3)
      .expectErrorMessage("Bang!")
      .verify();
  }

  @Test
  public void testMono(){
    StepVerifier.create(Mono.just("message 1"))
      .expectNext("message 1")
      .verifyComplete();

    StepVerifier.create(Mono.error(RuntimeException::new))
      .expectError(RuntimeException.class)
      .verify();
  }

  @Test
  public void testFactoryMethods(){
    List<String> messageList = Arrays.asList("message 1", "message 2", "message 3");
    String[] messageArray  = new String[] { "message 1", "message 2", "message 3" };

    Flux<String> fluxFromIterable = Flux.fromIterable(messageList);
    StepVerifier.create(fluxFromIterable)
      .expectNext("message 1", "message 2", "message 3")
      .verifyComplete();

    Flux<String> fluxFromArray = Flux.fromArray(messageArray);
    StepVerifier.create(fluxFromArray)
      .expectNext("message 1", "message 2", "message 3")
      .verifyComplete();

    Flux<String> fluxFromStream = Flux.fromStream(messageList.stream());
    StepVerifier.create(fluxFromStream)
      .expectNext("message 1", "message 2", "message 3")
      .verifyComplete();

    Flux<Integer> fluxFromRange = Flux.range(1, 5);
    StepVerifier.create(fluxFromRange)
      .expectNext(1,2,3,4,5)
      .verifyComplete();

    Mono<String> emptyMono = Mono.justOrEmpty(null);
    StepVerifier.create(emptyMono)
      .verifyComplete();

    Supplier<String> stringSupplier = () -> "message";

    Mono<String> monoFromSupplier = Mono.fromSupplier(stringSupplier);
    StepVerifier.create(monoFromSupplier)
      .expectNext("message")
      .verifyComplete();
  }
}
