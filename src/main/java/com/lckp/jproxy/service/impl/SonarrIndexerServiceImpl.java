package com.lckp.jproxy.service.impl;

import com.lckp.jproxy.constant.*;
import com.lckp.jproxy.entity.SonarrRule;
import com.lckp.jproxy.entity.SonarrTitle;
import com.lckp.jproxy.entity.TmdbTitle;
import com.lckp.jproxy.service.*;
import com.lckp.jproxy.util.FormatUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Sonarr 索引器服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SonarrIndexerServiceImpl extends IndexerServiceImpl implements ISonarrIndexerService {

	protected final ISonarrTitleService sonarrTitleService;

	protected final ISonarrRuleService sonarrRuleService;

	protected final ITmdbTitleService tmdbTitleService;

	protected final ISystemConfigService systemConfigService;

	protected final ISystemCacheService systemCacheService;

	@Value("${min-count}")
	private int minCount;

	/**
	 * 
	 * @param title
	 * @return
	 * @see com.lckp.jproxy.service.impl.IndexerServiceImpl#getSearchTitle(java.lang.String)
	 */
	@Override
	@Cacheable(cacheNames = CacheName.SONARR_SEARCH_TITLE, key = "#title")
	public List<String> getSearchTitle(String title) {
		List<String> searchTitleList = new ArrayList<>();
		SonarrTitle sonarrTitle = sonarrTitleService.queryByTitle(title);
		if (sonarrTitle == null) {
			sonarrTitle = syncAndGetSonarrTitle(title);
		}
		searchTitleList.add(sonarrTitle.getTitle());
		// 主标题追加 TMDB 标题
		if (Integer.valueOf(0).equals(sonarrTitle.getSno())
				|| Integer.valueOf(1).equals(sonarrTitle.getSno())) {
			List<TmdbTitle> tmdbTitleList = tmdbTitleService.query()
					.eq(TableField.TVDB_ID, sonarrTitle.getTvdbId()).groupBy(TableField.TITLE)
					.orderByAsc(TableField.ID).list();
			if (!tmdbTitleList.isEmpty()) {
				for (TmdbTitle tmdbTitle : tmdbTitleList) {
					searchTitleList.add(tmdbTitle.getTitle());
				}
				searchTitleList.add(tmdbTitleList.get(0).getTitle());
			}
		}
		return searchTitleList;
	}

	/**
	 * @param xml
	 * @return
	 * @see com.lckp.jproxy.service.ISonarrIndexerService#executeFormatRule(java.lang.String)
	 */
	@Override
	public String executeFormatRule(String xml) {
		if (StringUtils.isBlank(xml) || !xml.contains("<" + ApiField.INDEXER_ITEM + ">")) {
			return xml;
		}
		try {
			String format = systemConfigService.queryValueByKey(SystemConfigKey.SONARR_INDEXER_FORMAT);
			Map<String, List<SonarrRule>> tokenRuleMap = new ConcurrentHashMap<>();
			Matcher matcher = Pattern.compile(Token.REGEX).matcher(format);
			while (matcher.find()) {
				String token = matcher.group(1);
				tokenRuleMap.put(token, sonarrRuleService.query(token));
			}
			if (!tokenRuleMap.containsKey(Token.TITLE)) {
				return xml;
			}
			String cleanTitleRegex = systemConfigService.queryValueByKey(SystemConfigKey.CLEAN_TITLE_REGEX);
			List<SonarrTitle> sonarrTitleList = sonarrTitleService.queryAll();
			Document document = DocumentHelper.parseText(xml);
			Element root = document.getRootElement();
			Element channel = root.element(ApiField.INDEXER_CHANNEL);
			for (Iterator<Element> items = channel.elementIterator(ApiField.INDEXER_ITEM); items.hasNext();) {
				Element item = items.next();
				Element titleElement = item.element(ApiField.INDEXER_TITLE);
				String text = titleElement.getText();
				// 去除换行
				text = text.replaceAll("\\r\\n|\\r|\\n", " ");
				String newText = sonarrTitleService.formatTitle(text, format, cleanTitleRegex,
						tokenRuleMap.get(Token.TITLE), sonarrTitleList);
				if (newText.contains("{" + Token.TITLE + "}")) {
					log.debug("索引器格式化失败：{} ==> 未匹配到标题", text);
					continue;
				}
				Element descriptionElement = item.element(ApiField.INDEXER_DESCRIPTION);
				if (descriptionElement != null) {
					String description = descriptionElement.getText();
					if (StringUtils.isNotBlank(description)) {
						text = text + " " + FormatUtil.PLACEHOLDER_SEPARATOR + " " + description;
					}
				}
				newText = sonarrTitleService.format(text, newText, tokenRuleMap);
				titleElement.setText(newText);
				log.debug("索引器格式化：{} ==> {}", text, newText);
			}
			return document.asXML();
		} catch (DocumentException e) {
			log.error("xml 解析出错：{}", e);
		}
		return xml;
	}

	/**
	 * 
	 * 同步后再次获取 Sonarr 标题
	 *
	 * @param title
	 * @return SonarrTitle
	 */
	private synchronized SonarrTitle syncAndGetSonarrTitle(String title) {
		SonarrTitle sonarrTitle = sonarrTitleService.queryByTitle(title);
		if (sonarrTitle == null) {
			sonarrTitleService.sync();
			systemCacheService.clear(CacheName.TMDB_TITLE_SYNC_INTERVAL);
			try {
				tmdbTitleService.sync(sonarrTitleService.queryNeedSyncTmdbTitle());
			} catch (Exception e) {
				log.debug("同步 TMDB 标题出错：{}", e.getMessage());
			}
			sonarrTitle = sonarrTitleService.queryByTitle(title);
			if (sonarrTitle == null) {
				log.debug("找不到匹配的标题：{}", title);
				sonarrTitle = new SonarrTitle();
				sonarrTitle.setTitle(title);
			}
		}
		return sonarrTitle;
	}

	/**
	 * 
	 * @return
	 * @see com.lckp.jproxy.service.impl.IndexerServiceImpl#getMinCount()
	 */
	@Override
	public int getMinCount() {
		return minCount;
	}
}
