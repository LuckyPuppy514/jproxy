package com.lckp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lckp.param.RuleTestExampleBatchParam;
import com.lckp.param.RuleTestExampleQueryParam;
import com.lckp.resp.RuleTestExampleQueryResp;

/**
* @ClassName: IRuleTestExampleMapper
* @Description: 规则测试用例mapper
* @author LuckyPuppy514
* @date 2022-07-15 17:03:17
*
*/
public interface IRuleTestExampleMapper {
	// 查询规则测试用例
	IPage<RuleTestExampleQueryResp> query(Page<?> page, @Param("param")RuleTestExampleQueryParam param);
	// 查询规则测试用例
	List<RuleTestExampleQueryResp> queryNoPage(@Param("param")RuleTestExampleQueryParam param);
	
	// 删除规则测试用例
	int delete(List<RuleTestExampleBatchParam> paramList);
	
	// 新增规则测试用例
	int add(@Param("ruleId") String ruleId, @Param("contents") String[] contents);
	
	// 根据规则配置 ID 删除
	int deleteByRuleId(@Param("ruleId") String ruleId);
}
