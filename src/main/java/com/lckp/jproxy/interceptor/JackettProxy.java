/**
 * @Title: JackettProxy.java
 * @version V1.0
 */
package com.lckp.jproxy.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import com.lckp.jproxy.config.ApiConfig;
import com.lckp.jproxy.constant.Proxy;
import com.lckp.jproxy.constant.ProxyType;
import com.lckp.jproxy.param.ProxyParam;
import com.lckp.jproxy.util.ApplicationContextHolder;

/**
 * @className: JackettProxy
 * @description: jackett代理
 * @date 2022年6月17日
 * @author LuckyPuppy514
 */
public class JackettProxy extends ApiProxy implements HandlerInterceptor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JackettProxy.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		ApiConfig apiConfig = (ApiConfig) ApplicationContextHolder.getBean("apiConfig");
		LOGGER.info("proxy jackett: {}", apiConfig.getJackett());

		ProxyParam proxyParam = new ProxyParam();
		proxyParam.setUrl(apiConfig.getJackett());
		proxyParam.setSearchKeyField(Proxy.JACKETT_SEARCH_KEY_FIELD);
		proxyParam = getProxyParam(request, proxyParam);
		proxyParam.setProxyType(ProxyType.JACKETT);
		String xml = get(proxyParam);
		
		// 无结果且关键字类似：标题 S2 02，尝试只用标题搜索
		xml = tryNewSearchKey(xml, proxyParam);
		
		responseToSonarr(response, xml, proxyParam);
		return false;
	}
}
