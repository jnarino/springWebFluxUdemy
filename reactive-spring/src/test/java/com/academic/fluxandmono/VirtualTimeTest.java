package com.academic.fluxandmono;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

class VirtualTimeTest {

	@Test
	void testingWithoutVirtualTime() {

		Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1)).take(3);

		StepVerifier.create(longFlux.log()).expectSubscription().expectNext(0L, 1L, 2L).verifyComplete();
	}

	@Test
	void testingWithVirtualTime() {

		VirtualTimeScheduler.getOrSet();

		Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1)).take(3);

		StepVerifier.withVirtualTime(() -> longFlux.log()).expectSubscription().thenAwait(Duration.ofSeconds(3))
				.expectNext(0L, 1L, 2L).verifyComplete();
	}

}
