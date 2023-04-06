package com.lckp.jproxy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * 
 * <p>
 * Swagger 配置类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-04
 */
@Configuration
public class SwaggerConfig {

	@Value("${project.name}")
	private String projectName;

	@Value("${project.version}")
	private String projectVersion;

	@Value("${project.description}")
	private String projectDescription;

	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info().title(projectName).version(projectVersion).description(projectDescription));
	}
}
