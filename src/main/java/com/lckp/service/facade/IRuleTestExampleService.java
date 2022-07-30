package com.lckp.service.facade;

import java.util.List;

import com.lckp.param.RuleTestExampleBatchParam;
import com.lckp.param.RuleTestExampleQueryParam;
import com.lckp.resp.Pagination;
import com.lckp.resp.RuleTestExampleQueryResp;

/**
* @ClassName: IRuleTestExampleService
* @Description: 规则测试用例service
* @author LuckyPuppy514
* @date 2022-07-15 17:03:17
*
*/
public interface IRuleTestExampleService {
	// 查询规则测试用例
	Pagination<RuleTestExampleQueryResp> query(RuleTestExampleQueryParam param);
	
	// 删除规则测试用例
	int delete(List<RuleTestExampleBatchParam> paramList);
	
	// 新增规则测试用例
	int add(String ruleId, String[] contents);
	
	// 根据规则配置 ID 删除
	int deleteByRuleId(String ruleId);
}
