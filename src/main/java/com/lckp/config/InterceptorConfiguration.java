/**  
* @Title: InterceptorConfiguration.java
* @version V1.0  
*/
package com.lckp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lckp.constant.Page;
import com.lckp.interceptor.JackettInterceptor;
import com.lckp.interceptor.LoginInterceptor;
import com.lckp.interceptor.MarketServerInterceptor;
import com.lckp.interceptor.ProwlarrInterceptor;

/**
 * @ClassName: InterceptorConfiguration
 * @Description: 拦截器配置
 * @author LuckyPuppy514
 * @date 2020年8月17日 下午5:14:54
 *
 */
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
	
	private final static String ALL_PATH = "/**";
	private final static String[] LOGIN_PATH = {"/" + Page.LOGIN, "/adminUser/login", "/adminUser/captcha"};
	private final static String[] STATIC_PATH = {"/lib/**", "/js/**"};
	private final static String[] KNIFE4J_PATH = {"/swagger-resources", "/v2/**", "/webjars/**", "/doc.html", "/error"};
	private final static String[] RULEMARKET_SERVER_PATH = {"/ruleMarket/server/**"};
	
	private final static String[] JACKETT_PROXY_PATH = {"/jackett/**"};
	private final static String[] PROWLARR_PROXY_PATH = {"/prowlarr/**"};
	private final static String[] QBITTORRENT_PROXY_PATH = {"/qbittorrent/**"};
			
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 登录拦截器
		registry.addInterceptor(new LoginInterceptor())
			.addPathPatterns(ALL_PATH)
			// 排除登录相关
			.excludePathPatterns(LOGIN_PATH)
			// 排除静态资源
			.excludePathPatterns(STATIC_PATH)
			// 排除 knife4j
			.excludePathPatterns(KNIFE4J_PATH)
			// 排除规则市场服务端
			.excludePathPatterns(RULEMARKET_SERVER_PATH)
			// 排除代理
			.excludePathPatterns(JACKETT_PROXY_PATH)
			.excludePathPatterns(PROWLARR_PROXY_PATH)
			.excludePathPatterns(QBITTORRENT_PROXY_PATH);
		
		// 代理拦截器
		registry.addInterceptor(new JackettInterceptor()).addPathPatterns(JACKETT_PROXY_PATH);
		registry.addInterceptor(new ProwlarrInterceptor()).addPathPatterns(PROWLARR_PROXY_PATH);
		
		// 规则市场拦截器
		registry.addInterceptor(new MarketServerInterceptor()).addPathPatterns(RULEMARKET_SERVER_PATH);
	}
}
