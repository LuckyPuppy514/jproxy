package com.lckp.jproxy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lckp.jproxy.component.SyncIntervalComponent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lckp.jproxy.constant.ApiField;
import com.lckp.jproxy.constant.CacheName;
import com.lckp.jproxy.constant.Common;
import com.lckp.jproxy.constant.Monitored;
import com.lckp.jproxy.constant.SystemConfigKey;
import com.lckp.jproxy.constant.TableField;
import com.lckp.jproxy.constant.Token;
import com.lckp.jproxy.entity.SonarrRule;
import com.lckp.jproxy.entity.SonarrTitle;
import com.lckp.jproxy.mapper.SonarrTitleMapper;
import com.lckp.jproxy.model.request.SonarrTitleQueryRequest;
import com.lckp.jproxy.service.ISonarrTitleService;
import com.lckp.jproxy.service.ISystemConfigService;
import com.lckp.jproxy.util.FormatUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * SonarrTitle 服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @since 2023-03-19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SonarrTitleServiceImpl extends ServiceImpl<SonarrTitleMapper, SonarrTitle>
		implements ISonarrTitleService {

	private final ISystemConfigService systemConfigService;

	private final SonarrTitleMapper sonarrTitleMapper;

	private final RedisTemplate<Object, Object> redisTemplate;

	private final RestTemplate restTemplate;

	private final SyncIntervalComponent syncIntervalComponent;

	@Value("${time.sync-interval}")
	private long syncInterval;

	private final ISonarrTitleService proxy() {
		return (ISonarrTitleService) AopContext.currentProxy();
	}

	/**
	 * 
	 * @see com.lckp.jproxy.service.ISonarrTitleService#sync()
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(cacheNames = { CacheName.SONARR_SEARCH_TITLE, CacheName.INDEXER_SEARCH_OFFSET,
			CacheName.SONARR_RESULT_TITLE }, allEntries = true, condition = "#result == true")
	public synchronized boolean sync() {
		if (!syncIntervalComponent.checkInterval(CacheName.SONARR_TITLE_SYNC_INTERVAL)) {
			return false;
		}

		// 请求 Sonarr 接口获取数据
		StringBuilder url = new StringBuilder(
				systemConfigService.queryValueByKey(SystemConfigKey.SONARR_URL));
		url.append("/api/v3/series?" + ApiField.SONARR_APIKEY);
		url.append("=" + systemConfigService.queryValueByKey(SystemConfigKey.SONARR_APIKEY));
		JSONArray jsonArray = JSON
				.parseArray(restTemplate.getForEntity(url.toString(), String.class).getBody());
		String cleanTitleRegex = systemConfigService.queryValueByKey(SystemConfigKey.CLEAN_TITLE_REGEX);
		List<SonarrTitle> sonarrTitleList = new ArrayList<>();
		for (Object object1 : jsonArray) {
			JSONObject jsonObject = (JSONObject) object1;
			// 主标题
			int sno = 0;
			Integer tvdbId = jsonObject.getInteger(ApiField.SONARR_TVDB_ID);
			Integer id = generateSonarrTitleId(tvdbId, sno);
			String mainTitle = jsonObject.getString(ApiField.SONARR_TITLE);
			String title = mainTitle;
			Integer monitored = Monitored.getByFlag(jsonObject.getBooleanValue(ApiField.SONARR_MONITORED))
					.getCode();
			SonarrTitle sonarrTitle = new SonarrTitle();
			sonarrTitle.setId(id);
			sonarrTitle.setTvdbId(tvdbId);
			sonarrTitle.setSno(sno++);
			sonarrTitle.setMainTitle(mainTitle);
			sonarrTitle.setTitle(title);
			sonarrTitle.setCleanTitle(FormatUtil.cleanTitle(title, cleanTitleRegex));
			sonarrTitle.setSeasonNumber(-1);
			sonarrTitle.setMonitored(monitored);
			sonarrTitleList.add(sonarrTitle);
			// 备选标题
			JSONArray alternateTitles = jsonObject.getJSONArray(ApiField.SONARR_ALTERNATE_TITLES);
			for (Object object2 : alternateTitles) {
				JSONObject alternateTitle = (JSONObject) object2;
				id = generateSonarrTitleId(tvdbId, sno);
				title = alternateTitle.getString(ApiField.SONARR_TITLE);
				sonarrTitle = new SonarrTitle();
				sonarrTitle.setId(id);
				sonarrTitle.setTvdbId(tvdbId);
				sonarrTitle.setSno(sno++);
				sonarrTitle.setMainTitle(mainTitle);
				sonarrTitle.setTitle(title);
				sonarrTitle.setCleanTitle(FormatUtil.cleanTitle(title, cleanTitleRegex));
				sonarrTitle.setSeasonNumber(alternateTitle.getInteger(ApiField.SONARR_SCENE_SEASON_NUMBER));
				sonarrTitle.setMonitored(monitored);
				sonarrTitleList.add(sonarrTitle);
			}
		}
		proxy().saveOrUpdateBatch(sonarrTitleList, Common.BATCH_SIZE);
		return true;
	}

	/**
	 * @return
	 * @see com.lckp.jproxy.service.ISonarrTitleService#queryNeedSyncTmdbTitle()
	 */
	@Override
	public List<Integer> queryNeedSyncTmdbTitle() {
		return sonarrTitleMapper.selectNeedSyncTmdbTitle();
	}

	/**
	 * 
	 * 生成 sonarrTitle 主键 id
	 *
	 * @param tvdbId
	 * @param sno
	 * @return Integer
	 */
	private Integer generateSonarrTitleId(Integer tvdbId, int sno) {
		return Integer.parseInt(new StringBuilder(tvdbId.toString()).append(sno).toString());
	}

	/**
	 * @param title
	 * @return
	 * @see com.lckp.jproxy.service.ISonarrTitleService#queryByTitle(java.lang.String)
	 */
	@Override
	public SonarrTitle queryByTitle(String title) {
		String cleanTitleRegex = systemConfigService.queryValueByKey(SystemConfigKey.CLEAN_TITLE_REGEX);
		String cleanTitle = FormatUtil.cleanTitle(title, cleanTitleRegex);
		List<SonarrTitle> sonarrTitleList = proxy().query().eq(TableField.CLEAN_TITLE, cleanTitle).list();
		SonarrTitle sonarrTitle = null;
		if (!sonarrTitleList.isEmpty()) {
			sonarrTitle = sonarrTitleList.get(0);
		} else {
			title = FormatUtil.removeSeason(title);
			cleanTitle = FormatUtil.cleanTitle(title, cleanTitleRegex);
			sonarrTitleList = proxy().query().eq(TableField.CLEAN_TITLE, cleanTitle).list();
			if (!sonarrTitleList.isEmpty()) {
				sonarrTitle = sonarrTitleList.get(0);
			}
		}
		if (sonarrTitle != null) {
			sonarrTitle.setTitle(title);
		}
		return sonarrTitle;
	}

	/**
	 * @return
	 * @see com.lckp.jproxy.service.ISonarrTitleService#queryAll()
	 */
	@Override
	@Cacheable(cacheNames = CacheName.SONARR_RESULT_TITLE)
	public List<SonarrTitle> queryAll() {
		return sonarrTitleMapper.selectSonarrTitleAndTmdbTitle();
	}

	/**
	 * 
	 * @param text
	 * @param format
	 * @param cleanTitleRegex
	 * @param sonarrRuleList
	 * @param sonarrTitleList
	 * @return
	 * @see com.lckp.jproxy.service.ISonarrTitleService#formatTitle(java.lang.String,
	 *      java.lang.String, java.lang.String, java.util.List, java.util.List)
	 */
	@Override
	public String formatTitle(String text, String format, String cleanTitleRegex,
			List<SonarrRule> sonarrRuleList, List<SonarrTitle> sonarrTitleList) {
		for (SonarrRule sonarrRule : sonarrRuleList) {
			if (sonarrRule.getRegex().contains("{" + Token.CLEAN_TITLE + "}")) {
				for (SonarrTitle sonarrTitle : sonarrTitleList) {
					String cleanTitle = sonarrTitle.getCleanTitle() != null ? sonarrTitle.getCleanTitle()
							: FormatUtil.cleanTitle(sonarrTitle.getTitle(), cleanTitleRegex);
					cleanTitle = cleanTitle.replace(FormatUtil.PLACEHOLDER, ".?");
					String cleanText = FormatUtil.cleanTitle(text, cleanTitleRegex);
					String regex = sonarrRule.getRegex().replace("{" + Token.CLEAN_TITLE + "}", cleanTitle);
					if (cleanText.matches(regex)) {
						format = FormatUtil.replaceToken(Token.TITLE, sonarrTitle.getMainTitle(), format);
						Integer seasonNumber = sonarrTitle.getSeasonNumber();
						if (!Integer.valueOf(-1).equals(seasonNumber)
								&& !Integer.valueOf(1).equals(seasonNumber)) {
							format = FormatUtil.replaceToken(Token.SEASON, "S" + seasonNumber, format);
						}
						break;
					}
				}
			} else {
				Matcher matcher = Pattern.compile(sonarrRule.getRegex()).matcher(text);
				if (matcher.find()) {
					try {
						String value = matcher.replaceAll(sonarrRule.getReplacement());
						format = FormatUtil.replaceToken(Token.TITLE, value, format);
						break;
					} catch (Exception e) {
						log.error("replaceAll 出错：{}\n{}", e.getMessage(), JSON.toJSONString(sonarrRule));
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
	 * @see com.lckp.jproxy.service.ISonarrTitleService#format(java.lang.String,
	 *      java.lang.String, java.util.Map)
	 */
	@Override
	public String format(String text, String format, Map<String, List<SonarrRule>> tokenRuleMap) {
		boolean episodeFinded = false;
		Matcher matcher = Pattern.compile(Token.REGEX).matcher(format);
		while (matcher.find()) {
			String token = matcher.group(1);
			List<SonarrRule> sonarrRuleList = tokenRuleMap.get(token);
			for (SonarrRule sonarrRule : sonarrRuleList) {
				Matcher tokenMatcher = Pattern.compile(sonarrRule.getRegex()).matcher(text);
				if (tokenMatcher.find()) {
					try {
						String value = tokenMatcher.replaceAll(sonarrRule.getReplacement());
						format = FormatUtil.replaceToken(token, value, format, sonarrRule.getOffset());
						if (Token.EPISODE.equals(token)) {
							episodeFinded = true;
						}
						break;
					} catch (Exception e) {
						log.error("replaceAll 出错：{}\n{}", e.getMessage(), JSON.toJSONString(sonarrRule));
					}
				}
			}
		}
		if (!episodeFinded && format.contains("{" + Token.EPISODE + "}")) {
			return text;
		}
		matcher = Pattern.compile(Token.REGEX).matcher(format);
		while (matcher.find()) {
			format = FormatUtil.removeToken(matcher.group(1), format);
		}
		return format.trim();
	}

	/**
	 * @param text
	 * @param format
	 * @param tokenRuleMap
	 * @return
	 * @see com.lckp.jproxy.service.ISonarrTitleService#formatWithCache(java.lang.String,
	 *      java.lang.String, java.util.Map)
	 */
	@Override
	@Cacheable(cacheNames = CacheName.DOWNLOADER_FORMAT_NAME, key = "#text")
	public String formatWithCache(String text, String format, Map<String, List<SonarrRule>> tokenRuleMap) {
		return format(text, format, tokenRuleMap);
	}

	/**
	 * @param request
	 * @return
	 * @see com.lckp.jproxy.service.ISonarrTitleService#query(com.lckp.jproxy.model.request.SonarrTitleQueryRequest)
	 */
	@Override
	public IPage<SonarrTitle> query(SonarrTitleQueryRequest request) {
		QueryWrapper<SonarrTitle> wrapper = new QueryWrapper<>();
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
