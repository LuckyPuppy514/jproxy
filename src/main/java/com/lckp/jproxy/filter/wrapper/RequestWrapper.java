package com.lckp.jproxy.filter.wrapper;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

/**
 * <p>
 * 重写 HttpServletRequestWrapper 以复用
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-04
 */
public class RequestWrapper extends HttpServletRequestWrapper {

	private Map<String, String[]> parameterMap;

	private Charset charset;

	public RequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		parameterMap = new HashMap<>(request.getParameterMap());
		String encode = getCharacterEncoding();
		try {
			charset = Charset.forName(encode);
		} catch (Exception e) {
			charset = Charset.forName(StandardCharsets.UTF_8.name());
		}
	}

	public void setParameter(String name, String value) {
		parameterMap.put(name, new String[] { value });
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return parameterMap;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return new Vector<String>(parameterMap.keySet()).elements();
	}

	@Override
	public String[] getParameterValues(String name) {
		return parameterMap.get(name);
	}

	@Override
	public String getParameter(String name) {
		String[] values = parameterMap.get(name);
		if (values != null && values.length > 0) {
			return values[0];
		}
		return null;
	}

	@Override
	public String getQueryString() {
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			builder.append("&");
			builder.append(URLEncoder.encode(entry.getKey(), charset));
			builder.append("=");
			builder.append(URLEncoder.encode(entry.getValue()[0], charset));
		}
		if (builder.length() > 0) {
			builder.replace(0, 1, "");
		}
		return builder.toString();
	}
}
