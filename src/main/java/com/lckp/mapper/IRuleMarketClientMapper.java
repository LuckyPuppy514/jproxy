package com.lckp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lckp.param.RuleConfigAddParam;
import com.lckp.param.RuleConfigBatchParam;
import com.lckp.resp.RuleMarketServerShareResp;

/**
* @ClassName: IRuleMarketClientMapper
* @Description: 规则市场客户端mapper
* @author LuckyPuppy514
* @date 2022-07-27 06:46:33
*
*/
public interface IRuleMarketClientMapper {
	// 查询待分享规则详情
	List<RuleConfigAddParam> queryRuleNeedShare(@Param("list") List<RuleConfigBatchParam> paramList);
	// 更新分享信息
	int updateShare(RuleMarketServerShareResp share);
	// 查询下载状态
	Integer queryDownloadStatus(@Param("ruleId") String ruleId);
}
