package com.lckp.jproxy.filter.wrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

/**
 * <p>
 * 重写 HttpServletResponseWrapper 以复用
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-04
 */
public class ResponseWrapper extends HttpServletResponseWrapper {

	private ByteArrayOutputStream buffer;
	private ServletOutputStream out;
	
	/**
	 * @param response
	 */
	public ResponseWrapper(HttpServletResponse response) {
		super(response);
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
			// do nothing
		}
	}
}
