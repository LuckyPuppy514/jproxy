package com.lckp.jproxy.service;

import java.util.List;

import com.lckp.jproxy.filter.wrapper.RequestWrapper;
import com.lckp.jproxy.model.request.IndexerRequest;

/**
 * <p>
 * 索引器服务类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-20
 */
public interface IIndexerService {
	/**
	 * 
	 * 提取标题
	 *
	 * @return String
	 */
	public String getTitle(String searchKey);

	/**
	 * 
	 * 获取所有待查询标题
	 *
	 * @param title
	 * @return List<String>
	 */
	public List<String> getSearchTitle(String title);

	/**
	 * 
	 * 生成 offset key
	 *
	 * @param requestWrapper
	 * @return String
	 */
	public String generateOffsetKey(RequestWrapper requestWrapper);

	/**
	 * 
	 * 获取 offset 列表
	 *
	 * @param key
	 * @param size
	 * @return List<Integer>
	 */
	public List<Integer> getOffsetList(String key, int size);

	/**
	 * 
	 * 计算当前标题下标
	 *
	 * @param offset
	 * @param offsetList
	 * @return int
	 */
	public int calculateCurrentIndex(int offset, List<Integer> offsetList);

	/**
	 * 
	 * 更新索引器参数
	 *
	 * @param index
	 * @param searchTitleList
	 * @param offsetList
	 * @param indexerRequest  void
	 */
	public void updateIndexerRequest(int index, List<String> searchTitleList, List<Integer> offsetList,
			IndexerRequest indexerRequest);

	/**
	 * 
	 * 更新 offset 列表
	 *
	 * @param key
	 * @param offsetList
	 * @return List<Integer>
	 */
	public List<Integer> updateOffsetList(String key, List<Integer> offsetList);

	/**
	 * 
	 * 执行新请求
	 *
	 * @param requestWrapper
	 * @return String
	 */
	public String executeNewRequest(RequestWrapper requestWrapper);

	/**
	 * 
	 * 执行格式化规则
	 *
	 * @return String
	 */
	public String executeFormatRule(String xml);

	/**
	 * 
	 * 获取追加主标题（去除季数集数）搜索时最少结果数量
	 *
	 * @return int
	 */
	public int getMinCount();
}
