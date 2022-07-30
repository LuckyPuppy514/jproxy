package com.lckp.service.spring;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.alibaba.fastjson.JSON;
import com.lckp.mapper.IRuleMarketClientMapper;
import com.lckp.param.RuleConfigAddParam;
import com.lckp.param.RuleConfigBatchParam;
import com.lckp.resp.RuleMarketServerShareResp;
import com.lckp.service.facade.IRuleMarketClientService;

import reactor.core.publisher.Mono;

/**
* @ClassName: RuleMarketClientServiceImpl
* @Description: 规则市场客户端serviceImpl
* @author LuckyPuppy514
* @date 2022-07-27 06:46:33
*
*/
@Service("ruleMarketClientService")
public class RuleMarketClientServiceImpl implements IRuleMarketClientService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RuleMarketClientServiceImpl.class);
			
	@Autowired
	private IRuleMarketClientMapper ruleMarketClientMapper;

	@Value("${api.rule-market}")
	private String ruleMarketApi;
	
	/**
	 * 查询待分享规则详情
	 */
	@Override
	public List<RuleConfigAddParam> queryRuleNeedShare(List<RuleConfigBatchParam> paramList) {
		return ruleMarketClientMapper.queryRuleNeedShare(paramList);
	}

	/**
	 * 更新分享信息
	 */
	@Override
	public int updateShare(List<RuleMarketServerShareResp> shareList) {
		int count = 0;
		for (RuleMarketServerShareResp share : shareList) {
			count = count + ruleMarketClientMapper.updateShare(share);
		}
		return count;
	}

	/**
	 * 查询下载状态
	 */
	@Override
	public int queryDownloadStatus(String ruleId) {
		Integer downloadStatus = ruleMarketClientMapper.queryDownloadStatus(ruleId);
		if (downloadStatus == null) {
			return 0;
		}
		return downloadStatus;
	}

	@Override
	public String post(String path, Object param) {
		path = clientToServer(path);
		LOGGER.debug("post: {}, {}", path, JSON.toJSONString(param));
		WebClient webClient = WebClient.create();
		Mono<ClientResponse> mono = webClient
				.post()
				.uri(ruleMarketApi + path)
				.header("timestamp", String.valueOf(System.currentTimeMillis()))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(param)
				.exchange();
		String responseBody = mono.block().bodyToMono(String.class).block();
		LOGGER.debug("responseBody: {}", responseBody);
		return responseBody;
	}

	@Override
	public String get(HttpServletRequest request) {
		String path = clientToServer(request.getServletPath());
		
		String paramString = "";
		Map<String, String[]> paramMap = request.getParameterMap();
		StringBuilder paramBuilder = new StringBuilder();
		for (String key : paramMap.keySet()) {
			String value = paramMap.get(key)[0];
			paramBuilder.append("&" + key + "=" + value);
		}
		if (StringUtils.isNotBlank(paramBuilder)) {
			paramString = paramBuilder.substring(1);
			if (!paramString.startsWith("?")) {
				paramString = "?" + paramString;
			}
		}
		
		LOGGER.debug("get: {}, {}", path, paramString);
		WebClient webClient = WebClient.create();
		Mono<ClientResponse> mono = webClient.get().uri(ruleMarketApi + path + paramString).header("timestamp", String.valueOf(System.currentTimeMillis())).exchange();
		ClientResponse response = mono.block();
		if (HttpStatus.OK == response.statusCode()) {
			String responseBody = response.bodyToMono(String.class).block();
			LOGGER.debug("responseBody: {}", responseBody);
			return responseBody;
		}
		
		return null;
	}
	
	private String clientToServer(String path) {
		return path.replace("client", "server");
	}

}
