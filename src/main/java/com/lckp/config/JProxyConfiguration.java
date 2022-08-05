/**
 * @Title: JProxyConfiguration.java
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
 * @className: JProxyConfiguration
 * @description: JProxy 配置
 * @date 2022年8月4日
 * @author LuckyPuppy514
 */
@Configuration
public class JProxyConfiguration {
	private static final Logger LOGGER = LoggerFactory.getLogger(JProxyConfiguration.class);
		
	@Autowired
	private IProxyConfigService proxyConfigService;
	@Autowired
	private IRuleConfigService ruleConfigService;
	
	// 代理配置
	public static ProxyConfig jackett;
	public static ProxyConfig prowlarr;
	public static ProxyConfig qBittorrent;
	
	// 规则列表
	public static List<RuleConfig> searchRuleList;
	public static List<RuleConfig> resultRuleList;
	
	// 等待数据库初始化时间
	private static final Long WAIT_TIME = 6000L;
	
	/**
	 * 
	 * 
	 * @description: JProxy 配置初始化
	 */
	@PostConstruct
	public void init() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 等待数据库初始化后再执行
				try {
					Thread.sleep(WAIT_TIME);
				} catch (InterruptedException e) {
					LOGGER.error("等待数据库初始化出错", e);
				}
				
				// 初始化代理配置
				initProxyConfig();
				
				// 初始化规则配置
				initRuleConfig();
			}
		}).start();
	}
	
	/**
	 * 
	 * 
	 * @description: 初始化所有代理配置
	 */
	public void initProxyConfig() {
		ProxyConfigQueryParam param = new ProxyConfigQueryParam();
		param.setProxyType(ProxyType.Jackett.toString());
		jackett = proxyConfigService.query(param);
		
		param.setProxyType(ProxyType.Prowlarr.toString());
		prowlarr = proxyConfigService.query(param);
		
		param.setProxyType(ProxyType.qBittorrent.toString());
		qBittorrent = proxyConfigService.query(param);
		
		LOGGER.info("加载代理配置成功");
		LOGGER.info("Jackett: {}", JSON.toJSONString(jackett));
		LOGGER.info("Prowlarr: {}", JSON.toJSONString(prowlarr));
		LOGGER.info("qBittorrent: {}", JSON.toJSONString(qBittorrent));
	}
    
	
	/**
	 * 
	 * 
	 * @description: 初始化所有规则
	 */
	public void initRuleConfig() {
		searchRuleList = ruleConfigService.query(RegularType.Search.toString());
		resultRuleList = ruleConfigService.query(RegularType.Result.toString());
		
		LOGGER.info("加载规则成功，搜索：{}，结果：{}", searchRuleList.size(), resultRuleList.size());
		LOGGER.debug("搜索规则：{}", JSON.toJSONString(searchRuleList));
		LOGGER.debug("结果规则：{}", JSON.toJSONString(resultRuleList));
	}
	
	/**
	 * 
	 * @return
	 * @description: 检查数据是否已经初始化
	 */
	public static boolean isInit() {
		if(jackett == null || prowlarr == null || qBittorrent == null || searchRuleList == null || resultRuleList == null) {
			LOGGER.info("数据未初始化，请稍等");
			return false;
		}
		return true;
	}
}
