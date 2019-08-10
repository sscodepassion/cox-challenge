package com.cox.coredomain.controller;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cox.coredomain.delegate.OrchestrationDelegate;
import com.cox.coredomain.model.AnswerResponse;

@RestController
@RequestMapping ("/coxchallenge")
public class ProgramController {
	
	@Autowired
	private OrchestrationDelegate orchestrationDelegate;
	
	@RequestMapping (value = "/startProcess", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<AnswerResponse> startProcess() throws InterruptedException, ExecutionException, IOException {
		
		Optional<AnswerResponse> answerResponse = orchestrationDelegate.processAndSaveAnswer();
		
		if (answerResponse.isPresent()) {
			return new ResponseEntity<AnswerResponse>(answerResponse.get(), HttpStatus.OK);
		} else {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There was an error with the service, please try again after some time");
		}
	}
}
