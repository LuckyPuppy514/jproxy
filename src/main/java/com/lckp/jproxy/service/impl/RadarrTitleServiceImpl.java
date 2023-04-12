package com.lckp.jproxy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import com.lckp.jproxy.constant.Monitored;
import com.lckp.jproxy.constant.SystemConfigKey;
import com.lckp.jproxy.constant.TableField;
import com.lckp.jproxy.constant.Token;
import com.lckp.jproxy.entity.RadarrRule;
import com.lckp.jproxy.entity.RadarrTitle;
import com.lckp.jproxy.mapper.RadarrTitleMapper;
import com.lckp.jproxy.model.request.RadarrTitleQueryRequest;
import com.lckp.jproxy.service.IRadarrTitleService;
import com.lckp.jproxy.service.ISystemConfigService;
import com.lckp.jproxy.util.FormatUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * RadarrTitle 服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @since 2023-03-20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RadarrTitleServiceImpl extends ServiceImpl<RadarrTitleMapper, RadarrTitle>
		implements IRadarrTitleService {

	private final ISystemConfigService systemConfigService;

	@Autowired
	@Qualifier("syncIntervalCache")
	private Cache<String, Integer> syncIntervalCache;

	private final RestTemplate restTemplate;

	private final IRadarrTitleService proxy() {
		return (IRadarrTitleService) AopContext.currentProxy();
	}

	/**
	 * 
	 * @see com.lckp.jproxy.service.IRadarrTitleService#sync()
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(cacheNames = { CacheName.RADARR_SEARCH_TITLE, CacheName.INDEXER_SEARCH_OFFSET,
			CacheName.RADARR_RESULT_TITLE }, allEntries = true, condition = "#result == true")
	public synchronized boolean sync() {
		if (syncIntervalCache.asMap().compute(CacheName.RADARR_TITLE_SYNC_INTERVAL, (key, value) -> {
			value = value == null ? 1 : 2;
			return value;
		}) > 1) {
			return false;
		}

		// 请求 Radarr 接口获取数据
		StringBuilder url = new StringBuilder(
				systemConfigService.queryValueByKey(SystemConfigKey.RADARR_URL));
		url.append("/api/v3/movie?" + ApiField.RADARR_APIKEY);
		url.append("=" + systemConfigService.queryValueByKey(SystemConfigKey.RADARR_APIKEY));
		JSONArray jsonArray = JSON
				.parseArray(restTemplate.getForEntity(url.toString(), String.class).getBody());
		String cleanTitleRegex = systemConfigService.queryValueByKey(SystemConfigKey.CLEAN_TITLE_REGEX);
		List<RadarrTitle> radarrTitleList = new ArrayList<>();
		for (Object object1 : jsonArray) {
			JSONObject jsonObject = (JSONObject) object1;
			// 主标题
			int sno = 0;
			Integer tmdbId = jsonObject.getInteger(ApiField.RADARR_TMDB_ID);
			Integer id = generateRadarrTitleId(tmdbId, sno);
			Integer movieId = jsonObject.getInteger(ApiField.RADARR_ID);
			Integer year = jsonObject.getInteger(ApiField.RADARR_YEAR);
			String mainTitle = jsonObject.getString(ApiField.RADARR_TITLE);
			String title = mainTitle;
			Integer monitored = Monitored.getByFlag(jsonObject.getBooleanValue(ApiField.RADARR_MONITORED))
					.getCode();
			RadarrTitle mainRadarrTitle = new RadarrTitle();
			mainRadarrTitle.setId(id);
			mainRadarrTitle.setMovieId(movieId);
			mainRadarrTitle.setTmdbId(tmdbId);
			mainRadarrTitle.setSno(sno++);
			mainRadarrTitle.setMainTitle(mainTitle);
			mainRadarrTitle.setTitle(title);
			mainRadarrTitle.setCleanTitle(FormatUtil.cleanTitle(title, cleanTitleRegex));
			mainRadarrTitle.setYear(year);
			mainRadarrTitle.setMonitored(monitored);
			radarrTitleList.add(mainRadarrTitle);
			// 英文标题
			RadarrTitle radarrTitle = new RadarrTitle();
			BeanUtils.copyProperties(mainRadarrTitle, radarrTitle, RadarrTitle.class);
			id = generateRadarrTitleId(tmdbId, sno);
			title = jsonObject.getString(ApiField.RADARR_PATH);
			int index = title.lastIndexOf("/");
			index = index == -1 ? 0 : index + 1;
			title = title.substring(index);
			title = title.replaceAll(" \\(\\d{4}\\)$", "");
			radarrTitle.setId(id);
			radarrTitle.setMovieId(movieId);
			radarrTitle.setSno(sno++);
			radarrTitle.setTitle(title);
			radarrTitle.setCleanTitle(FormatUtil.cleanTitle(title, cleanTitleRegex));
			radarrTitleList.add(radarrTitle);
			// 英文标题
			radarrTitle = new RadarrTitle();
			BeanUtils.copyProperties(mainRadarrTitle, radarrTitle, RadarrTitle.class);
			id = generateRadarrTitleId(tmdbId, sno);
			radarrTitle.setId(id);
			radarrTitle.setMovieId(movieId);
			radarrTitle.setSno(sno++);
			radarrTitle.setTitle(title);
			radarrTitle.setCleanTitle(jsonObject.getString(ApiField.RADARR_CLEAN_TITLE));
			radarrTitleList.add(radarrTitle);
			// 原始标题
			radarrTitle = new RadarrTitle();
			BeanUtils.copyProperties(mainRadarrTitle, radarrTitle, RadarrTitle.class);
			id = generateRadarrTitleId(tmdbId, sno);
			title = jsonObject.getString(ApiField.RADARR_ORIGINAL_TITLE);
			radarrTitle.setId(id);
			radarrTitle.setMovieId(movieId);
			radarrTitle.setSno(sno++);
			radarrTitle.setTitle(title);
			radarrTitle.setCleanTitle(FormatUtil.cleanTitle(title, cleanTitleRegex));
			radarrTitleList.add(radarrTitle);

			// 备选标题
			JSONArray alternateTitles = jsonObject.getJSONArray(ApiField.RADARR_ALTERNATE_TITLES);
			for (Object object2 : alternateTitles) {
				JSONObject alternateTitle = (JSONObject) object2;
				id = generateRadarrTitleId(tmdbId, sno);
				title = alternateTitle.getString(ApiField.RADARR_TITLE);
				radarrTitle = new RadarrTitle();
				radarrTitle.setId(id);
				radarrTitle.setMovieId(movieId);
				radarrTitle.setTmdbId(tmdbId);
				radarrTitle.setSno(sno++);
				radarrTitle.setMainTitle(mainTitle);
				radarrTitle.setTitle(title);
				radarrTitle.setCleanTitle(FormatUtil.cleanTitle(title, cleanTitleRegex));
				radarrTitle.setYear(year);
				radarrTitle.setMonitored(monitored);
				radarrTitleList.add(radarrTitle);
			}
		}
		proxy().saveOrUpdateBatch(radarrTitleList, Common.BATCH_SIZE);
		log.info("同步电影标题成功：{}", radarrTitleList.size());
		return true;
	}

	/**
	 * 
	 * 生成 radarrTitleId 主键 id
	 *
	 * @param tvdbId
	 * @param sno
	 * @return Integer
	 */
	private Integer generateRadarrTitleId(Integer tmdbId, int sno) {
		return Integer.parseInt(new StringBuilder(tmdbId.toString()).append(sno).toString());
	}

	/**
	 * @return
	 * @see com.lckp.jproxy.service.IRadarrTitleService#queryAll()
	 */
	@Override
	@Cacheable(cacheNames = CacheName.RADARR_RESULT_TITLE)
	public List<RadarrTitle> queryAll() {
		return proxy().query().groupBy(TableField.CLEAN_TITLE)
				.last("ORDER BY monitored DESC, LENGTH (title) DESC").list();
	}

	/**
	 * @param text
	 * @param format
	 * @param cleanTitleRegex
	 * @param radarrRuleList
	 * @param radarrTitleList
	 * @return
	 * @see com.lckp.jproxy.service.IRadarrTitleService#formatTitle(java.lang.String,
	 *      java.lang.String, java.lang.String, java.util.List, java.util.List)
	 */
	@Override
	public String formatTitle(String text, String format, String cleanTitleRegex,
			Map<String, List<RadarrRule>> tokenRuleMap, List<RadarrTitle> radarrTitleList) {
		List<RadarrRule> titleRuleList = tokenRuleMap.get(Token.TITLE);
		List<RadarrRule> yearRuleList = tokenRuleMap.get(Token.YEAR);
		for (RadarrRule titleRule : titleRuleList) {
			if (titleRule.getRegex().contains("{" + Token.CLEAN_TITLE + "}")) {
				for (RadarrTitle radarrTitle : radarrTitleList) {
					String cleanTitle = radarrTitle.getCleanTitle() != null ? radarrTitle.getCleanTitle()
							: FormatUtil.cleanTitle(radarrTitle.getTitle(), cleanTitleRegex);
					cleanTitle = cleanTitle.replace(FormatUtil.PLACEHOLDER, ".?");
					String cleanText = FormatUtil.cleanTitle(text, cleanTitleRegex);
					String regex = titleRule.getRegex().replace("{" + Token.CLEAN_TITLE + "}", cleanTitle);
					if (cleanText.matches(regex)) {
						String titleYear = String.valueOf(radarrTitle.getYear());
						boolean yearMatch = true;
						for (RadarrRule yearRule : yearRuleList) {
							Matcher tokenMatcher = Pattern.compile(yearRule.getRegex()).matcher(text);
							if (tokenMatcher.find()) {
								String textYear = tokenMatcher.replaceAll(yearRule.getReplacement());
								if (!titleYear.equals(textYear)) {
									log.debug("年份不匹配：{} - {}", titleYear, textYear);
									yearMatch = false;
								}
								break;
							}
						}
						if (!yearMatch) {
							continue;
						}
						format = FormatUtil.replaceToken(Token.TITLE, radarrTitle.getMainTitle(), format);
						format = FormatUtil.replaceToken(Token.YEAR, titleYear, format);
						break;
					}
				}
			} else {
				Matcher matcher = Pattern.compile(titleRule.getRegex()).matcher(text);
				if (matcher.find()) {
					try {
						String value = matcher.replaceAll(titleRule.getReplacement());
						format = FormatUtil.replaceToken(Token.TITLE, value, format);
						break;
					} catch (Exception e) {
						log.error("replaceAll 出错：{}\n{}", e.getMessage(), JSON.toJSONString(titleRule));
					}
				}
			}
		}
		return format.trim();
	}

	/**
	 * @param text
	 * @param format
	 * @param tokenRuleMap
	 * @return
	 * @see com.lckp.jproxy.service.IRadarrTitleService#format(java.lang.String,
	 *      java.lang.String, java.util.Map)
	 */
	@Override
	public String format(String text, String format, Map<String, List<RadarrRule>> tokenRuleMap) {
		Matcher matcher = Pattern.compile(Token.REGEX).matcher(format);
		while (matcher.find()) {
			String token = matcher.group(1);
			List<RadarrRule> radarrRuleList = tokenRuleMap.get(token);
			for (RadarrRule radarrRule : radarrRuleList) {
				Matcher tokenMatcher = Pattern.compile(radarrRule.getRegex()).matcher(text);
				if (tokenMatcher.find()) {
					try {
						String value = tokenMatcher.replaceAll(radarrRule.getReplacement());
						format = FormatUtil.replaceToken(token, value, format);
						break;
					} catch (Exception e) {
						log.error("replaceAll 出错：{}\n{}", e.getMessage(), JSON.toJSONString(radarrRule));
					}
				}
			}
		}
		format = FormatUtil.removeAllToken(format);
		return format.trim();
	}

	/**
	 * @param title
	 * @return
	 * @see com.lckp.jproxy.service.IRadarrTitleService#queryByTitle(java.lang.String)
	 */
	@Override
	public RadarrTitle queryByTitle(String title) {
		String cleanTitleRegex = systemConfigService.queryValueByKey(SystemConfigKey.CLEAN_TITLE_REGEX);
		String cleanTitle = FormatUtil.cleanTitle(title, cleanTitleRegex);
		List<RadarrTitle> radarrTitleList = proxy().query().eq(TableField.CLEAN_TITLE, cleanTitle).list();
		RadarrTitle radarrTitle = null;
		if (!radarrTitleList.isEmpty()) {
			radarrTitle = radarrTitleList.get(0);
			radarrTitle.setTitle(title);
		}
		return radarrTitle;
	}

	/**
	 * @param request
	 * @return
	 * @see com.lckp.jproxy.service.IRadarrTitleService#query(com.lckp.jproxy.model.request.RadarrTitleQueryRequest)
	 */
	@Override
	public IPage<RadarrTitle> query(RadarrTitleQueryRequest request) {
		QueryWrapper<RadarrTitle> wrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(request.getTitle())) {
			wrapper.like(TableField.TITLE, request.getTitle().trim());
		}
		if (request.getTmdbId() != null) {
			wrapper.eq(TableField.TMDB_ID, request.getTmdbId());
		}
		wrapper.orderByDesc(TableField.UPDATE_TIME);
		return proxy().page(request.mybatisPlusPage(), wrapper);
	}
}
