package com.academic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
class ReactiveSpringApplicationTests {

	@Test
	void contextLoads() {

		Flux.just("Strping");
	}

}
