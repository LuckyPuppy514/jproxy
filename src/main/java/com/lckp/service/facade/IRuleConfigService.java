package com.lckp.service.facade;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lckp.model.RuleConfig;
import com.lckp.param.RuleConfigAddParam;
import com.lckp.param.RuleConfigBatchParam;
import com.lckp.param.RuleConfigEditParam;
import com.lckp.param.RuleConfigQueryParam;

/**
* @ClassName: IRuleConfigService
* @Description: 规则配置service
* @author LuckyPuppy514
* @date 2022-07-19 06:34:23
*
*/
public interface IRuleConfigService {
	// 新增规则配置
	public int add(RuleConfigAddParam param);
	// 查询规则配置
	IPage<RuleConfig> query(RuleConfigQueryParam param);
	// 查询规则名称列表
	List<RuleConfig> queryRuleNameList();
	// 删除规则配置
	int delete(List<RuleConfigBatchParam> paramList);
	// 变更有效状态
	int changeValidstatus(List<RuleConfigBatchParam> paramList, String validstatus);
	// 导出规则配置
	List<RuleConfigAddParam> export(List<RuleConfigBatchParam> paramList);
	// 导入规则配置
	int importRuleConfig(List<RuleConfig> ruleConfigList);
	// 查询规则配置
	List<RuleConfig> query(String regularType);
	
	// 根据规则 ID 查询
	RuleConfigEditParam queryByRuleId(String ruleId);
	// 编辑规则配置
	int editRuleConfig(RuleConfigEditParam param);
	// 增加下载次数
	int addDownloadCount(List<RuleConfigBatchParam> paramList);
	
	// 查询所有下载的规则ID
	List<String> queryAllDownloadRuleId();
	// 查询所有规则ID
	List<String> queryAllRuleId();
}
