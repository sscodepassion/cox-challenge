package com.cox.coredomain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Optional;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.cox.coredomain.config.RestTemplateConfig;
import com.cox.coredomain.model.AnswerRequest;
import com.cox.coredomain.model.AnswerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest (webEnvironment = WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class DataSetServiceImplTest {
	
	@Configuration
	@Import({ DataSetServiceImpl.class, RestTemplateConfig.class })
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
	private DataSetServiceImpl dataSetService;
	
	ObjectMapper objectMapper;
	
	@Before
	public void setUp() {
		mockRestServer = MockRestServiceServer.createServer(restTemplate);
		objectMapper = new ObjectMapper();
	}
	
	@Test
	public void testRetrieveDataSetShouldBeSuccessful() throws Exception {
		mockRestServer.expect(requestTo("http://api.coxauto-interview.com/api/datasetId"))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess()
							.contentType(MediaType.APPLICATION_JSON)
							.body(objectMapper.writeValueAsString(objectMapper.readTree("{\"datasetId\": \"abcdef\"}"))));
					
		Optional<String> actualdataSetId = dataSetService.retrieveDataSet();
		mockRestServer.verify();
		assertTrue(actualdataSetId.isPresent());
		assertNotNull(actualdataSetId.get());
		assertTrue(actualdataSetId.get().length() > 1);
		assertEquals(actualdataSetId, Optional.of("abcdef"));
	}
	
	@Test
	public void testRetrieveDataSetShouldReturnNoDataSet() throws Exception {
		mockRestServer.expect(requestTo("http://api.coxauto-interview.com/api/datasetId"))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess()
							.contentType(MediaType.APPLICATION_JSON)
							.body(""));
					
		Optional<String> actualdataSetId = dataSetService.retrieveDataSet();
		mockRestServer.verify();
		assertEquals(actualdataSetId, Optional.empty());
	}
	
	@Test
	public void testSaveAnswerShouldBeSuccessful() throws Exception {
		AnswerResponse answerResponse = new AnswerResponse();
		answerResponse.setMessage("test");
		
		mockRestServer.expect(requestTo("http://api.coxauto-interview.com/api/abcdef/answer"))
					.andExpect(method(HttpMethod.POST))
					.andRespond(withSuccess()
							.contentType(MediaType.APPLICATION_JSON)
							.body(objectMapper.writeValueAsString(answerResponse)));
					
		Optional<AnswerResponse> actualAnswerResponse = dataSetService.saveAnswer("abcdef", new AnswerRequest());
		mockRestServer.verify();
		assertTrue(actualAnswerResponse.isPresent());
		assertNotNull(actualAnswerResponse.get());
	}
	
	@Test
	public void testSaveAnswerShouldNotReturnResponse() throws Exception {
		mockRestServer.expect(requestTo("http://api.coxauto-interview.com/api/abcdef/answer"))
					.andExpect(method(HttpMethod.POST))
					.andRespond(withSuccess()
							.contentType(MediaType.APPLICATION_JSON)
							.body(""));
					
		Optional<AnswerResponse> actualAnswerResponse = dataSetService.saveAnswer("abcdef", new AnswerRequest());
		mockRestServer.verify();
		assertEquals(actualAnswerResponse, Optional.empty());
	}
}
