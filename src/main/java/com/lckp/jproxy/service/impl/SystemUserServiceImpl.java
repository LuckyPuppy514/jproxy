package com.lckp.jproxy.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lckp.jproxy.constant.CacheName;
import com.lckp.jproxy.constant.TableField;
import com.lckp.jproxy.entity.SystemUser;
import com.lckp.jproxy.mapper.SystemUserMapper;
import com.lckp.jproxy.service.ISystemUserService;

import jakarta.annotation.PostConstruct;
import liquibase.util.MD5Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * SystemUser 服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @since 2023-03-24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser>
		implements ISystemUserService {

	private final CacheManager cacheManager;

	@Value("${time.token-expires}")
	private long tokenExpires;

	private String secret;

	private final ISystemUserService proxy() {
		return (ISystemUserService) AopContext.currentProxy();
	}

	/**
	 * @param systemUser
	 * @return
	 * @see com.lckp.jproxy.service.ISystemUserService#check(com.lckp.jproxy.entity.SystemUser)
	 */
	@Override
	public boolean check(SystemUser systemUser) {
		List<SystemUser> systemUserList = proxy().query().eq(TableField.USERNAME, systemUser.getUsername())
				.list();
		if (systemUserList.isEmpty()) {
			return false;
		}
		if (systemUserList.get(0).getPassword().equals(md5(systemUser.getPassword()))) {
			systemUser.setId(systemUserList.get(0).getId());
			systemUser.setRole(systemUserList.get(0).getRole());
			systemUser.setValidStatus(systemUserList.get(0).getValidStatus());
			return true;
		}
		return false;
	}

	/**
	 * @param systemUser
	 * @return
	 * @see com.lckp.jproxy.service.ISystemUserService#sign(com.lckp.jproxy.entity.SystemUser)
	 */
	@Override
	public String sign(SystemUser systemUser) {
		Date expireDate = new Date(System.currentTimeMillis() + tokenExpires * 60000L);
		Map<String, Object> map = new HashMap<>();
		map.put("alg", "HS256");
		map.put("typ", "JWT");
		return JWT.create().withHeader(map).withClaim(TableField.ID, systemUser.getId())
				.withClaim(TableField.USERNAME, systemUser.getUsername())
				.withClaim(TableField.PASSWORD, systemUser.getPassword())
				.withClaim(TableField.ROLE, systemUser.getRole())
				.withClaim(TableField.VALID_STATUS, systemUser.getValidStatus()).withExpiresAt(expireDate)
				.withIssuedAt(new Date()).sign(Algorithm.HMAC256(secret));
	}

	/**
	 * @param token
	 * @return
	 * @see com.lckp.jproxy.service.ISystemUserService#verify(java.lang.String)
	 */
	@Override
	public boolean verify(String token) {
		if (StringUtils.isBlank(token)) {
			return false;
		}
		Cache cache = cacheManager.getCache(CacheName.TOKEN_BLACK_LIST);
		if (cache != null && cache.get(token) != null) {
			return false;
		}
		try {
			token = removeBearer(token);
			JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
		} catch (Exception e) {
			log.debug("invalid token: {}", e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * @param token
	 * @see com.lckp.jproxy.service.ISystemUserService#logout(java.lang.String)
	 */
	@Override
	@Cacheable(cacheNames = CacheName.TOKEN_BLACK_LIST, key = "#token")
	public boolean logout(String token) {
		return true;
	}

	/**
	 * @param token
	 * @return
	 * @see com.lckp.jproxy.service.ISystemUserService#getSystemUser(java.lang.String)
	 */
	@Override
	public SystemUser getSystemUser(String token) {
		token = removeBearer(token);
		DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
		SystemUser systemUser = new SystemUser();
		systemUser.setId(jwt.getClaim(TableField.ID).asInt());
		systemUser.setUsername(jwt.getClaim(TableField.USERNAME).asString());
		systemUser.setPassword(jwt.getClaim(TableField.PASSWORD).asString());
		systemUser.setRole(jwt.getClaim(TableField.ROLE).asString());
		systemUser.setValidStatus(jwt.getClaim(TableField.VALID_STATUS).asInt());
		return systemUser;
	}

	/**
	 * @param systemUser
	 * @return
	 * @see com.lckp.jproxy.service.ISystemUserService#update(com.lckp.jproxy.entity.SystemUser)
	 */
	@Override
	public boolean update(SystemUser systemUser) {
		systemUser.setPassword(md5(systemUser.getPassword()));
		return proxy().saveOrUpdate(systemUser);
	}

	private String removeBearer(String token) {
		return token.replace("Bearer ", "");
	}

	private String md5(String password) {
		return MD5Util.computeMD5(password).toUpperCase();
	}

	@PostConstruct
	private void initSecret() {
		secret = md5(UUID.randomUUID().toString());
		log.info("初始化 secret 成功：{}", secret.substring(0, 5) + "******" + secret.substring(27));
	}
}
