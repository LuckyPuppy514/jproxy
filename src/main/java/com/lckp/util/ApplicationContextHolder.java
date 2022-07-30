/**
 * @Title: ApplicationContextHolder.java
 * @version V1.0
 */
package com.lckp.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @className: ApplicationContextHolder
 * @description: 
 * @date 2022年6月10日
 * @author LuckyPuppy514
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware {

	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextHolder.context = applicationContext;
	}
	
	public static Object getBean(String name) {
		return context.getBean(name);
	}
	
	public static <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
	}
	
	public static <T> T getBean(Class<T> clazz, Object...args) {
		return context.getBean(clazz, args);
	}
	
	public static ApplicationContext getApplicationContext() {
		return context;
	}
}