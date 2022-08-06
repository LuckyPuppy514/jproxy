/**
 * @Title: QBittorrentProxyServlet.java
 * @version V1.0
 */
package com.lckp.proxy;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.URIUtils;
import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lckp.config.JProxyConfiguration;

/**
 * @className: QBittorrentProxyServlet
 * @description: qBittorrent 代理 servlet
 * @date 2022年8月4日
 * @author LuckyPuppy514
 */
public class QBittorrentProxyServlet extends ProxyServlet {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(QBittorrentProxyServlet.class);
	
	private static final long serialVersionUID = 2290078556412263841L;

	@Override
    protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
		if(!JProxyConfiguration.isInit()) {
			LOGGER.error("数据未初始化，请稍后再试");
			return;
		}
		StringBuffer buffer = new StringBuffer();
		if (!JProxyConfiguration.qBittorrent.getProxyIp().startsWith("http")) {
			buffer.append("http://");
		}
		buffer.append(JProxyConfiguration.qBittorrent.getProxyIp());
		buffer.append(":" + JProxyConfiguration.qBittorrent.getProxyPort());
		String targetUri = buffer.toString();
        servletRequest.setAttribute(ATTR_TARGET_URI, null);
        super.targetUri = targetUri;
        URI uri = null;
        try {
            uri = new URI(targetUri);
        } catch (URISyntaxException e) {
        	LOGGER.error("qBittorrent 代理 servlet 创建 URI 出错：{}", targetUri, e);
        }
        servletRequest.setAttribute(ATTR_TARGET_HOST, null);
        super.targetHost = URIUtils.extractHost(uri);
        super.service(servletRequest, servletResponse);
    }
}