package com.lckp.service.spring;


import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lckp.mapper.IRuleTestExampleMapper;
import com.lckp.param.RuleTestExampleBatchParam;
import com.lckp.param.RuleTestExampleQueryParam;
import com.lckp.resp.Pagination;
import com.lckp.resp.RuleTestExampleQueryResp;
import com.lckp.service.facade.IRuleTestExampleService;

/**
* @ClassName: RuleTestExampleServiceImpl
* @Description: 规则测试用例serviceImpl
* @author LuckyPuppy514
* @date 2022-07-15 17:03:17
*
*/
@Service("ruleTestExampleService")
public class RuleTestExampleServiceImpl implements IRuleTestExampleService {
	
	@Autowired
	private IRuleTestExampleMapper ruleTestExampleMapper;

	/**
	 * 查询规则测试用例
	 */
	@Override
	public Pagination<RuleTestExampleQueryResp> query(RuleTestExampleQueryParam param) {
		Pagination<RuleTestExampleQueryResp> result = new Pagination<>();
		result.setPageIndex(param.getPageIndex());
		result.setPageSize(param.getPageSize());
		
		if (StringUtils.isNotBlank(param.getFormatStatus())) {
			List<RuleTestExampleQueryResp> resultList = ruleTestExampleMapper.queryNoPage(param);
			resultList = resultList.stream().filter(new Predicate<RuleTestExampleQueryResp>() {
				@Override
				public boolean test(RuleTestExampleQueryResp example) {
					return example.getFormatStatus().equals(param.getFormatStatus());
				}
			}).collect(Collectors.toList());
						
			result.setTotal(resultList.size());
			result.setPageCount((long) Math.ceil(result.getTotal()/1.0/result.getPageSize()));
			int si = (int) ((result.getPageIndex() - 1) * result.getPageSize());
			int ei = (int) (si + result.getPageSize());
			ei = ei <= resultList.size() ? ei : resultList.size(); 
			result.setList(resultList.subList(si, ei));
			
		} else {
			Page<?>page = new Page<>();
			page.setCurrent(param.getPageIndex());
			page.setSize(param.getPageSize());
			IPage<RuleTestExampleQueryResp> resultList = ruleTestExampleMapper.query(page, param);
			result.setTotal(resultList.getTotal());
			result.setPageCount(resultList.getPages());
			result.setList(resultList.getRecords());
		}
		
		return result;
	}

	/**
	 * 删除规则测试用例
	 */
	@Override
	public int delete(List<RuleTestExampleBatchParam> paramList) {
		return ruleTestExampleMapper.delete(paramList);
	}

	/**
	 * 新增规则测试用例
	 */
	@Override
	public int add(String ruleId, String[] contents) {
		return ruleTestExampleMapper.add(ruleId, contents);
	}

	/**
	 * 根据规则配置 ID 删除
	 */
	@Override
	public int deleteByRuleId(String ruleId) {
		return ruleTestExampleMapper.deleteByRuleId(ruleId);
	}

}
