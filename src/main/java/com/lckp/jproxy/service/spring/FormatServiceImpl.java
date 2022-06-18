package com.lckp.jproxy.service.spring;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lckp.jproxy.config.FormatConfig;
import com.lckp.jproxy.constant.SeriesType;
import com.lckp.jproxy.interceptor.ApiProxy;
import com.lckp.jproxy.model.Regular;
import com.lckp.jproxy.param.FormatAccurateTestParam;
import com.lckp.jproxy.resp.FormatAccurateTestResp;
import com.lckp.jproxy.service.facade.IFormatService;
import com.lckp.jproxy.util.FormatUtil;

/**
* @ClassName: FormatServiceImpl
* @Description: 格式化serviceImpl
* @author LuckyPuppy514
* @date 2022-06-17 14:30:20
*
*/
@Service("formatService")
public class FormatServiceImpl implements IFormatService {
	
	@Autowired
	private FormatConfig formatConfig;

	/**
	 * 精确匹配测试
	 */
	@Override
	public FormatAccurateTestResp accurateTest(FormatAccurateTestParam param) {
		FormatAccurateTestResp resp = new FormatAccurateTestResp();
		List<Regular> common =  ApiProxy.formatType.getAnime().getCommon();
		List<Regular> accurate =  ApiProxy.formatType.getAnime().getAccurate();
		resp.setSeriesType(SeriesType.ANIME);
		if (SeriesType.SERIAL.equals(param.getSeriesType())) {
			common =  ApiProxy.formatType.getSerial().getCommon();
			accurate =  ApiProxy.formatType.getSerial().getAccurate();
			resp.setSeriesType(SeriesType.SERIAL);
		}
		
		if (StringUtils.isNoneBlank(param.getExample1())) {
			resp.setFormatResult1(format(param.getExample1(), common, accurate, param, resp));
		}
		if (StringUtils.isNoneBlank(param.getExample2())) {
			resp.setFormatResult2(format(param.getExample2(), common, accurate, param, resp));
		}
		if (StringUtils.isNoneBlank(param.getExample3())) {
			resp.setFormatResult3(format(param.getExample3(), common, accurate, param, resp));
		}
		
		
		return resp;
	}
	
	private String format(String title, List<Regular> common, List<Regular> accurate,FormatAccurateTestParam param, FormatAccurateTestResp resp) {
		if (formatConfig.isEnable()) {
			title = FormatUtil.commonFormat(title, common);
			title = FormatUtil.accurateFormat(title, accurate);
		}

		if (StringUtils.isNotBlank(param.getMatch()) && StringUtils.isNotBlank(param.getReplace())) {
			String old = title;
			title = title.replaceAll(param.getMatch(), param.getReplace());
			if (!title.equals(old)) {
				resp.setMatch(param.getMatch());
				resp.setReplace(param.getReplace());
				
			}
		}
		return title;
	}

}
