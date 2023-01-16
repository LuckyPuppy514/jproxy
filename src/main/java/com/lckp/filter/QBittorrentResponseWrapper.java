/**
 * @Title: QBittorrentResponseWrapper.java
 * @version V1.0
 */
package com.lckp.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @className: QBittorrentResponseWrapper
 * @description: QBittorrentResponseWrapper
 * @date 2023年1月16日
 * @author LuckyPuppy514
 */
public class QBittorrentResponseWrapper extends HttpServletResponseWrapper {

	private ByteArrayOutputStream buffer;
	private ServletOutputStream out;

	public QBittorrentResponseWrapper(HttpServletResponse httpServletResponse) {
		super(httpServletResponse);
		buffer = new ByteArrayOutputStream();
		out = new WrapperOutputStream(buffer);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return out;
	}

	@Override
	public void flushBuffer() throws IOException {
		if (out != null) {
			out.flush();
		}
	}

	public byte[] getContent() throws IOException {
		flushBuffer();
		return buffer.toByteArray();
	}

	class WrapperOutputStream extends ServletOutputStream {
		private ByteArrayOutputStream bos;

		public WrapperOutputStream(ByteArrayOutputStream bos) {
			this.bos = bos;
		}

		@Override
		public void write(int b) throws IOException {
			bos.write(b);
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setWriteListener(WriteListener arg0) {
		}
	}
}