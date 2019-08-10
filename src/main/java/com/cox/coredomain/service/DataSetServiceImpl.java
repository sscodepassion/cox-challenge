package com.cox.coredomain.service;

import static org.apache.commons.lang3.StringUtils.join;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cox.coredomain.model.AnswerRequest;
import com.cox.coredomain.model.AnswerResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DataSetServiceImpl implements DataSetService {

	private static final String RESOURCE_DATASET = "/datasetId";

	private static final String RESOURCE_DATASET_ANSWER = "/%s/answer";
	
	@Autowired
	@Qualifier("rest-template")
	private RestTemplate restTemplate;
	
	@Autowired
	@Qualifier("api-uri")
	private URI apiURI;
	
	@Override
	public Optional<String> retrieveDataSet() throws IOException {
		
		ResponseEntity<String> datasetIdResponse = restTemplate.exchange(join(apiURI, RESOURCE_DATASET), 
				HttpMethod.GET, new HttpEntity<String>(createRequestHeader()), String.class);

		if (null == datasetIdResponse || null == datasetIdResponse.getBody()) {
			return Optional.empty();
		}
		
		String datasetId = null;
		datasetId = getDataSetID(datasetIdResponse.getBody());
		return Optional.of(datasetId);
	}

	@Override
	public Optional<AnswerResponse> saveAnswer(final String datasetId, final AnswerRequest vehicleDealerData) {
		
		ResponseEntity<AnswerResponse> answerResponse = restTemplate.postForEntity(join(apiURI, String.format(RESOURCE_DATASET_ANSWER, datasetId)), 
				new HttpEntity<AnswerRequest>(vehicleDealerData), AnswerResponse.class);
		
		if (null == answerResponse || null == answerResponse.getBody()) {
			return Optional.empty();
		}
		
		return Optional.of(answerResponse.getBody());
	}
	
	@SuppressWarnings("serial")
	protected HttpHeaders createRequestHeader() {
		return new HttpHeaders() {{
			setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		}};
	}
	
	protected String getDataSetID(final String datasetIdJson) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(datasetIdJson);
		JsonNode datasetId = root.path("datasetId");
		return datasetId.asText();
	}
}
