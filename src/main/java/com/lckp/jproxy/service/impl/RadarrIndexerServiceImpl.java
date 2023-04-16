package com.lckp.jproxy.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.lckp.jproxy.constant.ApiField;
import com.lckp.jproxy.constant.CacheName;
import com.lckp.jproxy.constant.SystemConfigKey;
import com.lckp.jproxy.constant.TableField;
import com.lckp.jproxy.constant.Token;
import com.lckp.jproxy.entity.RadarrRule;
import com.lckp.jproxy.entity.RadarrTitle;
import com.lckp.jproxy.service.IRadarrIndexerService;
import com.lckp.jproxy.service.IRadarrRuleService;
import com.lckp.jproxy.service.IRadarrTitleService;
import com.lckp.jproxy.service.ISystemConfigService;
import com.lckp.jproxy.util.FormatUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Radarr 索引器服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RadarrIndexerServiceImpl extends IndexerServiceImpl implements IRadarrIndexerService {

	protected final IRadarrTitleService radarrTitleService;

	protected final IRadarrRuleService radarrRuleService;

	protected final ISystemConfigService systemConfigService;

	/**
	 * 
	 * @param searchKey
	 * @return
	 * @see com.lckp.jproxy.service.impl.IndexerServiceImpl#getTitle(java.lang.String)
	 */
	@Override
	public String getTitle(String searchKey) {
		return searchKey;
	}

	/**
	 * 
	 * @param title
	 * @return
	 * @see com.lckp.jproxy.service.impl.IndexerServiceImpl#getSearchTitle(java.lang.String)
	 */
	@Override
	@Cacheable(cacheNames = CacheName.RADARR_SEARCH_TITLE, key = "#title")
	public List<String> getSearchTitle(String title) {
		List<String> searchTitleList = new ArrayList<>();
		searchTitleList.add(title);
		// 追加去除年份标题
		String titleWithoutYear = FormatUtil.removeYear(title);
		if (titleWithoutYear.length() != title.length()) {
			searchTitleList.add(titleWithoutYear);
			title = titleWithoutYear;
		}
		RadarrTitle radarrTitle = radarrTitleService.queryByTitle(title);
		if (radarrTitle == null) {
			radarrTitle = syncAndGetRadarrTitle(title);
		}
		// 英文标题追加主标题
		if (Integer.valueOf(1).equals(radarrTitle.getSno())
				|| Integer.valueOf(2).equals(radarrTitle.getSno())) {
			List<RadarrTitle> radarrTitleList = radarrTitleService.query()
					.eq(TableField.TMDB_ID, radarrTitle.getTmdbId()).eq(TableField.SNO, 0).list();
			if (!radarrTitleList.isEmpty()) {
				searchTitleList.add(radarrTitleList.get(0).getCleanTitle());
			}
		}
		return searchTitleList;
	}

	/**
	 * 
	 * @param xml
	 * @return
	 * @see com.lckp.jproxy.service.impl.IndexerServiceImpl#executeFormatRule(java.lang.String)
	 */
	@Override
	public String executeFormatRule(String xml) {
		if (StringUtils.isBlank(xml) || !xml.contains("<" + ApiField.INDEXER_ITEM + ">")) {
			return xml;
		}
		try {
			String format = systemConfigService.queryValueByKey(SystemConfigKey.RADARR_INDEXER_FORMAT);
			Map<String, List<RadarrRule>> tokenRuleMap = new ConcurrentHashMap<>();
			Matcher matcher = Pattern.compile(Token.REGEX).matcher(format);
			while (matcher.find()) {
				String token = matcher.group(1);
				tokenRuleMap.put(token, radarrRuleService.query(token));
			}
			if (!tokenRuleMap.containsKey(Token.TITLE)) {
				return xml;
			}
			String cleanTitleRegex = systemConfigService.queryValueByKey(SystemConfigKey.CLEAN_TITLE_REGEX);
			List<RadarrTitle> radarrTitleList = radarrTitleService.queryAll();
			Document document = DocumentHelper.parseText(xml);
			Element root = document.getRootElement();
			Element channel = root.element(ApiField.INDEXER_CHANNEL);
			for (Iterator<Element> items = channel.elementIterator(ApiField.INDEXER_ITEM); items.hasNext();) {
				Element item = items.next();
				Element titleElement = item.element(ApiField.INDEXER_TITLE);
				String text = titleElement.getText();
				String newText = radarrTitleService.formatTitle(text, format, cleanTitleRegex, tokenRuleMap,
						radarrTitleList);
				if (newText.contains("{" + Token.TITLE + "}")) {
					log.debug("索引器格式化失败：{} ==> 未匹配到标题", text);
					continue;
				}
				newText = radarrTitleService.format(text, newText, tokenRuleMap);
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
	 * 同步后再次获取 Radarr 标题
	 *
	 * @param title
	 * @return RadarrTitle
	 */
	private synchronized RadarrTitle syncAndGetRadarrTitle(String title) {
		RadarrTitle radarrTitle = radarrTitleService.queryByTitle(title);
		if (radarrTitle == null) {
			radarrTitleService.sync();
			radarrTitle = radarrTitleService.queryByTitle(title);
			if (radarrTitle == null) {
				log.debug("找不到匹配的标题：{}", title);
				radarrTitle = new RadarrTitle();
				radarrTitle.setTitle(title);
			}
		}
		return radarrTitle;
	}
}
