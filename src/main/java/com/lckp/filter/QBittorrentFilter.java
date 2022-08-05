/**
 * @Title: QBittorrentFilter.java
 * @version V1.0
 */
package com.lckp.filter;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lckp.proxy.QBittorrentProxy;

/**
 * @className: QBittorrentFilter
 * @description: qBittorrent 过滤器
 * @date 2022年8月3日
 * @author LuckyPuppy514
 */
@WebFilter(filterName = "qBittorrentFilter", urlPatterns = "/qbittorrent/api/v2/torrents/add")
public class QBittorrentFilter extends QBittorrentProxy implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(QBittorrentFilter.class);
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {		
		try {
			addTorrentAndRename((HttpServletRequest)servletRequest, (HttpServletResponse)servletResponse);
			return;
		} catch (URISyntaxException | IOException e) {
			LOGGER.error("添加种子并重命名出错：{}", e);
		}
	}
}
