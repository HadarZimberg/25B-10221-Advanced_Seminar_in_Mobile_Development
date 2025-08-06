package com.example.apiserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "firebase.enabled=false")
class ApiServerApplicationTests {

	@Test
	void contextLoads() {
	}

}
