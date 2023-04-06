package com.lckp.jproxy.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lckp.jproxy.constant.Messages;
import com.lckp.jproxy.constant.SystemConfigKey;
import com.lckp.jproxy.constant.TableField;
import com.lckp.jproxy.constant.Token;
import com.lckp.jproxy.constant.ValidStatus;
import com.lckp.jproxy.entity.RadarrExample;
import com.lckp.jproxy.entity.RadarrRule;
import com.lckp.jproxy.entity.RadarrTitle;
import com.lckp.jproxy.mapper.RadarrExampleMapper;
import com.lckp.jproxy.model.request.RadarrExampleQueryRequest;
import com.lckp.jproxy.service.IRadarrExampleService;
import com.lckp.jproxy.service.IRadarrRuleService;
import com.lckp.jproxy.service.IRadarrTitleService;
import com.lckp.jproxy.service.ISystemConfigService;
import com.lckp.jproxy.util.FormatUtil;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 * RadarrExample 服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @since 2023-03-30
 */
@Service
@RequiredArgsConstructor
public class RadarrExampleServiceImpl extends ServiceImpl<RadarrExampleMapper, RadarrExample>
		implements IRadarrExampleService {

	private final ISystemConfigService systemConfigService;

	private final IRadarrRuleService radarrRuleService;

	private final IRadarrTitleService radarrTitleService;

	private final MessageSource messageSource;
	
	private final IRadarrExampleService proxy() {
		return (IRadarrExampleService) AopContext.currentProxy();
	}

	/**
	 * @param request
	 * @return
	 * @see com.lckp.jproxy.service.IRadarrExampleService#query(com.lckp.jproxy.model.request.RadarrExampleQueryRequest)
	 */
	@Override
	public IPage<RadarrExample> query(RadarrExampleQueryRequest request, Locale locale) {
		QueryWrapper<RadarrExample> wrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(request.getOriginalText())) {
			wrapper.like(TableField.ORIGINAL_TEXT, request.getOriginalText().trim());
		}
		wrapper.orderByDesc(TableField.UPDATE_TIME);
		Page<RadarrExample> result = request.mybatisPlusPage();
		List<RadarrExample> resultList = null;
		if (request.getValidStatus() == null) {
			result = proxy().page(request.mybatisPlusPage(), wrapper);
			resultList = result.getRecords();
		} else {
			resultList = proxy().list(wrapper);
		}
		if (!resultList.isEmpty()) {
			String format = systemConfigService.queryValueByKey(SystemConfigKey.RADARR_INDEXER_FORMAT);
			String cleanTitleRegex = systemConfigService.queryValueByKey(SystemConfigKey.CLEAN_TITLE_REGEX);
			Map<String, List<RadarrRule>> tokenRuleMap = new ConcurrentHashMap<>();
			Matcher matcher = Pattern.compile(Token.REGEX).matcher(format);
			while (matcher.find()) {
				String token = matcher.group(1);
				tokenRuleMap.put(token, radarrRuleService.query(token));
			}
			List<RadarrTitle> radarrTitleList = radarrTitleService.queryAll();
			for (RadarrExample radarrExample : resultList) {
				radarrExample.setFormatText(radarrExample.getOriginalText());
				radarrExample.setValidStatus(ValidStatus.INVALID.getCode());
				String formatText = radarrTitleService.formatTitle(radarrExample.getOriginalText(), format,
						cleanTitleRegex, tokenRuleMap.get(Token.TITLE), radarrTitleList);
				if (formatText.contains("{" + Token.TITLE + "}")) {
					formatText = FormatUtil.replaceToken(Token.TITLE,
							messageSource.getMessage(Messages.EXAMPLE_MATCH_TITLE_FAIL, null, locale),
							formatText);
				}
				formatText = radarrTitleService.format(radarrExample.getOriginalText(), formatText,
						tokenRuleMap);
				radarrExample.setFormatText(formatText);
				if (!radarrExample.getOriginalText().equals(radarrExample.getFormatText())) {
					radarrExample.setValidStatus(ValidStatus.VALID.getCode());
				}
			}
		}
		// 手动分页
		if (request.getValidStatus() != null && !resultList.isEmpty()) {
			resultList = resultList.stream()
					.filter(example -> request.getValidStatus().equals(example.getValidStatus())).toList();
			result.setTotal(resultList.size());
			result.setPages((long) Math.ceil(result.getTotal() / 1.0 / result.getSize()));
			int si = (int) ((result.getCurrent() - 1) * result.getSize());
			int ei = (int) (si + result.getSize());
			ei = ei <= resultList.size() ? ei : resultList.size();
			result.setRecords((resultList.subList(si, ei)));
		}
		return result;
	}

}
