package com.lckp.jproxy.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lckp.jproxy.entity.SonarrTitle;

/**
 * <p>
 * SonarrTitle Mapper 接口
 * </p>
 *
 * @author LuckyPuppy514
 * @since 2023-03-19
 */
public interface SonarrTitleMapper extends BaseMapper<SonarrTitle> {
	/**
	 * 
	 * 查询需要同步 TMBD 标题的 tvdbId
	 *
	 * @return List<Integer>
	 */
	public List<Integer> selectNeedSyncTmdbTitle();
	
	/**
	 * 
	 * 查询 Sonarr 标题和 TMDB 标题
	 *
	 * @return List<SonarrTitle>
	 */
	public List<SonarrTitle> selectSonarrTitleAndTmdbTitle();
}
