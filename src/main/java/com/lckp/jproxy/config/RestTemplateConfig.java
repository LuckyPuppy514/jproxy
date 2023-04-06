package com.lckp.jproxy.config;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * RestTemplate 配置
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-25
 */
@Configuration
public class RestTemplateConfig {

	@Bean
	RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder
				.setReadTimeout(Duration.ofMinutes(15))
				.setConnectTimeout(Duration.ofMinutes(15))
				.build();
	}
}
