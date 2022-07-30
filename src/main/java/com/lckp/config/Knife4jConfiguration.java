/**  
* @Title: Knife4jConfiguration.java
* @version V1.0  
*/
package com.lckp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName: Knife4jConfiguration
 * @Description: knife4j接口文档配置
 * @author LuckyPuppy514
 * @date 2020年6月22日 下午2:51:38
 *
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class Knife4jConfiguration {

	@Value("${server.port}")
	private String port;
	@Value("${server.servlet.context-path}")
	private String contextPath;
	@Value("${project.name}")
	private String projectName;
	@Value("${project.version}")
	private String projectVersion;

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.lckp.controller")).paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(projectName).description("接口文档")
				.termsOfServiceUrl("http://127.0.0.1:" + port + contextPath)
				.version(projectVersion)
				.build();
	}
}
