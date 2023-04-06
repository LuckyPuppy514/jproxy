package com.lckp.jproxy.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lckp.jproxy.constant.ValidStatus;
import com.lckp.jproxy.entity.SonarrRule;
import com.lckp.jproxy.model.request.SonarrRuleQueryRequest;

/**
 * <p>
 * SonarrRule 服务类
 * </p>
 *
 * @author LuckyPuppy514
 * @since 2023-03-20
 */
public interface ISonarrRuleService extends IService<SonarrRule> {
	/**
	 * 
	 * 通过 token 查询 Sonarr 规则
	 *
	 * @param token
	 * @return List<SonarrRule>
	 */
	public List<SonarrRule> query(String token);

	/**
	 * 
	 * 分页查询
	 *
	 * @param request
	 * @return IPage<SonarrRule>
	 */
	public IPage<SonarrRule> query(SonarrRuleQueryRequest request);

	/**
	 * 
	 * 同步
	 *
	 * @return boolean
	 */
	public boolean sync();

	/**
	 * 
	 * 切换有效状态
	 *
	 * @param idList
	 * @param validStatus
	 * @return boolean
	 */
	public boolean switchValidStatus(List<String> idList, ValidStatus validStatus);
}
