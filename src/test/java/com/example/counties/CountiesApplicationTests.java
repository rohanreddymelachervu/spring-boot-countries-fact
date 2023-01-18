package com.example.counties;

import com.example.counties.controller.Controller;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(Controller.class)
class CountiesApplicationTests {

	@Test
	void contextLoads() {

	}

}
