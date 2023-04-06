package com.lckp.jproxy.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipException;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.lckp.jproxy.constant.ApiField;
import com.lckp.jproxy.constant.SystemConfigKey;
import com.lckp.jproxy.constant.Token;
import com.lckp.jproxy.entity.SonarrRule;
import com.lckp.jproxy.service.ISonarrQbittorrentService;
import com.lckp.jproxy.service.ISonarrRuleService;
import com.lckp.jproxy.service.ISonarrTitleService;
import com.lckp.jproxy.service.ISystemConfigService;
import com.lckp.jproxy.util.GzipUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Sonarr qBittorrent 服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-20
 */
@Slf4j
@Service
public class SonarrQbittorrentServiceImpl extends SonarrDownloaderServiceImpl
		implements ISonarrQbittorrentService {

	/**
	 * @param systemConfigService
	 */
	public SonarrQbittorrentServiceImpl(ISystemConfigService systemConfigService,
			ISonarrTitleService sonarrTitleService, ISonarrRuleService sonarrRuleService) {
		super(systemConfigService, sonarrTitleService, sonarrRuleService);
	}

	/**
	 * 
	 * @param content
	 * @return
	 * @see com.lckp.jproxy.service.impl.DownloaderServiceImpl#executeFormatRule(byte[])
	 */
	@Override
	public byte[] executeFormatRule(byte[] content) {
		if (content.length == 0) {
			return content;
		}
		try {
			String unzipContent = GzipUtil.decompress(content);
			String format = systemConfigService.queryValueByKey(SystemConfigKey.SONARR_DOWNLOADER_FORMAT);
			Map<String, List<SonarrRule>> tokenRuleMap = new ConcurrentHashMap<>();
			Matcher matcher = Pattern.compile(Token.REGEX).matcher(format);
			while (matcher.find()) {
				String token = matcher.group(1);
				tokenRuleMap.put(token, sonarrRuleService.query(token));
			}
			JSONArray array = JSON.parseArray(unzipContent);
			for (Object object : array) {
				JSONObject json = (JSONObject) object;
				if (json.getIntValue(ApiField.QBITTORRENT_PROGRESS) == 1) {
					String name = json.getString(ApiField.QBITTORRENT_NAME);
					String newName = sonarrTitleService.formatWithCache(name, format, tokenRuleMap);
					json.put(ApiField.QBITTORRENT_NAME, newName);
					log.debug("下载器格式化：{} ==> {}", name, newName);
				}
			}
			String finalContent = JSON.toJSONString(array);
			content = GzipUtil.compress(finalContent);
		} catch (ZipException ze) {
			log.debug("ZipException: {}, {}", new String(content, StandardCharsets.UTF_8), ze.getMessage());
		} catch (Exception e) {
			log.error("执行结果规则出错：{}", e);
		}
		return content;
	}
}
