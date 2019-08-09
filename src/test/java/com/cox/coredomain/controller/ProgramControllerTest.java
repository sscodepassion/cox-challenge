package com.cox.coredomain.controller;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.cox.coredomain.delegate.OrchestrationDelegate;
import com.cox.coredomain.model.AnswerResponse;

@RunWith(MockitoJUnitRunner.class)
public class ProgramControllerTest {
	
	@Mock
	private OrchestrationDelegate orchestrationDelagateMock;
	
	@InjectMocks
	private ProgramController programController;
	
	@Test
	public void testStartProcessSuccess() throws Exception {
		when(orchestrationDelagateMock.processAndSaveAnswer()).thenReturn(Optional.of(new AnswerResponse()
				.setMessage("Congratulations.")
				.setSuccess(true)
				.setTotalMilliseconds(5000)));

		ResponseEntity<AnswerResponse> answerResponse = programController.startProcess();
		assertNotNull(answerResponse);
		assertNotNull(answerResponse.getBody());
		assertEquals(answerResponse.getStatusCode(), HttpStatus.OK);
		assertEquals(answerResponse.getBody().getSuccess(), true);
		assertEquals(answerResponse.getBody().getMessage(), "Congratulations.");
		assertEquals(answerResponse.getBody().getTotalMilliseconds(), Integer.valueOf(5000));
		
		verify(orchestrationDelagateMock).processAndSaveAnswer();
	}

	
	@Test (expected = ResponseStatusException.class)
	public void testStartProcessShouldThrowException() throws Exception {
		when(orchestrationDelagateMock.processAndSaveAnswer()).thenReturn(Optional.empty());

		ResponseEntity<AnswerResponse> answerResponse = programController.startProcess();
		assertNull(answerResponse);
		assertThatExceptionOfType(ResponseStatusException.class);

		verify(orchestrationDelagateMock).processAndSaveAnswer();
	}
	
	@Test (expected = Exception.class)
	public void testStartProcessShouldThrowExceptionWhenDelegateCallThrowsException() throws Exception {
		when(orchestrationDelagateMock.processAndSaveAnswer())
			.thenThrow(ExecutionException.class);

		ResponseEntity<AnswerResponse> answerResponse = programController.startProcess();
		assertNull(answerResponse);
		assertThatExceptionOfType(Exception.class);

		verify(orchestrationDelagateMock).processAndSaveAnswer();
	}
}
