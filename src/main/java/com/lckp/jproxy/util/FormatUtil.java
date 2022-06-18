/**
 * @Title: FormatUtil.java
 * @version V1.0
 */
package com.lckp.jproxy.util;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lckp.jproxy.constant.ResponseXml;
import com.lckp.jproxy.model.FormatRule;
import com.lckp.jproxy.model.Regular;

/**
 * @className: FormatUtil
 * @description: 格式化工具类
 * @date 2022年6月17日
 * @author LuckyPuppy514
 */
public class FormatUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FormatUtil.class);
	
	/**
	 * 
	 * @param searchKey 查询关键字
	 * @param search 查询替换规则
	 * @return 新查询关键字或 null
	 * @description: 根据查询替换规则格式化查询关键字
	 */
	public static String formatSearchKey(String searchKey, List<Regular> search) {
		if (StringUtils.isBlank(searchKey)) {
			return searchKey;
		}
		String newSearchKey;
		for (Regular regular : search) {
			newSearchKey = searchKey.replaceAll(regular.getMatch(), regular.getReplace());
			if (!searchKey.equals(newSearchKey)) {
				return newSearchKey;
			}
		}
		return searchKey;
	}
	
	/**
	 * 
	 * @param xml 接口返回 xml
	 * @param all 所有替换规则 
	 * @return 格式化后的 xml
	 * @throws DocumentException 
	 * @description: 根据规则格式化 xml
	 */
	public static String formatXml(String xml, FormatRule rule) throws DocumentException {
		if (StringUtils.isBlank(xml)) {
			return xml;
		}
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		Element channel = root.element(ResponseXml.CHANNEL);
		if (null == channel) {
			return xml;
		}

		for (Iterator<Element> items = channel.elementIterator(ResponseXml.ITEM); items.hasNext();) {
			Element item = items.next();
			Element titleElement = item.element(ResponseXml.TITLE);
			String title = titleElement.getText();
			
			LOGGER.debug("title before format: {}", title);
			title = commonFormat(titleElement.getText(), rule.getCommon());
			title = accurateFormat(title, rule.getAccurate());
			LOGGER.debug("title after format: {}", title);
			
			titleElement.setText(title);
		}
		return document.asXML();
	}
	
	/**
	 * 
	 * @param content 内容
	 * @param common 通用规则
	 * @return 格式化后内容
	 * @description: 用所有通用规则逐个进行格式化
	 */
	public static String commonFormat(String content, List<Regular> common) {
		for (Regular regular : common) {
			content = content.replaceAll(regular.getMatch(), regular.getReplace());
		}
		return content;
	}
	
	/**
	 * 
	 * @param content 内容
	 * @param accurate 精确规则
	 * @return 格式化后内容
	 * @description: 用精确规则格式化，成功一次则返回
	 */
	public static String accurateFormat(String content, List<Regular> accurate) {
		String old = content;
		for (Regular regular : accurate) {
			content = old.replaceAll(regular.getMatch(), regular.getReplace());
			if (!old.equals(content)) {
				return content;
			}
		}
		return old;
	}
}
