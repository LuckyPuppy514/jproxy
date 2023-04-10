package com.lckp.jproxy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.lckp.jproxy.constant.ApiField;
import com.lckp.jproxy.constant.CacheName;
import com.lckp.jproxy.constant.Common;
import com.lckp.jproxy.constant.SystemConfigKey;
import com.lckp.jproxy.constant.TableField;
import com.lckp.jproxy.entity.TmdbTitle;
import com.lckp.jproxy.mapper.TmdbTitleMapper;
import com.lckp.jproxy.model.request.TmdbTitleQueryRequest;
import com.lckp.jproxy.service.ISystemConfigService;
import com.lckp.jproxy.service.ITmdbTitleService;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 * TmdbTitle 服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @since 2023-03-19
 */
@Service
@RequiredArgsConstructor
public class TmdbTitleServiceImpl extends ServiceImpl<TmdbTitleMapper, TmdbTitle>
		implements ITmdbTitleService {

	private final ISystemConfigService systemConfigService;

	private final RestTemplate restTemplate;

	@Autowired
	@Qualifier("syncIntervalCache")
	private Cache<String, Integer> syncIntervalCache;

	private final ITmdbTitleService proxy() {
		return (ITmdbTitleService) AopContext.currentProxy();
	}

	/**
	 * @param tvdbId
	 * @param language
	 * @return
	 * @see com.lckp.jproxy.service.ITmdbTitleService#find(java.lang.Integer,
	 *      java.lang.String)
	 */
	@Override
	public TmdbTitle find(Integer tvdbId, String language) {
		StringBuilder url = new StringBuilder();
		url.append(systemConfigService.queryValueByKey(SystemConfigKey.TMDB_URL));
		url.append("/3/find/" + tvdbId);
		url.append("?" + ApiField.TMDB_API_KEY);
		url.append("=" + systemConfigService.queryValueByKey(SystemConfigKey.TMDB_APIKEY));
		url.append("&" + ApiField.TMDB_LANGUAGE + "=" + language);
		url.append("&" + ApiField.TMDB_EXTERNAL_SOURCE + "=" + ApiField.TMDB_TVDB_ID);
		JSONArray jsonArray = JSON
				.parseObject(restTemplate.getForEntity(url.toString(), String.class).getBody())
				.getJSONArray(ApiField.TMDB_TV_RESULTS);
		if (!jsonArray.isEmpty()) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(0);
			String title = jsonObject.getString(ApiField.TMDB_NAME);
			TmdbTitle tmdbTitle = new TmdbTitle();
			tmdbTitle.setTvdbId(tvdbId);
			tmdbTitle.setLanguage(language);
			tmdbTitle.setTmdbId(jsonObject.getInteger(ApiField.TMDB_ID));
			tmdbTitle.setTitle(title);
			return tmdbTitle;
		}
		return null;
	}

	/**
	 * 
	 * @param tvdbIdList
	 * @see com.lckp.jproxy.service.ITmdbTitleService#sync(java.util.List)
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(cacheNames = { CacheName.SONARR_SEARCH_TITLE, CacheName.INDEXER_SEARCH_OFFSET,
			CacheName.SONARR_RESULT_TITLE }, allEntries = true, condition = "#result == true")
	public boolean sync(List<Integer> tvdbIdList) {
		if (tvdbIdList == null || tvdbIdList.isEmpty()) {
			return true;
		}
		if (syncIntervalCache.asMap().compute(CacheName.TMDB_TITLE_SYNC_INTERVAL, (key, value) -> {
			value = value == null ? 1 : 2;
			return value;
		}) > 1) {
			return false;
		}

		String[] languages = new String[2];
		languages[0] = systemConfigService.queryValueByKey(SystemConfigKey.SONARR_LANGUAGE_1);
		languages[1] = systemConfigService.queryValueByKey(SystemConfigKey.SONARR_LANGUAGE_2);
		List<TmdbTitle> tmdbTitleList = new ArrayList<>();
		for (Integer tvdbId : tvdbIdList) {
			for (String language : languages) {
				TmdbTitle tmdbTitle = find(tvdbId, language);
				if (tmdbTitle != null) {
					tmdbTitleList.add(tmdbTitle);
				}
			}
		}
		proxy().saveOrUpdateBatch(tmdbTitleList, Common.BATCH_SIZE);
		return true;
	}

	/**
	 * @param request
	 * @return
	 * @see com.lckp.jproxy.service.ITmdbTitleService#query(com.lckp.jproxy.model.request.TmdbTitleQueryRequest)
	 */
	@Override
	public IPage<TmdbTitle> query(TmdbTitleQueryRequest request) {
		QueryWrapper<TmdbTitle> wrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(request.getTitle())) {
			wrapper.like(TableField.TITLE, request.getTitle().trim());
		}
		if (request.getTvdbId() != null) {
			wrapper.eq(TableField.TVDB_ID, request.getTvdbId());
		}
		wrapper.orderByDesc(TableField.UPDATE_TIME);
		return proxy().page(request.mybatisPlusPage(), wrapper);
	}
}
