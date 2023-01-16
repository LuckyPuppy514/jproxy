package com.lckp.service.spring;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lckp.config.JProxyConfiguration;
import com.lckp.mapper.IRuleConfigMapper;
import com.lckp.model.RuleConfig;
import com.lckp.param.RuleConfigAddParam;
import com.lckp.param.RuleConfigBatchParam;
import com.lckp.param.RuleConfigEditParam;
import com.lckp.param.RuleConfigQueryParam;
import com.lckp.service.facade.IRuleConfigService;

/**
* @ClassName: RuleConfigServiceImpl
* @Description: 规则配置serviceImpl
* @author LuckyPuppy514
* @date 2022-07-19 06:34:23
*
*/
@Service("ruleConfigService")
public class RuleConfigServiceImpl implements IRuleConfigService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RuleConfigServiceImpl.class);

	@Autowired
	private IRuleConfigMapper ruleConfigMapper;
	@Autowired
	private JProxyConfiguration jproxyConfiguration;

	/**
	 * 新增规则配置
	 */
	@Override
	public int add(RuleConfigAddParam param) {
		int result = ruleConfigMapper.add(param);
		jproxyConfiguration.initRuleConfig();
		return result;
	}

	/**
	 * 查询规则配置
	 */
	@Override
	public IPage<RuleConfig> query(RuleConfigQueryParam param) {
		Page<?>page = new Page<>();
		page.setCurrent(param.getPageIndex());
		page.setSize(param.getPageSize());
		return ruleConfigMapper.query(page, param);
	}
	
	/**
	 * 查询规则名称列表
	 */
	@Override
	public List<RuleConfig> queryRuleNameList() {
		return ruleConfigMapper.queryRuleNameList();
	}
	
	/**
	 * 删除规则配置
	 */
	@Override
	public int delete(List<RuleConfigBatchParam> paramList) {
		ruleConfigMapper.deleteExample(paramList);
		return ruleConfigMapper.delete(paramList);
	}

	/**
	 * 变更有效状态
	 */
	@Override
	public int changeValidstatus(List<RuleConfigBatchParam> paramList, String validstatus) {
		int result = ruleConfigMapper.changeValidstatus(paramList, validstatus);
		jproxyConfiguration.initRuleConfig();
		return result;
	}
	
	/**
	 * 导出规则配置
	 */
	@Override
	public List<RuleConfigAddParam> export(List<RuleConfigBatchParam> paramList) {
		return ruleConfigMapper.export(paramList);
	}

	/**
	 * 导入规则配置
	 */
	@Override
	public int importRuleConfig(List<RuleConfig> ruleConfigList) {
		int sum = ruleConfigList.size();
        int success = ruleConfigMapper.importRuleConfig(ruleConfigList);
        LOGGER.info("共：{} 条数据，成功导入：{} 条数据", sum, success);
        jproxyConfiguration.initRuleConfig();
		return success;
	}

	/**
	 * 查询规则配置
	 */
	@Override
	public List<RuleConfig> query(String regularType) {
		return ruleConfigMapper.queryNoPage(regularType);
	}

	/**
	 * 根据规则 ID 查询
	 */
	@Override
	public RuleConfigEditParam queryByRuleId(String ruleId) {
		return ruleConfigMapper.queryByRuleId(ruleId);
	}

	/**
	 * 编辑规则配置
	 */
	@Override
	public int editRuleConfig(RuleConfigEditParam param) {
		return editRuleConfig(param, true);
	}
	
	@Override
	public int editRuleConfig(RuleConfigEditParam param, boolean reload) {
		int result = ruleConfigMapper.editRuleConfig(param);
		if(reload) {
			jproxyConfiguration.initRuleConfig();
		}
		return result;
	}

	/**
	 * 增加下载次数
	 */
	@Override
	public int addDownloadCount(List<RuleConfigBatchParam> paramList) {
		return ruleConfigMapper.addDownloadCount(paramList);
	}

	/**
	 * 查询所有下载的规则ID
	 */
	@Override
	public List<String> queryAllDownloadRuleId() {
		return ruleConfigMapper.queryAllDownloadRuleId();
	}

	/**
	 * 查询所有规则ID
	 */
	@Override
	public List<String> queryAllRuleId() {
		return ruleConfigMapper.queryAllRuleId();
	}
}
