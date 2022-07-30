package com.lckp.service.facade;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.lckp.param.RuleConfigAddParam;
import com.lckp.param.RuleConfigBatchParam;
import com.lckp.resp.RuleMarketServerShareResp;

/**
* @ClassName: IRuleMarketClientService
* @Description: 规则市场客户端service
* @author LuckyPuppy514
* @date 2022-07-27 06:46:33
*
*/
public interface IRuleMarketClientService {
	// 查询待分享规则详情
	List<RuleConfigAddParam> queryRuleNeedShare(List<RuleConfigBatchParam> paramList);
	// 更新分享信息
	int updateShare(List<RuleMarketServerShareResp> shareList);
	// 查询下载状态
	int queryDownloadStatus(String ruleId);
	
	// POST 请求
	String post(String path, Object param);
	
	// GET 请求
	String get(HttpServletRequest request);
}
