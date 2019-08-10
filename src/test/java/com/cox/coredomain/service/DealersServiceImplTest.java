package com.cox.coredomain.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.cox.coredomain.config.RestTemplateConfig;
import com.cox.coredomain.model.Dealer;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest (webEnvironment = WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class DealersServiceImplTest {
	
	@Configuration
	@Import({ DealersServiceImpl.class, RestTemplateConfig.class })
	protected static class Config {
		
		@Bean
		public RestTemplateBuilder restTemplateBuilder() {
			return new RestTemplateBuilder();
		}
	}
	
	@Autowired
	private RestTemplate restTemplate;
	
	private MockRestServiceServer mockRestServer;
	
	@Autowired
	private DealersServiceImpl dealersService;
	
	ObjectMapper objectMapper;
	
	@Before
	public void setUp() {
		mockRestServer = MockRestServiceServer.createServer(restTemplate);
		objectMapper = new ObjectMapper();
	}
	
	@Test
	public void testfindDealerForDataSetByDealerIdShouldReturnSuccessfulResponse() throws Exception {
		Dealer dealer = new Dealer(BigInteger.valueOf(1234567));
		mockRestServer.expect(requestTo("http://api.coxauto-interview.com/api/abcdef/dealers/1234567"))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess()
							.contentType(MediaType.APPLICATION_JSON)
							.body(objectMapper.writeValueAsString(dealer)));
					
		CompletableFuture<Dealer> dealerActual = dealersService.findDealerForDataSetByDealerId("abcdef", BigInteger.valueOf(1234567)); 
		mockRestServer.verify();
		assertNotNull(dealerActual);
		assertEquals(dealerActual.get().getDealerId(), BigInteger.valueOf(1234567));
	}
	
	@Test (expected = HttpClientErrorException.class)
	public void testfindDealerForDataSetByDealerIdShouldThrowException() throws Exception {
		mockRestServer.expect(requestTo("http://api.coxauto-interview.com/api/abcdef/dealers/1234567"))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withStatus(HttpStatus.NOT_FOUND));
					
		CompletableFuture<Dealer> dealerActual = dealersService.findDealerForDataSetByDealerId("abcdef", BigInteger.valueOf(1234567)); 
		mockRestServer.verify();
		assertNull(dealerActual);
		assertThatExceptionOfType(ResponseStatusException.class);
	}
}
