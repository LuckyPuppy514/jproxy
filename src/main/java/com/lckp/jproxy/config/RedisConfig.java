package com.lckp.jproxy.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

/**
 * 
 * <p>
 * Redis 配置类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-04
 */
@Configuration
public class RedisConfig implements CachingConfigurer {

	@Value("${time.cache-expires}")
	private long cacheExpires;

	@Bean
	RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setDefaultSerializer(getRedisSerializer());
		return template;
	}

	@Bean
	RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofMinutes(cacheExpires))
				.serializeKeysWith(RedisSerializationContext.SerializationPair
						.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(
						RedisSerializationContext.SerializationPair.fromSerializer(getRedisSerializer()))
				.disableCachingNullValues();
		return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(config).build();
	}

	/**
	 * 
	 * 获取 Redis 序列化类
	 *
	 * @return RedisSerializer<Object>
	 */
	private RedisSerializer<Object> getRedisSerializer() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		mapper.activateDefaultTyping(new LaissezFaireSubTypeValidator(),
				ObjectMapper.DefaultTyping.NON_FINAL);
		return new Jackson2JsonRedisSerializer<>(mapper, Object.class);
	}
}