package com.lckp.jproxy.filter;

import java.io.IOException;

import com.lckp.jproxy.filter.wrapper.ResponseWrapper;
import com.lckp.jproxy.service.IDownloaderService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 下载器过滤器
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-17
 */
@RequiredArgsConstructor
public abstract class DownloaderFilter extends BaseFilter {
	
	private final IDownloaderService downloaderService;

	/**
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 * @see com.lckp.jproxy.filter.BaseFilter#doFilter(jakarta.servlet.ServletRequest,
	 *      jakarta.servlet.ServletResponse, jakarta.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);
		chain.doFilter(request, responseWrapper);
		byte[] content = responseWrapper.getContent();
		content = downloaderService.executeFormatRule(content);
		writeToResponse(content, response);
	}
}
