package com.lckp.jproxy.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * <p>
 * 基础过滤器
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-19
 */
public abstract class BaseFilter implements Filter {

	/**
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 * @see jakarta.servlet.Filter#doFilter(jakarta.servlet.ServletRequest,
	 *      jakarta.servlet.ServletResponse, jakarta.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		chain.doFilter(request, response);
	}

	/**
	 * 
	 * 把结果写入 response
	 *
	 * @param content
	 * @param response
	 * @throws IOException void
	 */
	public void writeToResponse(byte[] content, ServletResponse response) throws IOException {
		ServletOutputStream out = response.getOutputStream();
		response.setContentLength(content.length);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		out.write(content);
		out.flush();
	}

	/**
	 * 
	 * 把结果写入 response
	 *
	 * @param content
	 * @param response
	 * @throws IOException void
	 */
	public void writeToResponse(String content, ServletResponse response) throws IOException {
		writeToResponse(content.getBytes(StandardCharsets.UTF_8), response);
	}
}
