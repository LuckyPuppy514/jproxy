package com.lckp.jproxy.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 压缩工具类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-18
 */
public class GzipUtil {
	GzipUtil() {
	}

	private static final int BUFFER_SIZE = 8192;

	/**
	 * 
	 * 解压
	 *
	 * @param zipBytes
	 * @return
	 * @throws IOException String
	 */
	public static String decompress(byte[] zipBytes) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(zipBytes));
		byte[] buffer = new byte[BUFFER_SIZE];
		int len;
		while ((len = gzipInputStream.read(buffer)) != -1) {
			byteArrayOutputStream.write(buffer, 0, len);
		}
		return byteArrayOutputStream.toString();
	}

	/**
	 * 
	 * 压缩
	 *
	 * @param content
	 * @return
	 * @throws IOException byte[]
	 */
	public static byte[] compress(String content) throws IOException {
		if (StringUtils.isEmpty(content)) {
			return null;
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
		gzipOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
		gzipOutputStream.flush();
		gzipOutputStream.finish();
		return byteArrayOutputStream.toByteArray();
	}
}
