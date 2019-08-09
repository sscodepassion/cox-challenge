package com.cox.coredomain.model;

public class AnswerResponse {
	
	private Boolean success;
	
	private String message;
	
	private Integer totalMilliseconds;

	public Boolean getSuccess() {
		return success;
	}

	public AnswerResponse setSuccess(Boolean success) {
		this.success = success;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public AnswerResponse setMessage(String message) {
		this.message = message;
		return this;
	}

	public Integer getTotalMilliseconds() {
		return totalMilliseconds;
	}

	public AnswerResponse setTotalMilliseconds(Integer totalMilliseconds) {
		this.totalMilliseconds = totalMilliseconds;
		return this;
	}
}
