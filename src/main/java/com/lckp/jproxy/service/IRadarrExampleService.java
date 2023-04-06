package com.lckp.jproxy.service;

import java.util.Locale;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lckp.jproxy.entity.RadarrExample;
import com.lckp.jproxy.model.request.RadarrExampleQueryRequest;

/**
 * <p>
 * RadarrExample 服务类
 * </p>
 *
 * @author LuckyPuppy514
 * @since 2023-03-30
 */
public interface IRadarrExampleService extends IService<RadarrExample> {
	/**
	 * 
	 * 分页查询
	 *
	 * @param request
	 * @return IPage<RadarrExample>
	 */
	public IPage<RadarrExample> query(RadarrExampleQueryRequest request, Locale locale);
}
