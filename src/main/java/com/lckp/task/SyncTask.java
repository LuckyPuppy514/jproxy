/**
 * @Title: SyncTask.java
 * @version V1.0
 */
package com.lckp.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lckp.param.RuleConfigAddParam;
import com.lckp.param.RuleConfigBatchParam;
import com.lckp.param.RuleConfigEditParam;
import com.lckp.resp.ResVo;
import com.lckp.service.facade.IRuleConfigService;
import com.lckp.service.facade.IRuleMarketClientService;
import com.lckp.service.facade.IRuleTestExampleService;

/**
 * @className: SyncTask
 * @description: 同步任务
 * @date 2022年7月20日
 * @author LuckyPuppy514
 */
@Component
@EnableScheduling
public class SyncTask {
	public static final Logger LOGGER = LoggerFactory.getLogger(SyncTask.class);
	
	@Autowired
	private IRuleConfigService ruleConfigService;
	@Autowired
	private IRuleTestExampleService ruleTestExampleService;
	@Autowired
	private IRuleMarketClientService ruleMarketClientService;
	
	@Scheduled(cron="0 0 0/1 * * ?")
	public void syncRuleMarketChange() {
		LOGGER.info("开始同步规则市场变化");
		List<String> ruleIdList = ruleConfigService.queryAllDownloadRuleId();
		if (ruleIdList == null || ruleIdList.size() == 0) {
			LOGGER.info("查无待同步规则");
			return;
		}
		List<RuleConfigBatchParam> paramList = new ArrayList<RuleConfigBatchParam>();
		for (String ruleId : ruleIdList) {
			RuleConfigBatchParam param = new RuleConfigBatchParam();
			param.setRuleId(ruleId);
			paramList.add(param);
		}
		
		@SuppressWarnings("unchecked")
		ResVo<List<RuleConfigAddParam>> result = (ResVo<List<RuleConfigAddParam>>) JSON.parseObject(ruleMarketClientService.post("/ruleMarket/server/sync", paramList), new TypeReference<ResVo<List<RuleConfigAddParam>>>(){});
		LOGGER.debug("同步规则市场请求结果：{}", JSON.toJSONString(result));
		
		for (RuleConfigAddParam ruleConfig : result.getData()) {
			if (StringUtils.isNotBlank(ruleConfig.getExampleContent())) {
				ruleTestExampleService.deleteByRuleId(ruleConfig.getRuleId());
				String[] contents = ruleConfig.getExampleContent().split("\\n");
				ruleTestExampleService.add(ruleConfig.getRuleId(), contents);
			}
			RuleConfigEditParam param = new RuleConfigEditParam();
			param.setRuleId(ruleConfig.getRuleId());
			param.setRuleLanguage(ruleConfig.getRuleLanguage());
			param.setRuleType(ruleConfig.getRuleType());
			param.setRegularType(ruleConfig.getRegularType());
			param.setExecuteRule(ruleConfig.getExecuteRule());
			param.setExecutePriority(ruleConfig.getExecutePriority());
			param.setRemark(ruleConfig.getRemark());
			param.setRegularMatch(ruleConfig.getRegularMatch());
			param.setRegularReplace(ruleConfig.getRegularReplace());
			ruleConfigService.editRuleConfig(param);
		}		
		
		LOGGER.info("同步规则市场变化完毕");
	}
}
