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

import com.lckp.jproxy.model.Rule;

/**
 * @className: FormatUtil
 * @description: 格式化工具类
 * @date 2022年6月10日
 * @author LuckyPuppy514
 */
public class FormatUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FormatUtil.class);

	/**
	 * 
	 * @param xml: jackett 返回结果
	 * @param rule: 替换规则
	 * @return 根据规则替换后的xml
	 * @throws DocumentException
	 * @description: 格式化 jackett 结果中的标题
	 */
	public static String format(String xml, Rule rule) throws DocumentException {
		if (StringUtils.isBlank(xml)) {
			return xml;
		}
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		Element channel = root.element("channel");
		if (null == channel) {
			return xml;
		}
		
		for (Iterator<Element> items = channel.elementIterator("item"); items.hasNext();) {
			Element item = items.next();
			Element titleElement = item.element("title");
			String title = titleElement.getText();
			LOGGER.debug("title before format: {}", title);
			
			title = accurateFormat(title, rule.getAccurate());
			if (title == null) {
				title = commonFormat(titleElement.getText(), rule.getCommon());
			}
			
			LOGGER.debug("title after format: {}", title);
			titleElement.setText(title);
		}
		return document.asXML();
	}
	
	/**
	 * 
	 * @param title
	 * @param rules
	 * @return
	 * @description: 精确匹配，成功一次，则返回
	 */
	public static String accurateFormat(String title, List<String> rules) {
		String oldTitle = title;
		for (String rule : rules) {
			title =  format(title, rule);
			if (isFormat(oldTitle, title)) {
				return title;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param title
	 * @param rules
	 * @return
	 * @description: 公共匹配，执行所有规则
	 */
	public static String commonFormat(String title, List<String> rules) {
		for (String rule : rules) {
			title =  format(title, rule);
		}
		return title;
	}

	/**
	 * 
	 * @param content
	 * @param rule
	 * @return
	 * @description: 根据规则，替换内容
	 */
	public static String format(String content, String rule) {
		String[] ra = rule.split("\\}:\\{");
		ra[0] = ra[0].substring(1);
		ra[1] = ra[1].substring(0, ra[1].length() - 1);
		return content.replaceAll(ra[0], ra[1]);
	}
	/**
	 * 
	 * @param oldContent
	 * @param newContent
	 * @return
	 * @description: 检查是否格式化过
	 */
	private static boolean isFormat(String oldContent, String newContent) {
		return !oldContent.equals(newContent);
	}
}
