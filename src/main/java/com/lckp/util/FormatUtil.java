/**
 * @Title: FormatUtil.java
 * @version V1.0
 */
package com.lckp.util;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lckp.constant.ExecuteRule;
import com.lckp.constant.Field;
import com.lckp.model.RuleConfig;

/**
 * @className: FormatUtil
 * @description: 格式化工具类
 * @date 2022年7月21日
 * @author LuckyPuppy514
 */
public class FormatUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(FormatUtil.class);
	
	/**
	 * 
	 * @param searchKey
	 * @param ruleConfigList
	 * @return
	 * @description: 搜索关键字格式化
	 */
	public static String search(String searchKey, List<RuleConfig> ruleList) {
		LOGGER.debug("查询关键字 - 格式化前：{}", searchKey);
		if (StringUtils.isBlank(searchKey)) {
			return searchKey;
		}
		searchKey = format(searchKey, ruleList);
		LOGGER.debug("查询关键字 - 格式化后：{}", searchKey);
		return searchKey;
	}
	
	/**
	 * 
	 * @param resultXml
	 * @param ruleConfigList
	 * @return
	 * @throws DocumentException 
	 * @description: 结果 xml 格式化
	 */
	public static String result(String resultXml, List<RuleConfig> ruleList) throws DocumentException {
		
		Document document = DocumentHelper.parseText(resultXml);
		Element root = document.getRootElement();
		Element channel = root.element(Field.RESP_CHANNEL);
		if (null == channel) {
			return resultXml;
		}

		for (Iterator<Element> items = channel.elementIterator(Field.RESP_ITEM); items.hasNext();) {
			Element item = items.next();
			Element titleElement = item.element(Field.RESP_TITLE);
			String title = titleElement.getText();
			
			LOGGER.debug("结果标题 - 格式化前：{}", title);
			title = format(titleElement.getText(), ruleList);
			LOGGER.debug("结果标题 - 格式化后：{}", title);
			
			titleElement.setText(title);
		}
		return document.asXML();
	}
	
	/**
	 * 
	 * @param content
	 * @param ruleList
	 * @return
	 * @description: 通过规则格式化
	 */
	public static String format(String content, List<RuleConfig> ruleList) {
		boolean onceMatchFlag = false;
		for (RuleConfig rule : ruleList) {
			boolean isOnce = rule.getExecuteRule().equals(ExecuteRule.Once.toString());
			if (onceMatchFlag && isOnce) {
				continue;
			}
			
			String old = content;
			content = content.replaceAll(rule.getRegularMatch(), rule.getRegularReplace());
			if (isOnce && !onceMatchFlag && !old.equals(content)) {
				onceMatchFlag = true;
			}
		}
		return content;
	}
}
