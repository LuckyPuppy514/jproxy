package com.lckp.controller;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lckp.constant.Message;
import com.lckp.model.RuleConfig;
import com.lckp.param.RuleConfigAddParam;
import com.lckp.param.RuleConfigBatchParam;
import com.lckp.param.RuleConfigEditParam;
import com.lckp.param.RuleConfigQueryParam;
import com.lckp.resp.Pagination;
import com.lckp.resp.ResVo;
import com.lckp.resp.RuleMarketServerShareResp;
import com.lckp.service.facade.IRuleConfigService;
import com.lckp.service.facade.IRuleMarketClientService;
import com.lckp.service.facade.IRuleTestExampleService;
import com.lckp.task.SyncTask;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;


/**
* @ClassName: RuleMarketClientController
* @Description: 规则市场客户端Controller
* @author LuckyPuppy514
* @date 2022-07-27 06:14:14
*
*/
@RestController
@RequestMapping("/ruleMarket/client")
@Api(tags = "规则市场客户端")
@SuppressWarnings("unchecked")
public class RuleMarketClientController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RuleMarketClientController.class);
	
	@Autowired
	private IRuleMarketClientService ruleMarketClientService;
	@Autowired
	private IRuleConfigService ruleConfigService;
	@Autowired
	private IRuleTestExampleService ruleTestExampleService;
	@Autowired
	private SyncTask syncTask;
	
	@Autowired
	private MessageSource messageSource;
	
	@ApiOperation("分享")
	@PostMapping("/share")
	public ResVo<String> share(@RequestBody List<RuleConfigBatchParam> paramList, HttpServletRequest request, @ApiIgnore Locale locale) throws IOException {
		LOGGER.info("规则市场客户端 - 分享：{}", JSON.toJSONString(paramList));
		List<RuleConfigAddParam> ruleList = ruleMarketClientService.queryRuleNeedShare(paramList);
		LOGGER.debug("待分享规则：{}", JSON.toJSONString(ruleList));
		if (ruleList == null || ruleList.size() < paramList.size()) {
			return ResVo.fail(Message.DATA_SELECTED_INCLUDE_SHARED_OR_UNABLE_SHARE_DATA, messageSource, locale);
		}
		
		String path = request.getServletPath();
		ResVo<List<RuleMarketServerShareResp>> result = (ResVo<List<RuleMarketServerShareResp>>) JSON.parseObject(ruleMarketClientService.post(path, ruleList), new TypeReference<ResVo<List<RuleMarketServerShareResp>>>(){});
		if (result == null) {
			return ResVo.fail(Message.MARKET_SERVER_ERROR, messageSource, locale);
		}
		
		if (result.getCode() != 200) {
			return ResVo.fail(result.getMsg());
		}
		
		ruleMarketClientService.updateShare(result.getData());
		return ResVo.success(Message.SHARE_SUCCESS, messageSource, locale);
	}
	
	@ApiOperation("查询")
	@GetMapping("/query")
	public ResVo<Pagination<RuleConfig>> query(RuleConfigQueryParam param, @ApiIgnore Locale locale, HttpServletRequest request, HttpServletResponse response) throws IOException {
		LOGGER.info("规则市场客户端 - 查询：{}", JSON.toJSONString(param));
		if (null != param.getDownloadStatus()) {
			param.setRuleIdList(ruleConfigService.queryAllRuleId());
		}
		ResVo<Pagination<RuleConfig>> result = (ResVo<Pagination<RuleConfig>>) JSON.parseObject(ruleMarketClientService.post(request.getServletPath() + "?lang=" + locale, param), new TypeReference<ResVo<Pagination<RuleConfig>>>(){});
		if (result == null) {
			return ResVo.fail(Message.MARKET_SERVER_ERROR, messageSource, locale);
		}
		for (RuleConfig ruleConfig : result.getData().getList()) {
			ruleConfig.setDownloadStatus(ruleMarketClientService.queryDownloadStatus(ruleConfig.getRuleId()));
		}
		
		return result;
	}
	
	@ApiOperation("下载")
	@PostMapping("/download")
	public ResVo<String> download(@RequestBody List<RuleConfigBatchParam> paramList, HttpServletRequest request, @ApiIgnore Locale locale) {
		String path = request.getServletPath();
		ResVo<List<RuleConfigAddParam>> result = (ResVo<List<RuleConfigAddParam>>) JSON.parseObject(ruleMarketClientService.post(path, paramList), new TypeReference<ResVo<List<RuleConfigAddParam>>>(){});
		if (result == null || result.getData() == null) {
			return ResVo.fail(Message.MARKET_SERVER_ERROR, messageSource, locale);
		}
		if (result.getCode() != 200) {
			return ResVo.fail(result.getMsg());
		}
		
		int success = 0;
		for (RuleConfigAddParam ruleConfig : result.getData()) {
			ruleConfig.setShareKey("******");
			if(ruleConfigService.add(ruleConfig) > 0) {
				if(StringUtils.isNotBlank(ruleConfig.getExampleContent())) {
					String[] contents = ruleConfig.getExampleContent().split("\\n");
					ruleTestExampleService.add(ruleConfig.getRuleId(), contents);
				}
				success++;
			}
		}
		if (result.getData().size() == success) {
			return ResVo.success(Message.DOWNLOAD_SUCCESS, messageSource, locale);
		}
		return ResVo.fail(Message.DOWNLOAD_FAIL, messageSource, locale);
	}
	
	@ApiOperation("编辑")
	@PostMapping("/edit")
	public ResVo<String> edit(RuleConfigEditParam param, HttpServletRequest request, @ApiIgnore Locale locale) {
		LOGGER.info("规则市场客户端 - 编辑：{}", JSON.toJSONString(param));
		String path = "/ruleMarket/client/edit";
		
		ResVo<String> result = (ResVo<String>) JSON.parseObject(ruleMarketClientService.post(path, param), new TypeReference<ResVo<String>>(){});
		
		if (result == null) {
			return ResVo.fail(Message.MARKET_SERVER_ERROR, messageSource, locale);
		}
		
		return result;
	}
	
	@ApiOperation("同步")
	@PostMapping("/sync")
	public ResVo<String> sync() {
		syncTask.syncRuleMarketChange();
		return ResVo.success();
	}
}
