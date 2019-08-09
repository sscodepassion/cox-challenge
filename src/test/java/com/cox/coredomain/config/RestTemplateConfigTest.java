package com.cox.coredomain.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RestTemplateConfigTest {
	
	@Configuration
	@Import({RestTemplateConfig.class})
	protected static class Config {	
		
		@Bean
		public RestTemplateBuilder restTemplateBuilderMock() {
			RestTemplateBuilder restTemplateBuilder = mock(RestTemplateBuilder.class);
			when(restTemplateBuilder.build()).thenReturn(mock(RestTemplate.class));
			return restTemplateBuilder;
		}
	}
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	@Qualifier("api-uri")
	private URI apiURI;
	
	@Autowired
	private RestTemplateBuilder restTemplateBuilderMock;
	
	@Test
	public void testBuildRestTemplate() {
		assertNotNull(restTemplate);
		assertNotNull(apiURI);
		String apiURIActual = apiURI.toString();
		assertNotNull(apiURIActual);
		assertTrue(apiURIActual.length() > 10);
		verify(restTemplateBuilderMock).build();
	}
}