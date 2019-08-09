package com.cox.coredomain.exceptions;

import java.time.LocalDateTime;

public class ErrorMessage {

	private LocalDateTime timestamp;	
	
	private String message;
	
	private String debugMessage;
	
	private ErrorMessage() {
		timestamp = LocalDateTime.now();
	}
	
	public ErrorMessage(Throwable ex) {
		this();
		this.message = "Unexpected Error";
		this.debugMessage = ex.getLocalizedMessage();
	}
	
	public ErrorMessage(String message, Throwable ex) {
		this();
		this.message = message;
		this.debugMessage = ex.getLocalizedMessage();
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public ErrorMessage setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public ErrorMessage setMessage(String message) {
		this.message = message;
		return this;
	}

	public String getDebugMessage() {
		return debugMessage;
	}

	public ErrorMessage setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
		return this;
	}
}