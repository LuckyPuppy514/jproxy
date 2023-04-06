package com.lckp.jproxy.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lckp.jproxy.entity.TmdbTitle;
import com.lckp.jproxy.model.request.TmdbTitleQueryRequest;

/**
 * <p>
 * TmdbTitle 服务类
 * </p>
 *
 * @author LuckyPuppy514
 * @since 2023-03-19
 */
public interface ITmdbTitleService extends IService<TmdbTitle> {
	/**
	 * 
	 * 同步
	 *
	 * @param tvdbIdList boolean
	 */
	public boolean sync(List<Integer> tvdbIdList);

	/**
	 * 
	 * 根据 TVDB 编号和语言代码查询 TMDB 标题
	 *
	 * @param tvdbId
	 * @param language
	 * @return TmdbTitle
	 */
	public TmdbTitle find(Integer tvdbId, String language);

	/**
	 * 
	 * 分页查询
	 *
	 * @param request
	 * @return IPage<TmdbTitle>
	 */
	public IPage<TmdbTitle> query(TmdbTitleQueryRequest request);
}
