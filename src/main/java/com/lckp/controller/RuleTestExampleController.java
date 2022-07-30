package com.lckp.controller;

import java.util.List;
import java.util.Locale;

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
import com.lckp.constant.Message;
import com.lckp.model.RuleTestExample;
import com.lckp.param.RuleTestExampleBatchParam;
import com.lckp.param.RuleTestExampleQueryParam;
import com.lckp.resp.Pagination;
import com.lckp.resp.ResVo;
import com.lckp.resp.RuleTestExampleQueryResp;
import com.lckp.service.facade.IRuleTestExampleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;


/**
* @ClassName: RuleTestExampleController
* @Description: 规则测试用例Controller
* @author LuckyPuppy514
* @date 2022-07-15 17:03:17
*
*/
@RestController
@RequestMapping("/ruleTestExample")
@Api(tags = "规则测试用例")
public class RuleTestExampleController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RuleTestExampleController.class);
	
	@Autowired
	private IRuleTestExampleService ruleTestExampleService;
	@Autowired
	private MessageSource messageSource;
	
	@ApiOperation("查询")
	@GetMapping("/query")
	public ResVo<Pagination<RuleTestExampleQueryResp>> query(RuleTestExampleQueryParam param) {
		LOGGER.info("规则测试用例 - 查询：{}", JSON.toJSONString(param));
		return ResVo.success(ruleTestExampleService.query(param));
	}
	
	@ApiOperation("新增")
	@PostMapping("/add")
	public ResVo<String> add(RuleTestExample param, @ApiIgnore Locale locale) {
		LOGGER.info("格式化规则 - 新增: {}", JSON.toJSONString(param));
		
		String[] contents = param.getExampleContent().split("\\n");
		if (ruleTestExampleService.add(param.getRuleId(), contents) > 0) {
			return ResVo.success(Message.ADD_SUCCESS, messageSource, locale);
		}
		
		return ResVo.fail(Message.ADD_FAIL, messageSource, locale);
	}
	
	@ApiOperation("删除")
	@PostMapping("/delete")
	public ResVo<String> delete(@RequestBody List<RuleTestExampleBatchParam> paramList, @ApiIgnore Locale locale) {
		LOGGER.info("规则测试用例 - 删除：{}", JSON.toJSONString(paramList));

		if (paramList.size() > 0 && ruleTestExampleService.delete(paramList) > 0) {
			return ResVo.success(Message.DELETE_SUCCESS, messageSource, locale);
		}
		return ResVo.fail(Message.DELETE_FAIL, messageSource, locale);
	}
}
