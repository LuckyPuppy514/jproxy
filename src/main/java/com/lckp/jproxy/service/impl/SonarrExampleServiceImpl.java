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
import com.lckp.jproxy.entity.SonarrExample;
import com.lckp.jproxy.entity.SonarrRule;
import com.lckp.jproxy.entity.SonarrTitle;
import com.lckp.jproxy.mapper.SonarrExampleMapper;
import com.lckp.jproxy.model.request.SonarrExampleQueryRequest;
import com.lckp.jproxy.service.ISonarrExampleService;
import com.lckp.jproxy.service.ISonarrRuleService;
import com.lckp.jproxy.service.ISonarrTitleService;
import com.lckp.jproxy.service.ISystemConfigService;
import com.lckp.jproxy.util.FormatUtil;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 * SonarrExample 服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @since 2023-03-30
 */
@Service
@RequiredArgsConstructor
public class SonarrExampleServiceImpl extends ServiceImpl<SonarrExampleMapper, SonarrExample>
		implements ISonarrExampleService {

	private final ISystemConfigService systemConfigService;

	private final ISonarrRuleService sonarrRuleService;

	private final ISonarrTitleService sonarrTitleService;

	private final MessageSource messageSource;

	private final ISonarrExampleService proxy() {
		return (ISonarrExampleService) AopContext.currentProxy();
	}

	/**
	 * @param request
	 * @return
	 * @see com.lckp.jproxy.service.ISonarrExampleService#query(com.lckp.jproxy.model.request.SonarrExampleQueryRequest)
	 */
	@Override
	public IPage<SonarrExample> query(SonarrExampleQueryRequest request, Locale locale) {
		QueryWrapper<SonarrExample> wrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(request.getOriginalText())) {
			wrapper.like(TableField.ORIGINAL_TEXT, request.getOriginalText().trim());
		}
		wrapper.orderByDesc(TableField.UPDATE_TIME);
		Page<SonarrExample> result = request.mybatisPlusPage();
		List<SonarrExample> resultList = null;
		if (request.getValidStatus() == null) {
			result = page(request.mybatisPlusPage(), wrapper);
			resultList = result.getRecords();
		} else {
			resultList = proxy().list(wrapper);
		}
		if (!resultList.isEmpty()) {
			String format = systemConfigService.queryValueByKey(SystemConfigKey.SONARR_INDEXER_FORMAT);
			String cleanTitleRegex = systemConfigService.queryValueByKey(SystemConfigKey.CLEAN_TITLE_REGEX);
			Map<String, List<SonarrRule>> tokenRuleMap = new ConcurrentHashMap<>();
			Matcher matcher = Pattern.compile(Token.REGEX).matcher(format);
			while (matcher.find()) {
				String token = matcher.group(1);
				tokenRuleMap.put(token, sonarrRuleService.query(token));
			}
			List<SonarrTitle> sonarrTitleList = sonarrTitleService.queryAll();
			for (SonarrExample sonarrExample : resultList) {
				sonarrExample.setFormatText(sonarrExample.getOriginalText());
				sonarrExample.setValidStatus(ValidStatus.INVALID.getCode());
				String formatText = sonarrTitleService.formatTitle(sonarrExample.getOriginalText(), format,
						cleanTitleRegex, tokenRuleMap.get(Token.TITLE), sonarrTitleList);
				if (formatText.contains("{" + Token.TITLE + "}")) {
					formatText = FormatUtil.replaceToken(Token.TITLE,
							messageSource.getMessage(Messages.EXAMPLE_MATCH_TITLE_FAIL, null, locale),
							formatText);
				}
				formatText = sonarrTitleService.format(sonarrExample.getOriginalText(), formatText,
						tokenRuleMap);
				sonarrExample.setFormatText(formatText);
				if (!sonarrExample.getOriginalText().equals(sonarrExample.getFormatText())) {
					sonarrExample.setValidStatus(ValidStatus.VALID.getCode());
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
