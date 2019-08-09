package com.cox.coredomain.service;

import static org.apache.commons.lang3.StringUtils.join;

import java.math.BigInteger;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

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

import com.cox.coredomain.model.Dealer;

@Service
public class DealersServiceImpl implements DealersService {

	private static final String RESOURCE_DATASET_DEALERS = "/%s/dealers/%s";
	
	@Autowired
	@Qualifier("rest-template")
	private RestTemplate restTemplate;
	
	@Autowired
	@Qualifier("api-uri")
	private URI apiURI;
	
	@Override
	@Async
	public CompletableFuture<Dealer> findDealerForDataSetByDealerId(final String datasetId, final BigInteger dealerId) {
		ResponseEntity<Dealer> dealerResponse = restTemplate.exchange(join(apiURI, String.format(RESOURCE_DATASET_DEALERS, datasetId, dealerId)), 
				HttpMethod.GET, new HttpEntity<String>(createRequestHeader()), Dealer.class);
		
		if (dealerResponse.getStatusCodeValue() == HttpStatus.OK.value()) {
			return CompletableFuture.completedFuture(dealerResponse.getBody());
		} else {
			throw new ResponseStatusException(dealerResponse.getStatusCode(), "There was an error with the service, please try again after some time");
		}
	}
	
	@SuppressWarnings("serial")
	protected HttpHeaders createRequestHeader() {
		return new HttpHeaders() {{
			setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		}};
	}
}
