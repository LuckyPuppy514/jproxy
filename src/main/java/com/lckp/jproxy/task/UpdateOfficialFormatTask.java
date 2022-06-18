/**
 * @Title: UpdateOfficialFormatTask.java
 * @version V1.0
 */
package com.lckp.jproxy.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.alibaba.fastjson.JSON;

import reactor.core.publisher.Mono;

/**
 * @className: UpdateOfficialFormatTask
 * @description: 定时更新官方正则配置
 * @date 2022年6月18日
 * @author LuckyPuppy514
 */
@Component
@EnableScheduling
public class UpdateOfficialFormatTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateOfficialFormatTask.class);
	
	@Value("${task.update.official.url}")
	private String url;
	@Value("${task.update.official.filename}")
	private String filename;
	@Value("${path.profiles.official}")
	private String officialPath;
	
	@Autowired
	private ContextRefresher contextRefresher;
	
	@Scheduled(cron = "${task.update.official.cron}")
	public void run() {
		LOGGER.info("开始更新官方正则配置");
		LOGGER.info("更新地址：{}", url);
		WebClient webClient = WebClient.create();
		Mono<ClientResponse> mono = webClient.get().uri(url).exchange();
		ClientResponse response = mono.block();
		if (HttpStatus.OK == response.statusCode()) {
			String yml = response.bodyToMono(String.class).block();
			LOGGER.info("获取配置成功：{}", yml);
			
			if (StringUtils.isBlank(officialPath)) {
				officialPath = this.getClass().getResource("/" + filename).getPath();
				String system = System.getProperty("os.name");
				if (system.toLowerCase().contains("window")) {
					officialPath = officialPath.substring(1);
				}
			}
			
			LOGGER.info("获取配置文件路径成功：{}", officialPath);
		
			File ymlFile = new File(officialPath);
			try {
				FileUtils.copyFile(ymlFile, new File(officialPath+".bak"));
			} catch (IOException e) {
				LOGGER.error("备份配置文件出错", e);
				return;
			}
			
			
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(ymlFile));
				writer.write(yml);
				writer.flush();
				writer.close();
				
				// 重载配置文件
				contextRefresher.refresh();
				
			} catch (IOException e) {
				LOGGER.error("更新配置文件出错", e);
			}
		} else {
			LOGGER.info("获取配置失败：{}", JSON.toJSONString(response));
		}
		
		LOGGER.info("更新官方正则配置完毕");
	}
}
