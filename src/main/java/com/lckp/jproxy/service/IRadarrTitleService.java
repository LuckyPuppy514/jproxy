package com.lckp.jproxy.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lckp.jproxy.entity.RadarrRule;
import com.lckp.jproxy.entity.RadarrTitle;
import com.lckp.jproxy.model.request.RadarrTitleQueryRequest;

/**
 * <p>
 * RadarrTitle 服务类
 * </p>
 *
 * @author LuckyPuppy514
 * @since 2023-03-20
 */
public interface IRadarrTitleService extends IService<RadarrTitle> {
	/**
	 * 
	 * 同步
	 *
	 * boolean
	 */
	public boolean sync();

	/**
	 * 
	 * 查询所有标题
	 *
	 * @return List<RadarrTitle>
	 */
	public List<RadarrTitle> queryAll();

	/**
	 * 
	 * 格式化标题
	 *
	 * @param text
	 * @param format
	 * @param cleanTitleRegex
	 * @param radarrRuleList
	 * @param radarrTitleList
	 * @return String
	 */
	public String formatTitle(String text, String format, String cleanTitleRegex,
			List<RadarrRule> radarrRuleList, List<RadarrTitle> radarrTitleList);

	/**
	 * 
	 * 格式化
	 *
	 * @param text
	 * @param format
	 * @param tokenRuleMap
	 * @return String
	 */
	public String format(String text, String format, Map<String, List<RadarrRule>> tokenRuleMap);

	/**
	 * 
	 * 根据标题查询
	 *
	 * @param title
	 * @return RadarrTitle
	 */
	public RadarrTitle queryByTitle(String title);

	/**
	 * 
	 * 分页查询
	 *
	 * @param request
	 * @return IPage<RadarrTitle>
	 */
	public IPage<RadarrTitle> query(RadarrTitleQueryRequest request);
}
