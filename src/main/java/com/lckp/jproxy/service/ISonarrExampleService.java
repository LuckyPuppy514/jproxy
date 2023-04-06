package com.lckp.jproxy.service;

import java.util.Locale;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lckp.jproxy.entity.SonarrExample;
import com.lckp.jproxy.model.request.SonarrExampleQueryRequest;

/**
 * <p>
 * SonarrExample 服务类
 * </p>
 *
 * @author LuckyPuppy514
 * @since 2023-03-30
 */
public interface ISonarrExampleService extends IService<SonarrExample> {
	/**
	 * 
	 * 分页查询
	 *
	 * @param request
	 * @return IPage<SonarrExample>
	 */
	public IPage<SonarrExample> query(SonarrExampleQueryRequest request, Locale locale);
}
