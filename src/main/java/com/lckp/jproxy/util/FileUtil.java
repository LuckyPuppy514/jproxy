package com.lckp.jproxy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 文件工具类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-29
 */
@Slf4j
public class FileUtil {
	FileUtil() {
	}

	/**
	 * 
	 * 读取文件
	 *
	 * @param multipartFile
	 * @return
	 * @throws Exception String
	 */
	public static String read(MultipartFile multipartFile) throws Exception {
		if (multipartFile == null) {
			return null;
		}
		File file = File.createTempFile("/tmp", multipartFile.getOriginalFilename());
		multipartFile.transferTo(file);
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			return builder.toString();
		} catch (Exception e) {
			log.error("读取文件出错：", e);
			throw e;
		}
	}
}
