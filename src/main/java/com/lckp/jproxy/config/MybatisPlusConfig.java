package com.lckp.jproxy.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.lckp.jproxy.constant.TableField;

/**
 * 
 * <p>
 * Mybatis-Plus 配置类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-04
 */
@Configuration
@EnableTransactionManagement
public class MybatisPlusConfig implements MetaObjectHandler {

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Bean
	MybatisPlusInterceptor mybatisPlusInterceptor() {
		PaginationInnerInterceptor pagination = new PaginationInnerInterceptor(DbType.SQLITE);
		pagination.setOverflow(true);
		pagination.setMaxLimit(1000L);
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(pagination);
		return interceptor;
	}

	/**
	 * @param metaObject
	 * @see com.baomidou.mybatisplus.core.handlers.MetaObjectHandler#insertFill(org.apache.ibatis.reflection.MetaObject)
	 */
	@Override
	public void insertFill(MetaObject metaObject) {
		this.setFieldValByName(TableField.CREATE_TIME_CAMEL, dateFormat.format(new Date()), metaObject);
		this.setFieldValByName(TableField.UPDATE_TIME_CAMEL, dateFormat.format(new Date()), metaObject);
	}

	/**
	 * @param metaObject
	 * @see com.baomidou.mybatisplus.core.handlers.MetaObjectHandler#updateFill(org.apache.ibatis.reflection.MetaObject)
	 */
	@Override
	public void updateFill(MetaObject metaObject) {
		this.setFieldValByName(TableField.UPDATE_TIME_CAMEL, dateFormat.format(new Date()), metaObject);
	}
}