package com.lckp.jproxy.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lckp.jproxy.config.FormatConfig;
import com.lckp.jproxy.interceptor.ApiProxy;
import com.lckp.jproxy.param.FormatAccurateTestParam;
import com.lckp.jproxy.resp.FormatAccurateTestResp;
import com.lckp.jproxy.resp.ResVo;
import com.lckp.jproxy.service.facade.IFormatService;
import com.lckp.jproxy.task.UpdateOfficialFormatTask;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
* @ClassName: FormatController
* @Description: 格式化Controller
* @author LuckyPuppy514
* @date 2022-06-17 14:30:20
*
*/
@RestController
@RequestMapping("/format")
@Api(tags = "格式化")
public class FormatController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FormatController.class);
	
	@Autowired
	private IFormatService formatService;
	@Autowired
	private FormatConfig formatConfig;
	@Value("${path.profiles.custom}")
	private String customPath;
	
	@Autowired
	private UpdateOfficialFormatTask updateOfficialFormatTask;
	@Autowired
	private ContextRefresher contextRefresher;
	
	@GetMapping("/accurateTest")
	@ApiOperation("精确匹配测试")
	public ResVo<FormatAccurateTestResp> accurateTest(FormatAccurateTestParam param) {
		LOGGER.info("精确匹配测试：{}", JSON.toJSONString(param));
		return ResVo.success(formatService.accurateTest(param));
	}
	
	@GetMapping("/syncOfficialYml")
	@ApiOperation("同步官方规则")
	public ResVo<String> syncOfficialYml() {
		updateOfficialFormatTask.run();
		return ResVo.success();
	}
	
	
	@PostMapping("/queryCustomYml")
	@ApiOperation("查询自定义规则")
	public String queryCustomYml() throws IOException {
		if (StringUtils.isBlank(customPath)) {
			customPath = this.getClass().getResource("/application-format-custom.yml").getPath();
		}

		File customYmlFile = new File(customPath);
		BufferedReader reader = new BufferedReader(new FileReader(customYmlFile));
		StringBuilder builder = new StringBuilder();
		String line = reader.readLine();
		while (line != null) {
			builder.append(line + "\n");
			line = reader.readLine();
		}
		reader.close();
		return builder.toString();
	}
	
	@PostMapping("/editCustomYml")
	@ApiOperation("编辑自定义规则")
	public String editCustom(@RequestBody String customYml) throws IOException {
		if (StringUtils.isBlank(customPath)) {
			customPath = this.getClass().getResource("/application-format-custom.yml").getPath();
		}
		File customYmlFile = new File(customPath);
		BufferedWriter writer = new BufferedWriter(new FileWriter(customYmlFile));
		writer.write(customYml);
		writer.flush();
		writer.close();
		contextRefresher.refresh();
		initFormatRule();
		return customYml;
	}
	
	@GetMapping("/switchStatus")
	@ApiOperation("开启/关闭格式化（重启失效）")
	public ResVo<String> switchStatus() {
		formatConfig.setEnable(!formatConfig.isEnable());
		return ResVo.success("当前状态：" + formatConfig.isEnable());
	}
	
	
	@PostConstruct
	public void initFormatRule() {
		updateOfficialFormatTask.run();
		
		LOGGER.info("开始初始化规则");
		ApiProxy.initFormatRule(formatConfig);
		LOGGER.info("初始化规则完毕");
	}
}
