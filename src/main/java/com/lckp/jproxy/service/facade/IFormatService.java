package com.lckp.jproxy.service.facade;

import com.lckp.jproxy.param.FormatAccurateTestParam;
import com.lckp.jproxy.resp.FormatAccurateTestResp;

/**
* @ClassName: IFormatService
* @Description: 格式化service
* @author LuckyPuppy514
* @date 2022-06-17 14:30:20
*
*/
public interface IFormatService {
	// 精确匹配测试
	public FormatAccurateTestResp accurateTest(FormatAccurateTestParam param);
}
