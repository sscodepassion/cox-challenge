package com.cox.coredomain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest (classes = {CoxProgrammingChallengeApplication.class})
public class CoxProgrammingChallengeApplicationTests {

	@Test
	public void testShouldReturnNewInstance() {
		assertNotNull(new CoxProgrammingChallengeApplication());
	}
}
