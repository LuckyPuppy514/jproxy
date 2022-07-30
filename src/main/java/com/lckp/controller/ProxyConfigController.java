package com.lckp.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lckp.config.JProxy;
import com.lckp.constant.Message;
import com.lckp.constant.ProxyType;
import com.lckp.model.ProxyConfig;
import com.lckp.param.ProxyConfigModifyParam;
import com.lckp.param.ProxyConfigQueryParam;
import com.lckp.resp.ResVo;
import com.lckp.service.facade.IProxyConfigService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
* @ClassName: ProxyConfigController
* @Description: 代理配置Controller
* @author LuckyPuppy514
* @date 2022-07-15 14:39:56
*
*/
@RestController
@RequestMapping("/proxyConfig")
@Api(tags = "代理配置")
public class ProxyConfigController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyConfigController.class);
	
	@Autowired
	private IProxyConfigService proxyConfigService;
	@Autowired
	private MessageSource messageSource;
	
	@ApiOperation("查询")
	@GetMapping("/query")
	public ResVo<ProxyConfig> query(ProxyConfigQueryParam param) {
		LOGGER.info("代理配置 - 查询：{}", JSON.toJSONString(param));
		return ResVo.success(proxyConfigService.query(param));
	}
	
	@ApiOperation("修改")
	@PostMapping("/modify")
	public ResVo<String> modify(ProxyConfigModifyParam param, Locale locale) {
		LOGGER.info("代理配置 - 修改：{}", JSON.toJSONString(param));
		
		if (proxyConfigService.modify(param) > 0) {
			if (param.getProxyType().equals(ProxyType.Jackett.toString())) {
				JProxy.jackett.setProxyIp(param.getProxyIp());
				JProxy.jackett.setProxyPort(param.getProxyPort());
				JProxy.jackett.setProxyPath(param.getProxyPath());
				
			} else if(param.getProxyType().equals(ProxyType.Prowlarr.toString())) {
				JProxy.prowlarr.setProxyIp(param.getProxyIp());
				JProxy.prowlarr.setProxyPort(param.getProxyPort());
				JProxy.prowlarr.setProxyPath(param.getProxyPath());
			}
			
			return ResVo.success(Message.MODIFY_SUCCESS, messageSource, locale);
		}
		
		return ResVo.fail(Message.MODIFY_FAIL, messageSource, locale);
	}
}
