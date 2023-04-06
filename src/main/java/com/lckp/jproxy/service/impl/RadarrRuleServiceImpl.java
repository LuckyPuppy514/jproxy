package com.lckp.jproxy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
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
import com.lckp.jproxy.entity.RadarrRule;
import com.lckp.jproxy.mapper.RadarrRuleMapper;
import com.lckp.jproxy.model.request.RadarrRuleQueryRequest;
import com.lckp.jproxy.service.IRadarrRuleService;
import com.lckp.jproxy.service.ISystemConfigService;
import com.lckp.jproxy.util.Generator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * RadarrRule 服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @since 2023-03-20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RadarrRuleServiceImpl extends ServiceImpl<RadarrRuleMapper, RadarrRule>
		implements IRadarrRuleService {

	private final ISystemConfigService systemConfigService;

	private final RestTemplate restTemplate;

	private final IRadarrRuleService proxy() {
		return (IRadarrRuleService) AopContext.currentProxy();
	}

	/**
	 * 
	 * @param token
	 * @return
	 * @see com.lckp.jproxy.service.IRadarrRuleService#query(java.lang.String)
	 */
	@Cacheable(cacheNames = CacheName.RADARR_RULE)
	public List<RadarrRule> query(String token) {
		return proxy().query().eq(TableField.TOKEN, token)
				.eq(TableField.VALID_STATUS, ValidStatus.VALID.getCode()).orderByAsc(TableField.PRIORITY)
				.list();
	}

	/**
	 * @param request
	 * @return
	 * @see com.lckp.jproxy.service.IRadarrRuleService#query(com.lckp.jproxy.model.request.RadarrRuleQueryRequest)
	 */
	@Override
	public IPage<RadarrRule> query(RadarrRuleQueryRequest request) {
		QueryWrapper<RadarrRule> wrapper = new QueryWrapper<>();
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
	 * @see com.lckp.jproxy.service.IRadarrRuleService#sync()
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(cacheNames = CacheName.RADARR_RULE, allEntries = true, condition = "#result == true")
	public synchronized boolean sync() {
		String ruleSyncAuthors = systemConfigService.queryValueByKey(SystemConfigKey.RULE_SYNC_AUTHORS);
		String[] authors;
		if (StringUtils.isBlank(ruleSyncAuthors)) {
			log.debug("规则同步作者为空 {}", ruleSyncAuthors);
			return true;
		} else if (Common.RULE_SYNC_AUTHORS_ALL.equals(ruleSyncAuthors)) {
			String authorUrl = Generator.generateAuthorUrl();
			String body = restTemplate.getForEntity(authorUrl, String.class).getBody();
			authors = JSON.parseArray(body).toArray(new String[1]);
		} else {
			authors = ruleSyncAuthors.split(",");
		}
		log.debug("Authors: {}", JSON.toJSONString(authors));
		for (String author : authors) {
			try {
				String body = restTemplate.getForEntity(Generator.generateRadarrRuleUrl(author), String.class)
						.getBody();
				log.debug("Radarr Rules: {}", body);
				List<RadarrRule> radarrRuleList = JSON.parseArray(body, RadarrRule.class);
				radarrRuleList.forEach(radarrRule -> radarrRule.setValidStatus(null));
				proxy().saveOrUpdateBatch(radarrRuleList, Common.BATCH_SIZE);
			} catch (Exception e) {
				log.error("同步 Radarr 规则出错：{}", e.getMessage());
			}
		}
		return true;
	}

	/**
	 * @param idList
	 * @param validStatus
	 * @return
	 * @see com.lckp.jproxy.service.IRadarrRuleService#switchValidStatus(java.util.List,
	 *      com.lckp.jproxy.constant.ValidStatus)
	 */
	@Override
	@CacheEvict(cacheNames = CacheName.RADARR_RULE, allEntries = true, condition = "#result == true")
	public boolean switchValidStatus(List<String> idList, ValidStatus validStatus) {
		List<RadarrRule> radarrRuleList = new ArrayList<>(idList.size());
		for (String id : idList) {
			RadarrRule radarrRule = new RadarrRule();
			radarrRule.setId(id);
			radarrRule.setValidStatus(validStatus.getCode());
			radarrRuleList.add(radarrRule);
		}
		return proxy().updateBatchById(radarrRuleList, Common.BATCH_SIZE);
	}
}
