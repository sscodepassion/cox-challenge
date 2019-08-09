package com.cox.coredomain.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * 
 * @author Sachin Shanbhag
 * 
 * Rest Controller Advice for Exception handling
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

	@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException responseStatusEx) {
		ErrorMessage errorMessage = new ErrorMessage(responseStatusEx.getMessage(), responseStatusEx);
		return new ResponseEntity<>(errorMessage, responseStatusEx.getStatus());
	}
	
	@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleOtherExceptions(Throwable ex) {
		ErrorMessage errorMessage = new ErrorMessage(ex);
		return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}