package com.lckp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import com.lckp.service.facade.IRuleTestExampleService;
import com.lckp.util.Generator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;


/**
* @ClassName: RuleMarketServerController
* @Description: 规则市场服务端Controller
* @author LuckyPuppy514
* @date 2022-07-27 06:14:46
*
*/
@RestController
@RequestMapping("/ruleMarket/server")
@Api(tags = "规则市场服务端")
public class RuleMarketServerController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RuleMarketServerController.class);
	
	@Autowired
	private IRuleConfigService ruleConfigService;
	@Autowired
	private IRuleTestExampleService ruleTestExampleService;
	@Autowired
	private MessageSource messageSource;
	
	@ApiOperation("分享")
	@PostMapping("/share")
	public ResVo<List<RuleMarketServerShareResp>> share(@RequestBody List<RuleConfigAddParam> paramList, HttpServletRequest request) throws IOException {
		List<RuleMarketServerShareResp> respList = new ArrayList<RuleMarketServerShareResp>();
		for (RuleConfigAddParam param : paramList) {
			param.setShareKey(Generator.createShareKey());
			
			RuleMarketServerShareResp resp = new RuleMarketServerShareResp();
			resp.setRuleId(param.getRuleId());
			resp.setShareKey(param.getShareKey());
			
			ruleConfigService.add(param);
			if (StringUtils.isNotBlank(param.getExampleContent())) {
				String[] contents = param.getExampleContent().split("\\n");
				ruleTestExampleService.add(param.getRuleId(), contents);
			}
			
			respList.add(resp);
		}
		return ResVo.success(respList);
	}
	
	
	@ApiOperation("查询")
	@PostMapping("/query")
	public ResVo<Pagination<RuleConfig>> query(@RequestBody RuleConfigQueryParam param, @ApiIgnore Locale locale) {
		param.setValidstatus(1);
		LOGGER.info("规则市场服务端 - 查询：{}", JSON.toJSONString(param));
		
		if (param.getDownloadStatus() != null) {
			if(param.getRuleIdList() == null || param.getRuleIdList().size() == 0) {
				List<String> ruleIdList = new ArrayList<String>();
				ruleIdList.add("NODATA");
				param.setRuleIdList(ruleIdList);
			}
		}
		
		Pagination<RuleConfig> result = new Pagination<>();
		IPage<RuleConfig> page = ruleConfigService.query(param);
		result.setPageIndex(page.getCurrent());
		result.setPageSize(page.getSize());
		result.setTotal(page.getTotal());
		result.setPageCount(page.getPages());
		result.setList(page.getRecords());
		
		for (RuleConfig ruleConfig : result.getList()) {
			ruleConfig.setRuleType(messageSource.getMessage("PAGE." + ruleConfig.getRuleType().toUpperCase(), null, locale));
			ruleConfig.setRegularType(messageSource.getMessage("PAGE.REGULAR_TYPE." + ruleConfig.getRegularType().toUpperCase(), null, locale));
			ruleConfig.setExecuteRule(messageSource.getMessage("PAGE.EXECUTE_RULE." + ruleConfig.getExecuteRule().toUpperCase(), null, locale));
			ruleConfig.setShareKey(null);
		}
		
		return ResVo.success(result);
	}
	
	@ApiOperation("下载")
	@PostMapping("/download")
	public ResVo<List<RuleConfigAddParam>> download(@RequestBody List<RuleConfigBatchParam> paramList) {
		List<RuleConfigAddParam> ruleConfigList = ruleConfigService.export(paramList);
		for (RuleConfigAddParam ruleConfig : ruleConfigList) {
			ruleConfig.setShareKey(null);
		}
		ruleConfigService.addDownloadCount(paramList);
		return ResVo.success(ruleConfigList);
	}
	
	@ApiOperation("同步")
	@PostMapping("/sync")
	public ResVo<List<RuleConfigAddParam>> sync(@RequestBody List<RuleConfigBatchParam> paramList) {
		List<RuleConfigAddParam> ruleConfigList = ruleConfigService.export(paramList);
		for (RuleConfigAddParam ruleConfig : ruleConfigList) {
			ruleConfig.setShareKey(null);
		}
		return ResVo.success(ruleConfigList);
	}
	
	@ApiOperation("编辑")
	@PostMapping("/edit")
	public ResVo<String> edit(@RequestBody RuleConfigEditParam param, @ApiIgnore Locale locale) {
		LOGGER.info("规则市场服务端 - 编辑：{}", JSON.toJSONString(param));
		if (StringUtils.isBlank(param.getShareKey())) {
			return ResVo.fail("share key can not be null");
		}
		if (ruleConfigService.editRuleConfig(param) > 0) {
			ruleTestExampleService.deleteByRuleId(param.getRuleId());
			if (StringUtils.isNotBlank(param.getExampleContent())) {
				String[] contents = param.getExampleContent().split("\\n");
				ruleTestExampleService.add(param.getRuleId(), contents);
			}
			return ResVo.success(Message.EDIT_SUCCESS, messageSource, locale);
		}

		return ResVo.fail(Message.EDIT_FAIL, messageSource, locale);
	}
}
