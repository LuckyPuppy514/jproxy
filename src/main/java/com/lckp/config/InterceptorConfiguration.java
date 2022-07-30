/**  
* @Title: InterceptorConfiguration.java
* @version V1.0  
*/
package com.lckp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lckp.constant.Page;
import com.lckp.interceptor.LoginInterceptor;
import com.lckp.interceptor.MarketServerInterceptor;
import com.lckp.interceptor.ProxyInterceptor;

/**
 * @ClassName: InterceptorConfiguration
 * @Description: 拦截器配置
 * @author LuckyPuppy514
 * @date 2020年8月17日 下午5:14:54
 *
 */
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 代理拦截器
		registry.addInterceptor(new ProxyInterceptor())
			// 拦截所有
			.addPathPatterns("/**")
			// 排除登录相关
			.excludePathPatterns("/" + Page.LOGIN, "/adminUser/login", "/adminUser/captcha")
			// 排除静态文件
			.excludePathPatterns("/lib/**", "/js/**")
			// 排除 knife4j
			.excludePathPatterns("/swagger-resources", "/v2/**", "/webjars/**", "/doc.html")
			.excludePathPatterns("/ruleTestExample/**", "/ruleConfig/**", "/proxyConfig/**", "/page/**", "/adminUser/**", "/ruleMarket/**");
		
		// 登录拦截器
		registry.addInterceptor(new LoginInterceptor())
			// 拦截所有
			.addPathPatterns("/**")
			// 排除登录相关
			.excludePathPatterns("/" + Page.LOGIN, "/adminUser/login", "/adminUser/captcha")
			// 排除静态文件
			.excludePathPatterns("/lib/**", "/js/**")
			// 排除 knife4j
			.excludePathPatterns("/swagger-resources", "/v2/**", "/webjars/**", "/doc.html")
			.excludePathPatterns("/ruleMarket/server/**");
		
		// 规则市场拦截器
		registry.addInterceptor(new MarketServerInterceptor()).addPathPatterns("/ruleMarket/server/**");
	}
}
