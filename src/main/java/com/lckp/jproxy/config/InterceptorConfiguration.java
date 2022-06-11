/**  
* @Title: InterceptorConfiguration.java
* @version V1.0  
*/
package com.lckp.jproxy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lckp.jproxy.interceptor.ApiProxy;

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
        registry.addInterceptor(new ApiProxy()).addPathPatterns("/**");
    }
}
