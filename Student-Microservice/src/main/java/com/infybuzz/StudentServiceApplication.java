package com.infybuzz;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableFeignClients("com.infybuzz.feignclients")
@EnableEurekaClient
//@EnableCircuitBreaker
public class StudentServiceApplication {
	
	/*@Value("${address.service.url}")
	private String addressServiceUrl;*/

	public static void main(String[] args) {
		SpringApplication.run(StudentServiceApplication.class, args);
	}
	
	/*@Bean
	public WebClient webClient () {
		WebClient webClient =WebClient.builder()
				.baseUrl(addressServiceUrl).build();
		
		return webClient;
	}*/

}
