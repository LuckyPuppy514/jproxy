package com.lckp.jproxy.config;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ExtractingResponseErrorHandler;
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
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		messageConverters.add(converter);
		return restTemplateBuilder.setReadTimeout(Duration.ofMinutes(15))
				.setConnectTimeout(Duration.ofMinutes(15))
				.additionalMessageConverters(messageConverters)
				.errorHandler(new ExtractingResponseErrorHandler() {
					@Override
					public boolean hasError(ClientHttpResponse response) throws IOException {
						return !response.getStatusCode().is2xxSuccessful()
								&& !HttpStatusCode.valueOf(409).equals(response.getStatusCode());
					}
				}).build();
	}
}
