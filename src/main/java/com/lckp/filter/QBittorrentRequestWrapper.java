/**
 * @Title: QBittorrentRequestWrapper.java
 * @version V1.0
 */
package com.lckp.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lckp.config.JProxyConfiguration;
import com.lckp.util.FormatUtil;

/**
 * @className: QBittorrentRequestWrapper
 * @description: 重写 HttpServletRequestWrapper
 * @date 2022年8月6日
 * @author LuckyPuppy514
 */
public class QBittorrentRequestWrapper extends HttpServletRequestWrapper {
	private static final Logger LOGGER = LoggerFactory.getLogger(QBittorrentRequestWrapper.class);
	
	private ByteArrayOutputStream cacheStream;
	
	private int contentLength = 0;
	
	private final static String TORRENT_URL_FIELD = "urls";
	private final static String NAME_FIELD = "dn";
	private final static String RENAME_FIELD = "rename";
	private final static String FILENAME_FIELD = "filename";
	private final static String CONTENT_LENGTH_FIELD = "content-length";
	
	public QBittorrentRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		
		cacheStream = new ByteArrayOutputStream();		
		byte[] buffer = new byte[8192];
		int length = request.getInputStream().read(buffer);
		if(length == -1) {
			return;
		}
		
		// 从第一部分尝试获取名称，并格式化
		String firstString = new String(buffer, StandardCharsets.UTF_8);
		byte[] formatNameByte;
		boolean isTorrentFile = false;
		// Torrent URL，一般是 BT
		if(firstString.startsWith(TORRENT_URL_FIELD)) {
			formatNameByte = getTorrentUrlName(firstString);
			// Torrent URL，添加 rename 参数重命名
			if(formatNameByte != null) {
				writeToCacheStream(formatNameByte, 0, formatNameByte.length);
			}
		
		// Torrent File，一般是 PT
		} else {
			isTorrentFile = true;
			formatNameByte = getTorrentFileName(firstString);
		}
		
		while (-1 != length) {
			LOGGER.debug("Add Torrent Body: {}", new String(buffer, StandardCharsets.UTF_8));
			byte[] oldBuffer = new byte[length];
			System.arraycopy(buffer, 0, oldBuffer, 0, length);
			length = request.getInputStream().read(buffer);
			
			// Torrent File，添加 rename 参数重命名
			if (isTorrentFile && -1 == length && formatNameByte != null) {
				writeToCacheStream(oldBuffer, 0, oldBuffer.length - 4);
				writeToCacheStream(formatNameByte, 0, formatNameByte.length);
				
			} else {
				writeToCacheStream(oldBuffer, 0, oldBuffer.length);
			}
		}

		cacheStream.flush();
	}
	
	/**
	 * 
	 * @param b
	 * @param off
	 * @param len
	 * @description: 写入 cacheStream，并更新 contentLength
	 */
	public void writeToCacheStream(byte[] b, int off, int len) {
		cacheStream.write(b, 0, len);
		contentLength = contentLength + len;
	}
	
	/**
	 * 
	 * @param firstString
	 * @return
	 * @throws UnsupportedEncodingException
	 * @description: 获取 TorrentUrl 名称并格式化
	 */
	public byte[] getTorrentUrlName(String firstString) {
		try {
			firstString = URLDecoder.decode(firstString, StandardCharsets.UTF_8.toString());
			int startIndex = firstString.indexOf(NAME_FIELD);
			if (startIndex != -1) {
				String name = firstString.substring(startIndex + NAME_FIELD.length() + 1);
				name = name.substring(0, name.indexOf("&"));
				if(StringUtils.isNotBlank(name)) {
					name = URLDecoder.decode(name, StandardCharsets.UTF_8.toString());
					name = FormatUtil.format(name, JProxyConfiguration.resultRuleList);
					LOGGER.info("TorrentUrl 格式化后名称：{}", name);
					name = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
					name = RENAME_FIELD + "=" + name + "&";
					return name.getBytes(StandardCharsets.UTF_8);
				}
			}
			LOGGER.info("TorrentUrl 未包含名称", firstString);
			
		} catch (Exception e) {
			LOGGER.error("获取 TorrentUrl 名称并格式化出错", e);
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param firString
	 * @return
	 * @throws UnsupportedEncodingException
	 * @description: 获取 TorrentFile 名称并格式化
	 */
	public byte[] getTorrentFileName(String firstString) {
		try {
			int startIndex = firstString.indexOf(FILENAME_FIELD);
			String name = null;
			if (startIndex != -1) {
				name = firstString.substring(startIndex + FILENAME_FIELD.length() + 2);
				name = name.substring(0, name.indexOf(".torrent\""));
				name = FormatUtil.format(name, JProxyConfiguration.resultRuleList);
	
			} else {
				startIndex = firstString.indexOf(NAME_FIELD);
				if (startIndex != -1) {
					name = firstString.substring(startIndex + NAME_FIELD.length() + 1);
					name = name.substring(0, name.indexOf("&"));
					name = URLDecoder.decode(name, StandardCharsets.UTF_8.toString());
					name = FormatUtil.format(name, JProxyConfiguration.resultRuleList);
				}
			}
			
			if(StringUtils.isNotBlank(name)) {
				LOGGER.info("TorrentFile 格式化后名称：{}", name);
				name = "\r\nContent-Disposition: form-data; name=\"rename\"\r\n\r\n" + name;
				return name.getBytes(StandardCharsets.UTF_8);
			}
			LOGGER.info("TorrentFile 未包含名称", firstString);
			
		} catch (Exception e) {
			LOGGER.error("获取 TorrentFile 名称并格式化出错", e);
		}
		return null;
	}
	
	@Override
	public String getHeader(String name) {
		// 重写 inputstream 后，需要重写 content-length，否则会丢失数据
		if (CONTENT_LENGTH_FIELD.equalsIgnoreCase(name) && 0 != contentLength) {
			return String.valueOf(contentLength);
		}
		return super.getHeader(name);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new CachedServletInputStream();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	public class CachedServletInputStream extends ServletInputStream {
		private final ByteArrayInputStream inputStream;

		public CachedServletInputStream() {
			inputStream = new ByteArrayInputStream(cacheStream.toByteArray());
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setReadListener(ReadListener readListener) {

		}

		@Override
		public int read() throws IOException {
			return inputStream.read();
		}
	}
}
