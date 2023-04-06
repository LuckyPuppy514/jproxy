package com.lckp.jproxy.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lckp.jproxy.constant.ValidStatus;
import com.lckp.jproxy.entity.RadarrRule;
import com.lckp.jproxy.model.request.RadarrRuleQueryRequest;

/**
 * <p>
 * RadarrRule 服务类
 * </p>
 *
 * @author LuckyPuppy514
 * @since 2023-03-20
 */
public interface IRadarrRuleService extends IService<RadarrRule> {
	/**
	 * 
	 * 通过 token 查询 Radarr 规则
	 *
	 * @param token
	 * @return List<RadarrRule>
	 */
	public List<RadarrRule> query(String token);

	/**
	 * 
	 * 分页查询
	 *
	 * @param request
	 * @return IPage<RadarrRule>
	 */
	public IPage<RadarrRule> query(RadarrRuleQueryRequest request);

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
