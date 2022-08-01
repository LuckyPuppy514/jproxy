package com.lckp.controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
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
import springfox.documentation.annotations.ApiIgnore;


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
	
	@ApiOperation("测试")
	@GetMapping("/test")
	public ResVo<String> test(ProxyConfigModifyParam param, @ApiIgnore Locale locale) {
		LOGGER.info("代理配置 - 测试：{}", JSON.toJSONString(param));
		boolean reachable = false;
		Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(param.getProxyIp(), Integer.parseInt(param.getProxyPort())));
            reachable = true;
            
        } catch (Exception e) {
            LOGGER.error("代理配置 - 测试：{}", e);
            
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            	LOGGER.error("代理配置 - 关闭 Socket：{}", e);
            }
        }
        
        if (reachable) {
    		return ResVo.success(Message.TIPS_REACHABLE, messageSource, locale);
		}
        
		return ResVo.fail(Message.TIPS_UNREACHABLE, messageSource, locale);
	}
	
	@ApiOperation("查询")
	@GetMapping("/query")
	public ResVo<ProxyConfig> query(ProxyConfigQueryParam param) {
		LOGGER.info("代理配置 - 查询：{}", JSON.toJSONString(param));
		return ResVo.success(proxyConfigService.query(param));
	}
	
	@ApiOperation("修改")
	@PostMapping("/modify")
	public ResVo<String> modify(ProxyConfigModifyParam param, @ApiIgnore Locale locale) {
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
	
	public static boolean isHostConnectable(String host, int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
