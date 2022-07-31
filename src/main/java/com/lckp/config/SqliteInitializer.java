/**
 * @Title: SqliteInitializer.java
 * @version V1.0
 */
package com.lckp.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

/**
 * @className: SqliteInitializer
 * @description: sqlite初始化
 * @date 2022年7月15日
 * @author LuckyPuppy514
 */
@Configuration
public class SqliteInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger(SqliteInitializer.class);
	
	@Value("${spring.sql.init}")
    private Resource initSql;
 
    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
        try {
            final DataSourceInitializer initializer = new DataSourceInitializer();
            // 设置数据源
            initializer.setDataSource(dataSource);
            initializer.setDatabasePopulator(databasePopulator());
            LOGGER.info("数据库初始化成功");
            return initializer;
        } catch (Exception e) {
        	LOGGER.error("数据库初始化失败：", e);
        }
        return null;
    }
 
    private DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(initSql);
        return populator;
    }
}
