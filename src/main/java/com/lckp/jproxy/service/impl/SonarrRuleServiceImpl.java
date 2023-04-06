package com.lckp.jproxy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lckp.jproxy.constant.CacheName;
import com.lckp.jproxy.constant.Common;
import com.lckp.jproxy.constant.SystemConfigKey;
import com.lckp.jproxy.constant.TableField;
import com.lckp.jproxy.constant.ValidStatus;
import com.lckp.jproxy.entity.SonarrRule;
import com.lckp.jproxy.mapper.SonarrRuleMapper;
import com.lckp.jproxy.model.request.SonarrRuleQueryRequest;
import com.lckp.jproxy.service.ISonarrRuleService;
import com.lckp.jproxy.service.ISystemConfigService;
import com.lckp.jproxy.util.Generator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * SonarrRule 服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @since 2023-03-20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SonarrRuleServiceImpl extends ServiceImpl<SonarrRuleMapper, SonarrRule>
		implements ISonarrRuleService {

	private final ISystemConfigService systemConfigService;

	private final RestTemplate restTemplate;

	@Value("${rules.location}")
	private String rulesLocation;

	private final ISonarrRuleService proxy() {
		return (ISonarrRuleService) AopContext.currentProxy();
	}

	/**
	 * @param token
	 * @return
	 * @see com.lckp.jproxy.service.ISonarrRuleService#query(java.lang.String)
	 */
	@Cacheable(cacheNames = CacheName.SONARR_RULE)
	public List<SonarrRule> query(String token) {
		return proxy().query().eq(TableField.TOKEN, token)
				.eq(TableField.VALID_STATUS, ValidStatus.VALID.getCode()).orderByAsc(TableField.PRIORITY)
				.list();
	}

	/**
	 * @param request
	 * @return
	 * @see com.lckp.jproxy.service.ISonarrRuleService#query(com.lckp.jproxy.model.request.SonarrRuleQueryRequest)
	 */
	@Override
	public IPage<SonarrRule> query(SonarrRuleQueryRequest request) {
		QueryWrapper<SonarrRule> wrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(request.getToken())) {
			wrapper.like(TableField.TOKEN, request.getToken().trim());
		}
		if (StringUtils.isNotBlank(request.getRemark())) {
			wrapper.like(TableField.REMARK, request.getRemark().trim());
		}
		wrapper.orderByDesc(TableField.UPDATE_TIME);
		return proxy().page(request.mybatisPlusPage(), wrapper);
	}

	/**
	 * @return
	 * @see com.lckp.jproxy.service.ISonarrRuleService#sync()
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(cacheNames = CacheName.SONARR_RULE, allEntries = true, condition = "#result == true")
	public synchronized boolean sync() {
		String ruleSyncAuthors = systemConfigService.queryValueByKey(SystemConfigKey.RULE_SYNC_AUTHORS);
		String[] authors;
		if (StringUtils.isBlank(ruleSyncAuthors)) {
			log.debug("规则同步作者为空 {}", ruleSyncAuthors);
			return true;
		} else if (Common.RULE_SYNC_AUTHORS_ALL.equals(ruleSyncAuthors)) {
			String authorUrl = Generator.generateAuthorUrl(rulesLocation);
			String body = restTemplate.getForEntity(authorUrl, String.class).getBody();
			authors = JSON.parseArray(body).toArray(new String[1]);
		} else {
			authors = ruleSyncAuthors.split(",");
		}
		log.debug("Authors: {}", JSON.toJSONString(authors));
		for (String author : authors) {
			try {
				String body = restTemplate
						.getForEntity(Generator.generateSonarrRuleUrl(rulesLocation, author), String.class)
						.getBody();
				log.debug("Sonarr Rules: {}", body);
				List<SonarrRule> sonarrRuleList = JSON.parseArray(body, SonarrRule.class);
				sonarrRuleList.forEach(sonarrRule -> sonarrRule.setValidStatus(null));
				proxy().saveOrUpdateBatch(sonarrRuleList);
			} catch (Exception e) {
				log.error("同步 Sonarr 规则出错：{}", e.getMessage());
			}
		}
		return true;
	}

	/**
	 * @param idList
	 * @param validStatus
	 * @return
	 * @see com.lckp.jproxy.service.ISonarrRuleService#switchValidStatus(java.util.List,
	 *      com.lckp.jproxy.constant.ValidStatus)
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(cacheNames = CacheName.SONARR_RULE, allEntries = true, condition = "#result == true")
	public boolean switchValidStatus(List<String> idList, ValidStatus validStatus) {
		List<SonarrRule> sonarrRuleList = new ArrayList<>(idList.size());
		for (String id : idList) {
			SonarrRule sonarrRule = new SonarrRule();
			sonarrRule.setId(id);
			sonarrRule.setValidStatus(validStatus.getCode());
			sonarrRuleList.add(sonarrRule);
		}
		return proxy().updateBatchById(sonarrRuleList, Common.BATCH_SIZE);
	}
}
