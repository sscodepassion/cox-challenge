package com.cox.coredomain.service;

import java.util.Optional;

import com.cox.coredomain.model.AnswerRequest;
import com.cox.coredomain.model.AnswerResponse;

public interface DataSetService {
	
	public Optional<String> retrieveDataSet();
	
	public Optional<AnswerResponse> saveAnswer(String datasetId, AnswerRequest vehicleDealerData);
	
}
