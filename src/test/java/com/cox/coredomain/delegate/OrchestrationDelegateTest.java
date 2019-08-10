package com.cox.coredomain.delegate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.cox.coredomain.model.AnswerRequest;
import com.cox.coredomain.model.AnswerResponse;
import com.cox.coredomain.model.Dealer;
import com.cox.coredomain.model.Vehicle;
import com.cox.coredomain.service.DataSetService;
import com.cox.coredomain.service.DealersService;
import com.cox.coredomain.service.VehiclesService;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@RunWith(MockitoJUnitRunner.class)
public class OrchestrationDelegateTest {

	@Mock
	private DataSetService datasetService;
	
	@Mock
	private DealersService dealersService;
	
	@Mock
	private VehiclesService vehiclesService;
	
	@InjectMocks
	private OrchestrationDelegate delegate;
	
	@Before
	public void setUp() throws Exception {
		List<BigInteger> vehicles = Arrays.asList(new BigInteger("123456"));
		Vehicle vehicle = new Vehicle()
				.setVehicleId(BigInteger.valueOf(123456))
				.setDealerId(BigInteger.valueOf(987650))
				.setMake("Honda")
				.setModel("Odyssey")
				.setYear(2018);
		
		Dealer dealer = new Dealer(BigInteger.valueOf(987650))
				.setName("Brad Barker Honda");
		
		AnswerResponse answerResponse = new AnswerResponse()
				.setSuccess(true)
				.setMessage("Successful save")
				.setTotalMilliseconds(7000);
		
		when(datasetService.retrieveDataSet()).thenReturn(Optional.of("abcdef"));
		when(vehiclesService.findAllVehiclesForDataSet(any())).thenReturn(vehicles);
		when(vehiclesService.findVehicleForDataSetByVehicleId(any(), any())).thenReturn(CompletableFuture.completedFuture(vehicle));
		when(dealersService.findDealerForDataSetByDealerId(any(), any())).thenReturn(CompletableFuture.completedFuture(dealer));
		when(datasetService.saveAnswer(any(), any())).thenReturn(Optional.of(answerResponse));
	}
	
	@Test
	public void testProcessAndSaveAnswerShouldReturnSuccessfulResponse() throws Exception {
		Optional<AnswerResponse> answerResponseActual = delegate.processAndSaveAnswer();
 		
		verify(datasetService, times(1)).retrieveDataSet();
		verify(vehiclesService, times(1)).findAllVehiclesForDataSet(any(String.class));
		verify(vehiclesService, times(1)).findVehicleForDataSetByVehicleId(any(String.class), any(BigInteger.class));
		verify(dealersService, times(1)).findDealerForDataSetByDealerId(any(String.class), any(BigInteger.class));
		verify(datasetService, times(1)).saveAnswer(any(String.class), any(AnswerRequest.class));
		
		assertNotNull(answerResponseActual);
		assertTrue(answerResponseActual.isPresent());
		assertEquals(answerResponseActual.get().getSuccess(), true);
		assertEquals(answerResponseActual.get().getMessage(), "Successful save");
	}
	
	@Test
	public void testProcessAndSaveAnswerWhenNoDatasetReturned() throws Exception {
		when(datasetService.retrieveDataSet()).thenReturn(Optional.empty());
		Optional<AnswerResponse> answerResponseActual = delegate.processAndSaveAnswer();
 		
		verify(datasetService, times(1)).retrieveDataSet();
		verify(vehiclesService, times(0)).findAllVehiclesForDataSet(any(String.class));
		verify(vehiclesService, times(0)).findVehicleForDataSetByVehicleId(any(String.class), any(BigInteger.class));
		verify(dealersService, times(0)).findDealerForDataSetByDealerId(any(String.class), any(BigInteger.class));
		verify(datasetService, times(0)).saveAnswer(any(String.class), any(AnswerRequest.class));
		
		assertFalse(answerResponseActual.isPresent());
	}
	
	@Test
	public void testProcessAndSaveAnswerWhenNoVehiclesReturnedForDataset() throws Exception {
		when(vehiclesService.findAllVehiclesForDataSet(any())).thenReturn(Collections.emptyList());
		Optional<AnswerResponse> answerResponseActual = delegate.processAndSaveAnswer();
 		
		verify(datasetService, times(1)).retrieveDataSet();
		verify(vehiclesService, times(1)).findAllVehiclesForDataSet(any(String.class));
		verify(vehiclesService, times(0)).findVehicleForDataSetByVehicleId(any(String.class), any(BigInteger.class));
		verify(dealersService, times(0)).findDealerForDataSetByDealerId(any(String.class), any(BigInteger.class));
		verify(datasetService, times(0)).saveAnswer(any(String.class), any(AnswerRequest.class));
		
		assertFalse(answerResponseActual.isPresent());
	}
	
	@Test
	public void testProcessAndSaveAnswerWhenNoVehicleReturnedForDatasetByVehicleId() throws Exception {
		when(vehiclesService.findVehicleForDataSetByVehicleId(any(), any())).thenReturn(CompletableFuture.completedFuture(null));
		Optional<AnswerResponse> answerResponseActual = delegate.processAndSaveAnswer();
 		
		verify(datasetService, times(1)).retrieveDataSet();
		verify(vehiclesService, times(1)).findAllVehiclesForDataSet(any(String.class));
		verify(vehiclesService, times(1)).findVehicleForDataSetByVehicleId(any(String.class), any(BigInteger.class));
		verify(dealersService, times(0)).findDealerForDataSetByDealerId(any(String.class), any(BigInteger.class));
		verify(datasetService, times(0)).saveAnswer(any(String.class), any(AnswerRequest.class));
		
		assertFalse(answerResponseActual.isPresent());
	}
}
