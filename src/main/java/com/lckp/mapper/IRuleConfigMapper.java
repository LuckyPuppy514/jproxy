package com.lckp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lckp.model.RuleConfig;
import com.lckp.param.RuleConfigAddParam;
import com.lckp.param.RuleConfigBatchParam;
import com.lckp.param.RuleConfigEditParam;
import com.lckp.param.RuleConfigQueryParam;

/**
* @ClassName: IRuleConfigMapper
* @Description: 规则配置mapper
* @author LuckyPuppy514
* @date 2022-07-19 06:34:23
*
*/
public interface IRuleConfigMapper {
	// 新增规则配置
	public int add(RuleConfigAddParam param);
	// 查询规则配置
	IPage<RuleConfig> query(Page<?> page, @Param("param")RuleConfigQueryParam param);
	// 查询规则名称列表
	List<RuleConfig> queryRuleNameList();
	// 删除规则配置
	int delete(@Param("list") List<RuleConfigBatchParam> paramList);
	// 删除用例
	int deleteExample(@Param("list") List<RuleConfigBatchParam> paramList);
	// 变更有效状态
	int changeValidstatus(@Param("list") List<RuleConfigBatchParam> paramList, @Param("validstatus") String validstatus);
	// 导出规则配置
	List<RuleConfigAddParam> export(@Param("list") List<RuleConfigBatchParam> paramList);
	// 导入规则配置
	int importRuleConfig(@Param("list") List<RuleConfig> ruleConfigList);
	// 查询规则配置
	List<RuleConfig> queryNoPage(@Param("regularType") String regularType);
	
	// 根据规则 ID 查询
	RuleConfigEditParam queryByRuleId(@Param("ruleId") String ruleId);
	// 编辑规则配置
	int editRuleConfig(RuleConfigEditParam param);
	
	// 增加下载次数
	int addDownloadCount(@Param("list") List<RuleConfigBatchParam> paramList);
	
	// 查询所有下载的规则ID
	List<String> queryAllDownloadRuleId();
	// 查询所有规则ID
	List<String> queryAllRuleId();
}
