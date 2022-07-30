package com.lckp.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.activation.MimetypesFileTypeMap;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lckp.constant.Message;
import com.lckp.model.RuleConfig;
import com.lckp.param.RuleConfigAddParam;
import com.lckp.param.RuleConfigBatchParam;
import com.lckp.param.RuleConfigEditParam;
import com.lckp.param.RuleConfigQueryParam;
import com.lckp.resp.Pagination;
import com.lckp.resp.ResVo;
import com.lckp.service.facade.IRuleConfigService;
import com.lckp.service.facade.IRuleTestExampleService;
import com.lckp.util.Generator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;


/**
* @ClassName: RuleConfigController
* @Description: 规则配置Controller
* @author LuckyPuppy514
* @date 2022-07-19 06:34:23
*
*/
@RestController
@RequestMapping("/ruleConfig")
@Api(tags = "规则配置")
public class RuleConfigController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RuleConfigController.class);
	
	@Autowired
	private IRuleConfigService ruleConfigService;
	@Autowired
	private IRuleTestExampleService ruleTestExampleService;
	@Autowired
	private RuleMarketClientController ruleMarketClientController;
	
	@Autowired
	private MessageSource messageSource;
	
	@ApiOperation("测试")
	@PostMapping("/test")
	public ResVo<String> test(RuleConfigAddParam param, @ApiIgnore Locale locale) {
		LOGGER.info("规则配置 - 测试：{}", JSON.toJSONString(param));
		
		String[] exampleContents = param.getExampleContent().split("\\n");
		StringBuffer buffer = new StringBuffer();
		for (String exampleContent : exampleContents) {
			buffer.append(exampleContent.replaceAll(param.getRegularMatch(), param.getRegularReplace())+"\n");
		}
		
		return ResVo.success(buffer.toString());
	}
	
	@ApiOperation("新增")
	@PostMapping("/add")
	public ResVo<String> add(RuleConfigAddParam param, @ApiIgnore Locale locale) {
		LOGGER.info("规则配置 - 新增：{}", JSON.toJSONString(param));
		
		param.setRuleId(Generator.createRuleId());
		if (ruleConfigService.add(param) > 0) {
			String[] contents = param.getExampleContent().split("\\n");
			ruleTestExampleService.add(param.getRuleId(), contents);
			return ResVo.success(Message.ADD_SUCCESS, messageSource, locale);
		}
		return ResVo.fail(Message.ADD_FAIL, messageSource, locale);
	}
	
	@ApiOperation("查询")
	@GetMapping("/query")
	public ResVo<Pagination<RuleConfig>> query(RuleConfigQueryParam param, @ApiIgnore Locale locale) {
		LOGGER.info("规则配置 - 查询：{}", JSON.toJSONString(param));
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
		}
		
		return ResVo.success(result);
	}
	
	@ApiOperation("查询规则名称列表")
	@GetMapping("/queryRuleNameList")
	public ResVo<List<RuleConfig>> queryRuleNameList() {
		LOGGER.info("规则配置 - 查询规则名称列表");
		return ResVo.success(ruleConfigService.queryRuleNameList());
	}
	
	@ApiOperation("删除")
	@PostMapping("/delete")
	public ResVo<String> delete(@RequestBody List<RuleConfigBatchParam> paramList, @ApiIgnore Locale locale) {
		LOGGER.info("规则配置 - 删除：{}", JSON.toJSONString(paramList));
		if (paramList == null || paramList.size() == 0) {
			return ResVo.fail(Message.DELETE_FAIL, messageSource, locale);
		}
		int sum = paramList.size();
		int success = ruleConfigService.delete(paramList);
		if (sum == success) {
			return ResVo.success(Message.DELETE_SUCCESS, messageSource, locale);
		}
		if(sum > success) {
			return ResVo.fail(Message.DATA_DELETE_CONTAIN_VALID_DATA, messageSource, locale);
		}
		return ResVo.fail(Message.DELETE_FAIL, messageSource, locale);
	}
	
	@ApiOperation("变更有效状态")
	@PostMapping("/changeValidstatus")
	public ResVo<String> changeValidstatus(@RequestBody List<RuleConfigBatchParam> paramList, @RequestParam String validstatus, @ApiIgnore Locale locale) {
		LOGGER.info("规则配置 - 变更有效状态：{} - {}", validstatus, JSON.toJSONString(paramList));
		
		if (ruleConfigService.changeValidstatus(paramList, validstatus) > 0) {
			return ResVo.success(Message.CHANGE_SUCCESS, messageSource, locale);
		}
		
		return ResVo.fail(Message.CHANGE_FAIL, messageSource, locale);
	}
	
	@ApiOperation("导出")
	@GetMapping("/export")
	public void export(String ruleIds, HttpServletResponse response) throws IOException {
		LOGGER.info("规则配置 - 导出：{}", JSON.toJSONString(ruleIds));
		List<RuleConfigBatchParam> paramList = new ArrayList<RuleConfigBatchParam>();
		String[] ruleIdArray = ruleIds.split(",");
		for (String ruleId : ruleIdArray) {
			RuleConfigBatchParam param = new RuleConfigBatchParam();
			param.setRuleId(ruleId);
			paramList.add(param);
		}
		List<RuleConfigAddParam> ruleConfigList = ruleConfigService.export(paramList);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String filename = format.format(new Date()) + ".json";
		File ruleConfigFile = File.createTempFile("/tmp", filename);
		BufferedWriter writer1 = new BufferedWriter(new FileWriter(ruleConfigFile));
		writer1.write("[\n");
		boolean flag = false;
		for (RuleConfigAddParam ruleConfig : ruleConfigList) {
			writer1.write("\t");
			if (flag) {
				writer1.write(", ");
			}
			writer1.write(JSON.toJSONString(ruleConfig));
			writer1.newLine();
			flag = true;
		}
		writer1.write("]");
		writer1.flush();
		writer1.close();
		
        response.setHeader("Content-type", new MimetypesFileTypeMap().getContentType(ruleConfigFile));
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("utf-8"), "iso-8859-1"));
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Content-Length", String.valueOf(ruleConfigFile.length()));
        
        BufferedReader reader = new BufferedReader(new FileReader(ruleConfigFile));
        BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
        char[] buffer = new char[1024];
        while (reader.read(buffer) > -1) {
        	writer2.write(buffer);
		}
        writer2.flush();
        writer1.close();
        reader.close();
        if(ruleConfigFile.exists()) {
        	ruleConfigFile.delete();
        }
	}
	
	@ApiOperation("导入")
	@PostMapping("/import")
	public ResVo<String> importRuleConfig(MultipartFile file, @ApiIgnore Locale locale) {
		File jsonFile = null;
		BufferedReader reader = null;
		try {
			LOGGER.info("规则配置 - 导入：{}", JSON.toJSONString(file.getOriginalFilename()));
			jsonFile = File.createTempFile("/tmp", file.getOriginalFilename());
			file.transferTo(jsonFile);
			reader = new BufferedReader(new FileReader(jsonFile));
			String line = null;
			StringBuffer buffer = new StringBuffer();
			while ((line = reader.readLine()) != null) {
	        	buffer.append(line);
			}
			
			@SuppressWarnings("unchecked")
			List<RuleConfigAddParam> ruleConfigList = (List<RuleConfigAddParam>) JSON.parseObject(buffer.toString(), new TypeReference<List<RuleConfigAddParam>>(){});
			for (RuleConfigAddParam ruleConfig : ruleConfigList) {
				if(ruleConfigService.add(ruleConfig) > 0) {
					if(StringUtils.isNotBlank(ruleConfig.getExampleContent())) {
						String[] contents = ruleConfig.getExampleContent().split("\\n");
						ruleTestExampleService.add(ruleConfig.getRuleId(), contents);
					}
				}
			}
	        return ResVo.success(Message.IMPORT_SUCCESS, messageSource, locale);
	        
		} catch (Exception e) {
			LOGGER.error("规则配置 - 导入出错：{}", e);
			return ResVo.fail(Message.IMPORT_FAIL, messageSource, locale);
			
		} finally {
	        if (reader != null) {
				try {
					reader.close();
				} catch (Exception e2) {
					LOGGER.error("关闭流失败：{}", e2);
				}
			}
			if(jsonFile.exists()) {
				jsonFile.delete();
			}
		}
	}
	
	@ApiOperation("根据规则 ID 查询")
	@GetMapping("/queryByRuleId")
	public ResVo<RuleConfigEditParam> queryByRuleId(String ruleId) {
		LOGGER.info("规则配置 - 根据规则 ID 查询：{}", ruleId);
		return ResVo.success(ruleConfigService.queryByRuleId(ruleId));
	}
	
	@ApiOperation("编辑")
	@PostMapping("/edit")
	public ResVo<String> edit(RuleConfigEditParam param, @ApiIgnore Locale locale, HttpServletRequest request) {
		LOGGER.info("规则配置 - 编辑：{}", JSON.toJSONString(param));
		ruleTestExampleService.deleteByRuleId(param.getRuleId());
		String[] contents = param.getExampleContent().split("\\n");
		ruleTestExampleService.add(param.getRuleId(), contents);
		if (ruleConfigService.editRuleConfig(param) > 0) {
			if (StringUtils.isNotBlank(param.getShareKey()) && !param.getShareKey().equals("******")) {
				ruleMarketClientController.edit(param, request, locale);
			}
			
			return ResVo.success(Message.EDIT_SUCCESS, messageSource, locale);
		}
		
		return ResVo.success(Message.EDIT_FAIL, messageSource, locale);
	}
}
