/**
 * @Title: JProxy.java
 * @version V1.0
 */
package com.lckp.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSON;
import com.lckp.constant.ProxyType;
import com.lckp.constant.RegularType;
import com.lckp.model.ProxyConfig;
import com.lckp.model.RuleConfig;
import com.lckp.param.ProxyConfigQueryParam;
import com.lckp.service.facade.IProxyConfigService;
import com.lckp.service.facade.IRuleConfigService;

/**
 * @className: JProxy
 * @description: 初始化
 * @date 2022年7月21日
 * @author LuckyPuppy514
 */
@Configuration
public class JProxy {
	private static final Logger LOGGER = LoggerFactory.getLogger(JProxy.class);
	
	private static final Long WAIT_TIME = 3000L;
	
	@Autowired
	private IProxyConfigService proxyConfigService;
	@Autowired
	private IRuleConfigService ruleConfigService;
		
	public static ProxyConfig jackett;
	public static ProxyConfig prowlarr;
	
	public static List<RuleConfig> searchRuleList;
	public static List<RuleConfig> resultRuleList;
	
	@PostConstruct
	public void init() {
		Thread initThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// 等待数据库初始化后再执行
				try {
					Thread.sleep(WAIT_TIME);
				} catch (InterruptedException e) {
					LOGGER.error("等待数据库初始化失败：{}", e);
				}
				
				// 初始化代理配置
				initProxyConfig();
				
				// 初始化规则配置
				initRuleConfig();
			}
		});
		initThread.start();
	}
	
	public void initProxyConfig() {
		ProxyConfigQueryParam param = new ProxyConfigQueryParam();
		param.setProxyType(ProxyType.Jackett.toString());
		jackett = proxyConfigService.query(param);
		param.setProxyType(ProxyType.Prowlarr.toString());
		prowlarr = proxyConfigService.query(param);
	}
	
	public void initRuleConfig() {
		searchRuleList = ruleConfigService.query(RegularType.Search.toString());
		resultRuleList = ruleConfigService.query(RegularType.Result.toString());
		LOGGER.debug("加载规则成功：");
		for (RuleConfig ruleConfig : searchRuleList) {
			LOGGER.debug("搜索规则：{}", JSON.toJSONString(ruleConfig));
		}
		for (RuleConfig ruleConfig : resultRuleList) {
			LOGGER.debug("结果规则：{}", JSON.toJSONString(ruleConfig));
		}
	}
}
