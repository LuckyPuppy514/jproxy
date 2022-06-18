/**  
* @Title: InterceptorConfiguration.java
* @version V1.0  
*/
package com.lckp.jproxy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lckp.jproxy.interceptor.CommonInterceptor;
import com.lckp.jproxy.interceptor.JackettProxy;
import com.lckp.jproxy.interceptor.ProwlarrProxy;

/**
* @ClassName: InterceptorConfiguration
* @Description: 拦截器配置
* @author Liujiawang
* @date 2020年8月17日 下午5:14:54
*
*/
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(new JackettProxy()).addPathPatterns("/api/**");
    	registry.addInterceptor(new ProwlarrProxy()).addPathPatterns("/**/api/**");
    	registry.addInterceptor(new CommonInterceptor()).addPathPatterns("/**")
    													.excludePathPatterns("/doc.html")
    													.excludePathPatterns("/favicon.ico")
    													.excludePathPatterns("/swagger-resources")
    													.excludePathPatterns("/v2/**")
    													.excludePathPatterns("/webjars/**")
    													.excludePathPatterns("/format/**")
    													.excludePathPatterns("/error");
    }
}
