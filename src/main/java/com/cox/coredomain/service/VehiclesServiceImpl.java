package com.cox.coredomain.service;

import static org.apache.commons.lang3.StringUtils.join;

import java.math.BigInteger;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.cox.coredomain.model.Vehicle;
import com.cox.coredomain.model.VehicleIDsWrapper;

/**
 * @author sashanbh
 * 
 * This is the Service Implementation for calling Vehicles Rest services 
 *
 */
@Service
public class VehiclesServiceImpl implements VehiclesService {

	private static final String RESOURCE_DATASET_ALL_VEHICLES = "/%s/vehicles";

	private static final String RESOURCE_DATASET_VEHICLES = "/%s/vehicles/%s";
	
	@Autowired
	@Qualifier("rest-template")
	private RestTemplate restTemplate;
	
	@Autowired
	@Qualifier("api-uri")
	private URI apiURI;
	
	@Override
	public List<BigInteger> findAllVehiclesForDataSet(final String datasetId) {
		ResponseEntity<VehicleIDsWrapper> vehiclesResponse = restTemplate.exchange(join(apiURI, String.format(RESOURCE_DATASET_ALL_VEHICLES, datasetId)), 
				HttpMethod.GET, new HttpEntity<String>(createRequestHeader()), VehicleIDsWrapper.class);
		
		if (vehiclesResponse.getStatusCodeValue() == HttpStatus.OK.value()) {
			/*
			 * Return Unique Vehicle IDs
			 */
			return vehiclesResponse.getBody().getVehicleIds().parallelStream().distinct().collect(Collectors.toList());
		} else {
			throw new ResponseStatusException(vehiclesResponse.getStatusCode(), "There was an error with the service, please try again after some time");
		}
	}

    /**
     * @Async
     * 
     * This method will retrieve Vehicle details based on a Dataset and Vehicle Id asynchronously and
     * return a Future object to improve response times when called multiple times. 
     * 
     */
	@Override
	@Async
	public CompletableFuture<Vehicle> findVehicleForDataSetByVehicleId(final String datasetId, final BigInteger vehicleId) {
		ResponseEntity<Vehicle> vehicleResponse = restTemplate.exchange(join(apiURI, String.format(RESOURCE_DATASET_VEHICLES, datasetId, vehicleId)), 
				HttpMethod.GET, new HttpEntity<String>(createRequestHeader()), Vehicle.class);
		
		if (vehicleResponse.getStatusCodeValue() == HttpStatus.OK.value()) {
			return CompletableFuture.completedFuture(vehicleResponse.getBody());
		} else {
			throw new ResponseStatusException(vehicleResponse.getStatusCode(), "There was an error with the service, please try again after some time");
		}
	}

	@SuppressWarnings("serial")
	protected HttpHeaders createRequestHeader() {
		return new HttpHeaders() {{
			setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		}};
	}
}
