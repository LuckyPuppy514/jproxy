/**  
* @Title: ServletInitializer.java
* @version V1.0  
*/
package com.lckp.jproxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @ClassName: ServletInitializer
 * @Description: tomcat启动类
 * @author Liujiawang
 * @date 2020年9月17日 上午8:55:14
 *
 */
public class ServletInitializer extends SpringBootServletInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServletInitializer.class);

	public ServletInitializer() {
		LOGGER.info("初始化ServletInitializer");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
}