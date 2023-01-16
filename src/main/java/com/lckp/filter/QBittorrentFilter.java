/**
 * @Title: QBittorrentFilter.java
 * @version V1.0
 */
package com.lckp.filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lckp.config.JProxyConfiguration;
import com.lckp.util.FormatUtil;

/**
 * @className: QBittorrentFilter
 * @description: qBittorrent 过滤器
 * @date 2022年8月3日
 * @author LuckyPuppy514
 */
@WebFilter(filterName = "qBittorrentFilter", urlPatterns = "/qbittorrent/api/v2/torrents/info")
public class QBittorrentFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(QBittorrentFilter.class);
	
	// 种子名称 Map
	public static Map<String, String> torrentNameMap = new HashMap<String, String>();
	// 种子名称 Map 最大数量
	private static int MAX_SIZE = 1024;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		QBittorrentResponseWrapper responseWrapper = new QBittorrentResponseWrapper((HttpServletResponse) response);
		filterChain.doFilter(request, responseWrapper);
		byte[] content = responseWrapper.getContent();
		if (content.length > 0) {
			try {
				JSONArray array = JSON.parseArray(gzipDecompress(content));
				for (Object object : array) {
					JSONObject jsonObject = (JSONObject) object;
					// 下载完成的种子，格式化种子名称，以便 sonarr 正确导入
					if (jsonObject.getIntValue("progress") == 1) {
						String hash = jsonObject.getString("hash");
						String oldName = jsonObject.getString("name");
						String newName = torrentNameMap.get(hash);
						if (newName == null) {
							String suffix = "";
							int index = oldName.lastIndexOf(".");
							if (index != -1) {
								suffix = oldName.substring(index);
								oldName = oldName.substring(0, index);
							}
							newName = FormatUtil.format(oldName, JProxyConfiguration.resultRuleList) + suffix;
							torrentNameMap.put(hash, newName);
						}
						if (!oldName.equals(newName)) {
							jsonObject.put("name", newName);
							LOGGER.debug("种子名称格式化：{} => {}", oldName, newName);
						}
					}
				}
				content = gzipCompress(JSON.toJSONString(array));
				responseWrapper.setContentLength(content.length);
			} catch (ZipException e) {
				if (!e.getMessage().equals("Not in GZIP format")) {
					LOGGER.error("qBittorrent 过滤器出错", e);
				}
			} catch (Exception e) {
				LOGGER.error("qBittorrent 过滤器出错", e);
			}
			ServletOutputStream out = response.getOutputStream();
			out.write(content);
			out.flush();
			
			// 清空种子 Map
			if(torrentNameMap.size() > MAX_SIZE) {
				LOGGER.info("种子名称 Map 数量大于 {}，执行清空", MAX_SIZE);
				torrentNameMap = new HashMap<String, String>();
			}
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	/**
	 * 
	 * @param text
	 * @return
	 * @throws IOException
	 * @description: gzip 压缩
	 */
	private static byte[] gzipCompress(String text) throws IOException {
		if (StringUtils.isEmpty(text)) {
			return null;
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
		gzipOutputStream.write(text.getBytes(StandardCharsets.UTF_8));
		gzipOutputStream.flush();
		gzipOutputStream.finish();
		return byteArrayOutputStream.toByteArray();
	}

	/**
	 * 
	 * @param gzip
	 * @return
	 * @throws IOException
	 * @description: gzip 解压
	 */
	private static String gzipDecompress(byte[] gzip) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(gzip));
		byte[] buffer = new byte[1024];
		int len;
		while ((len = gzipInputStream.read(buffer)) != -1) {
			byteArrayOutputStream.write(buffer, 0, len);
		}
		return byteArrayOutputStream.toString();
	}
}
