package com.cox.coredomain.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.cox.coredomain.config.RestTemplateConfig;
import com.cox.coredomain.model.Vehicle;
import com.cox.coredomain.model.VehicleIDsWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest (webEnvironment = WebEnvironment.NONE)
@RunWith(SpringRunner.class)

public class VehiclesServiceImplTest {

	@Configuration
	@Import({ VehiclesServiceImpl.class, RestTemplateConfig.class })
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
	private VehiclesServiceImpl vehiclesService;
	
	ObjectMapper objectMapper;
	
	@Before
	public void setUp() {
		mockRestServer = MockRestServiceServer.createServer(restTemplate);
		objectMapper = new ObjectMapper();
	}
	
	@Test
	public void testfindAllVehiclesForDataSetShouldReturnSuccessfulResponse() throws Exception {
		VehicleIDsWrapper vehicleIDsWrapper = new VehicleIDsWrapper();
		List<BigInteger> vehicleIds = new ArrayList<>();
		vehicleIds.add(new BigInteger("1234567"));
		vehicleIDsWrapper.setVehicleIds(vehicleIds);
		mockRestServer.expect(requestTo("http://api.coxauto-interview.com/api/abcdef/vehicles"))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess()
							.contentType(MediaType.APPLICATION_JSON)
							.body(objectMapper.writeValueAsString(vehicleIDsWrapper)));
					
		List<BigInteger> vehiclesListActual = vehiclesService.findAllVehiclesForDataSet("abcdef"); 
		mockRestServer.verify();
		assertNotNull(vehiclesListActual);
		assertTrue(vehiclesListActual.size() > 0);
	}
	
	@Test (expected = HttpServerErrorException.class)
	public void testfindAllVehiclesForDataSetShouldReturnException() {
		mockRestServer.expect(requestTo("http://api.coxauto-interview.com/api/abcdef/vehicles"))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
					
		List<BigInteger> vehiclesListActual = vehiclesService.findAllVehiclesForDataSet("abcdef"); 
		mockRestServer.verify();
		assertNull(vehiclesListActual);
		assertThatExceptionOfType(ResponseStatusException.class);
	}
	
	@Test
	public void testfindVehicleForDataSetByVehicleIdShouldReturnSuccessfulResponse() throws Exception {
		Vehicle vehicle = new Vehicle();
		vehicle.setVehicleId(BigInteger.valueOf(1234567));
		vehicle.setDealerId(BigInteger.valueOf(987560));
		mockRestServer.expect(requestTo("http://api.coxauto-interview.com/api/abcdef/vehicles/1234567"))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withSuccess()
							.contentType(MediaType.APPLICATION_JSON)
							.body(objectMapper.writeValueAsString(vehicle)));
					
		CompletableFuture<Vehicle> vehicleActual = vehiclesService.findVehicleForDataSetByVehicleId("abcdef", BigInteger.valueOf(1234567)); 
		mockRestServer.verify();
		assertNotNull(vehicleActual);
		assertEquals(vehicleActual.get().getDealerId(), BigInteger.valueOf(987560));
		assertEquals(vehicleActual.get().getVehicleId(), BigInteger.valueOf(1234567));
	}
	
	@Test (expected = HttpServerErrorException.class)
	public void testfindVehicleForDataSetByVehicleIdShouldThrowException() {
		mockRestServer.expect(requestTo("http://api.coxauto-interview.com/api/abcdef/vehicles/1234567"))
					.andExpect(method(HttpMethod.GET))
					.andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
					
		CompletableFuture<Vehicle> vehicleActual = vehiclesService.findVehicleForDataSetByVehicleId("abcdef", BigInteger.valueOf(1234567)); 
		mockRestServer.verify();
		assertNull(vehicleActual);
		assertThatExceptionOfType(ResponseStatusException.class);
	}
}
