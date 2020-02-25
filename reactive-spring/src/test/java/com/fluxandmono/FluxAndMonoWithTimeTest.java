package com.fluxandmono;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class FluxAndMonoWithTimeTest {

	@Test
	void infiniteSequence() throws InterruptedException {
		Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(200)).log();

		infiniteFlux.subscribe((element) -> System.out.println("values is : " + element));

		Thread.sleep(3000);
	}

	@Test
	void infiniteSequenceTest() throws InterruptedException {
		Flux<Long> finiteFlux = Flux.interval(Duration.ofMillis(200)).take(3).log();

		StepVerifier.create(finiteFlux).expectSubscription().expectNext(0L, 1L, 2L).verifyComplete();
	}
	
	@Test
	void infiniteSequenceTestMap() throws InterruptedException {
		Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(100))
				.map(l -> new Integer(l.intValue()))
				.take(3)
				.log();

		StepVerifier.create(finiteFlux).expectSubscription().expectNext(0, 1, 2).verifyComplete();
	}
	
	@Test
	void infiniteSequenceTestMap_withDelay() throws InterruptedException {
		Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(100))
				.delayElements(Duration.ofSeconds(1))
				.map(l -> new Integer(l.intValue()))
				.take(3)
				.log();

		StepVerifier.create(finiteFlux).expectSubscription().expectNext(0, 1, 2).verifyComplete();
	}

}
