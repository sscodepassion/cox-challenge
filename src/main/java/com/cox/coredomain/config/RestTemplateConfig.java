package com.cox.coredomain.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class RestTemplateConfig {
	
	@Value("${cox-auto-interview-api.host}")
	private String coxInterviewApiHost;
	
	@Bean (name = "rest-template")
	public RestTemplate buildRestTemplate(final RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}
	
	@Bean (name = "api-uri")
	public URI buildAPIURI() {
		return UriComponentsBuilder.newInstance()
								.scheme("http")
								.host(coxInterviewApiHost)
								.pathSegment("api")
								.build()
								.toUri();
	}
}