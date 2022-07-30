/**
 * @Title: JProxyLocaleResolver.java
 * @version V1.0
 */
package com.lckp.config;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.thymeleaf.util.StringUtils;

/**
 * @className: JProxyLocaleResolver
 * @description: 语言解析器
 * @date 2022年7月17日
 * @author LuckyPuppy514
 */

@Configuration
public class JProxyLocaleResolver implements LocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        String lang = httpServletRequest.getParameter("lang");
        Locale locale = Locale.getDefault();
        if (!StringUtils.isEmpty(lang)) {
            String[] splits = lang.split("_");
            locale = new Locale(splits[0], splits[1]);
        }
                
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new JProxyLocaleResolver();
    }
}

