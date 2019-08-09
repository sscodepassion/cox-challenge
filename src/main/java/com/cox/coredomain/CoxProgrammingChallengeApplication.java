package com.cox.coredomain;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class CoxProgrammingChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoxProgrammingChallengeApplication.class, args);
	}

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		
		/*
		 * Tuning of core pool size for the Thread pool done based on 2 CPU cores and based on 
		 * (Wait time [max 4 seconds] / execution time [1 second]) of the Vehicles and Dealers API - [2 * (4/1)] = 8   
		 * 
		 */
		
		executor.setCorePoolSize(8);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(200);
		executor.setThreadNamePrefix("cox-dvthread-");
		executor.initialize();
		return executor;
	}
}
