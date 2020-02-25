package com.fluxandmono;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static reactor.core.scheduler.Schedulers.parallel;

class FluxAndMonoTransformTest {

	List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

	@Test
	void transformUsingMap() {

		Flux<String> namesFlux = Flux.fromIterable(names).map(s -> s.toUpperCase()).log();

		StepVerifier.create(namesFlux).expectNext("ADAM", "ANNA", "JACK", "JENNY").verifyComplete();

	}

	@Test
	void transformUsingMap_Length() {

		Flux<Integer> namesFlux = Flux.fromIterable(names).map(s -> s.length()).log();

		StepVerifier.create(namesFlux).expectNext(4, 4, 4, 5).verifyComplete();

	}

	@Test
	void transformUsingMap_Length_Repeat() {

		Flux<Integer> namesFlux = Flux.fromIterable(names).map(s -> s.length()).repeat(1).log();

		StepVerifier.create(namesFlux).expectNext(4, 4, 4, 5, 4, 4, 4, 5).verifyComplete();

	}

	@Test
	void transformUsingMap_Filter() {

		Flux<String> namesFlux = Flux.fromIterable(names).filter(s -> s.length() > 4).map(s -> s.toUpperCase()).log();

		StepVerifier.create(namesFlux).expectNext("JENNY").verifyComplete();

	}

	@Test
	void transformUsingFlatMap() {

		Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F")).flatMap(s -> {

			return Flux.fromIterable(covertToList(s));
		}).log();

		StepVerifier.create(stringFlux).expectNextCount(12).verifyComplete();

	}

	private List<String> covertToList(String s) {

		try {

			Thread.sleep(1000);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return Arrays.asList(s, "newValue");

	}

	@Test
	void transformUsingFlatMap_usingParallel() {

		Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F")).window(2)
				.flatMap((s) -> s.map(this::covertToList).subscribeOn(parallel())).flatMap(s -> Flux.fromIterable(s))
				.log();

		StepVerifier.create(stringFlux).expectNextCount(12).verifyComplete();

	}

	@Test
	void transformUsingFlatMap_usingParallel_maintain_order() {

		Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F")).window(2)
				.flatMapSequential((s) -> s.map(this::covertToList).subscribeOn(parallel()))
				.flatMap(s -> Flux.fromIterable(s)).log();

		StepVerifier.create(stringFlux).expectNextCount(12).verifyComplete();

	}

}
