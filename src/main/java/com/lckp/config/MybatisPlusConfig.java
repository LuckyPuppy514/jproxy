/**  
* @Title: MybatisPlusConfig.java
* @version V1.0  
*/
package com.lckp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;

/**
* @ClassName: MybatisPlusConfig
* @Description: mybatis-plus配置
* @author LuckyPuppy514
* @date 2020年7月7日 下午3:43:33
*
*/
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {

  @Bean
  public PaginationInterceptor paginationInterceptor() {
      PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
      // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
      // paginationInterceptor.setOverflow(false);
      // 设置最大单页限制数量，默认 500 条，-1 不受限制
      paginationInterceptor.setLimit(1000);
      // 开启 count 的 join 优化,只针对部分 left join
      paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
      return paginationInterceptor;
  }
}