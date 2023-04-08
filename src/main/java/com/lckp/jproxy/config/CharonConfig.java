package com.lckp.jproxy.config;

import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.configuration.RequestMappingConfigurer.requestMapping;
import static com.github.mkopylec.charon.forwarding.RestTemplateConfigurer.restTemplate;
import static com.github.mkopylec.charon.forwarding.TimeoutConfigurer.timeout;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RegexRequestPathRewriterConfigurer.regexRequestPathRewriter;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestServerNameRewriterConfigurer.requestServerNameRewriter;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestHostHeaderRewriterConfigurer.requestHostHeaderRewriter;

import java.time.Duration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.mkopylec.charon.configuration.CharonConfigurer;
import com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestServerNameRewriterConfigurer;
import com.lckp.jproxy.constant.Common;
import com.lckp.jproxy.constant.SystemConfigKey;
import com.lckp.jproxy.service.ISystemConfigService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Charon 反向代理配置
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-06
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CharonConfig {

	private final ISystemConfigService systemConfigService;

	private static final Duration TIMEOUT = Duration.ofMinutes(60);

	private static Map<String, RequestServerNameRewriterConfigurer> serverMap = new ConcurrentHashMap<>();

	private CharonConfigurer charonConfigurer;

	@Bean
	CharonConfigurer charonConfigurer() {
		charonConfigurer = charonConfiguration()
				.set(restTemplate().set(timeout().connection(TIMEOUT).read(TIMEOUT).write(TIMEOUT)));
		addServer(SystemConfigKey.QBITTORRENT_URL, Common.CHARON_QBITTORRENT_PATH, "http://127.0.0.1:8080");
		addServer(SystemConfigKey.TRANSMISSION_URL, Common.CHARON_TRANSMISSION_PATH, "http://127.0.0.1:9091");
		return charonConfigurer;
	}

	/**
	 * 
	 * 添加服务器
	 *
	 * @param key
	 * @param path
	 * @param url  void
	 */
	private void addServer(String key, String path, String url) {
		serverMap.put(key, requestServerNameRewriter().outgoingServers(url));
		charonConfigurer.add(requestMapping(key).pathRegex(path + Common.CHARON_ALL_PATH)
				.set(serverMap.get(key)).set(requestHostHeaderRewriter())
				.set(regexRequestPathRewriter().paths(path + Common.CHARON_IN_PATH, Common.CHARON_OUT_PATH)));
	}

	/**
	 * 
	 * 更新所有服务器地址
	 *
	 * void
	 */
	public void updateAllServerUrl() {
		for (Entry<String, RequestServerNameRewriterConfigurer> entry : serverMap.entrySet()) {
			try {
				entry.getValue().outgoingServers(systemConfigService.queryValueByKey(entry.getKey()));
			} catch (Exception e) {
				log.error("更新 Charon 服务地址出错：{}", e.getMessage());
			}
		}
	}
}
