package com.example.careplus;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
	"spring.sql.init.mode=never"
})
class CareplusApplicationTests {

	@Test
	void contextLoads() {
	}

}
