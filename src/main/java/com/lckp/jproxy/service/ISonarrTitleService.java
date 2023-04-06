package com.lckp.jproxy.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lckp.jproxy.entity.SonarrRule;
import com.lckp.jproxy.entity.SonarrTitle;
import com.lckp.jproxy.model.request.SonarrTitleQueryRequest;

/**
 * <p>
 * SonarrTitle 服务类
 * </p>
 *
 * @author LuckyPuppy514
 * @since 2023-03-19
 */
public interface ISonarrTitleService extends IService<SonarrTitle> {
	/**
	 * 
	 * 同步
	 *
	 * boolean
	 */
	public boolean sync();

	/**
	 * 
	 * 查询需要同步 TMDB 标题的 tvdbId
	 *
	 * @return List<Integer>
	 */
	public List<Integer> queryNeedSyncTmdbTitle();

	/**
	 * 
	 * 根据标题查询
	 *
	 * @param title
	 * @return SonarrTitle
	 */
	public SonarrTitle queryByTitle(String title);

	/**
	 * 
	 * 查询所有标题
	 *
	 * @return List<SonarrTitle>
	 */
	public List<SonarrTitle> queryAll();

	/**
	 * 
	 * 格式化标题
	 *
	 * @param text
	 * @param format
	 * @param cleanTitleRegex
	 * @param sonarrRuleList
	 * @param sonarrTitleList
	 * @return String
	 */
	public String formatTitle(String text, String format, String cleanTitleRegex,
			List<SonarrRule> sonarrRuleList, List<SonarrTitle> sonarrTitleList);

	/**
	 * 
	 * 格式化
	 *
	 * @param text
	 * @param format
	 * @param tokenRuleMap
	 * @return String
	 */
	public String format(String text, String format, Map<String, List<SonarrRule>> tokenRuleMap);

	/**
	 * 
	 * 格式化并缓存
	 *
	 * @param text
	 * @param format
	 * @param tokenRuleMap
	 * @return String
	 */
	public String formatWithCache(String text, String format, Map<String, List<SonarrRule>> tokenRuleMap);

	/**
	 * 
	 * 分页查询
	 *
	 * @param request
	 * @return IPage<SonarrTitle>
	 */
	public IPage<SonarrTitle> query(SonarrTitleQueryRequest request);
}
