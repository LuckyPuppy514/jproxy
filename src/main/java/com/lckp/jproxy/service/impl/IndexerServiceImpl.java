package com.lckp.jproxy.service.impl;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lckp.jproxy.constant.CacheName;
import com.lckp.jproxy.filter.wrapper.RequestWrapper;
import com.lckp.jproxy.model.request.IndexerRequest;
import com.lckp.jproxy.service.IIndexerService;
import com.lckp.jproxy.util.FormatUtil;

/**
 * <p>
 * 索引器服务实现类
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-20
 */
@Service
public class IndexerServiceImpl implements IIndexerService {

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * @param searchKey
	 * @return
	 * @see com.lckp.jproxy.service.IIndexerService#getTitle(java.lang.String)
	 */
	@Override
	public String getTitle(String searchKey) {
		return FormatUtil.removeEpisode(searchKey);
	}

	/**
	 * @param title
	 * @return
	 * @see com.lckp.jproxy.service.IIndexerService#getSearchTitle(java.lang.String)
	 */
	@Override
	public List<String> getSearchTitle(String title) {
		return new ArrayList<>();
	}

	/**
	 * @param requestWrapper
	 * @return
	 * @see com.lckp.jproxy.service.IIndexerService#generateOffsetKey(com.lckp.jproxy.filter.wrapper.RequestWrapper)
	 */
	@Override
	public String generateOffsetKey(RequestWrapper requestWrapper) {
		StringBuilder builder = new StringBuilder();
		builder.append(requestWrapper.getServletPath());
		builder.append("?" + requestWrapper.getQueryString().replaceAll("(offset=\\d+|apikey=\\w+)", ""));
		return builder.toString();
	}

	/**
	 * @param offset
	 * @param offsetList
	 * @return
	 * @see com.lckp.jproxy.service.IIndexerService#calculateCurrentIndex(int,
	 *      java.util.List)
	 */
	@Override
	public int calculateCurrentIndex(int offset, List<Integer> offsetList) {
		for (int index = 0; index < offsetList.size(); index++) {
			if (Integer.valueOf(-1).equals(offsetList.get(index))
					|| offsetList.get(index).intValue() > offset) {
				return index;
			}
		}
		return offsetList.size() - 1;
	}

	/**
	 * @param key
	 * @return
	 * @see com.lckp.jproxy.service.IIndexerService#getOffsetList(java.lang.String)
	 */
	@Override
	@Cacheable(cacheNames = CacheName.INDEXER_SEARCH_OFFSET, key = "#key")
	public List<Integer> getOffsetList(String key, int size) {
		List<Integer> offsetList = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			offsetList.add(-1);
		}
		return offsetList;
	}

	/**
	 * @param index
	 * @param searchTitleList
	 * @param offsetList
	 * @param indexerRequest
	 * @see com.lckp.jproxy.service.IIndexerService#updateIndexerRequest(int,
	 *      java.util.List, java.util.List,
	 *      com.lckp.jproxy.model.request.IndexerRequest)
	 */
	@Override
	public void updateIndexerRequest(int index, List<String> searchTitleList, List<Integer> offsetList,
			IndexerRequest indexerRequest) {
		if (index > 0) {
			int lastIndex = index - 1;
			String title = searchTitleList.get(index);
			String searchKey = indexerRequest.getSearchKey();
			searchKey = searchKey.replace(searchTitleList.get(0), title);
			if (lastIndex > 0) {
				searchKey = searchKey.replace(searchTitleList.get(lastIndex), title);
			}
			indexerRequest.setSearchKey(searchKey);
			indexerRequest.setOffset(indexerRequest.getOffset() - offsetList.get(lastIndex));
		}
	}

	/**
	 * @param key
	 * @param offsetList
	 * @see com.lckp.jproxy.service.IIndexerService#updateOffsetList(java.lang.String,
	 *      java.util.List)
	 */
	@Override
	@CachePut(cacheNames = CacheName.INDEXER_SEARCH_OFFSET, key = "#key")
	public List<Integer> updateOffsetList(String key, List<Integer> offsetList) {
		return offsetList;
	}

	/**
	 * @param requestWrapper
	 * @return
	 * @see com.lckp.jproxy.service.IIndexerService#executeNewRequest(com.lckp.jproxy.filter.wrapper.RequestWrapper)
	 */
	@Override
	public String executeNewRequest(RequestWrapper requestWrapper) {
		String path = requestWrapper.getServletPath();
		StringBuilder url = getIndexerUrl(path);
		url.append("?" + URLDecoder.decode(requestWrapper.getQueryString(), StandardCharsets.UTF_8));
		String xml = restTemplate.getForEntity(url.toString(), String.class).getBody();
		if (StringUtils.isNotBlank(xml)) {
			xml = new String(xml.getBytes(getCharset()), StandardCharsets.UTF_8);
		}
		return xml;
	}

	/**
	 * @param xml
	 * @return
	 * @see com.lckp.jproxy.service.IIndexerService#executeFormatRule(java.lang.String)
	 */
	@Override
	public String executeFormatRule(String xml) {
		return xml;
	}

	/**
	 * 
	 * 获取索引器地址
	 *
	 * @return StringBuilder
	 */
	protected StringBuilder getIndexerUrl(String path) {
		return new StringBuilder(path);
	}

	/**
	 * 
	 * 获取编码
	 *
	 * @return Charset
	 */
	protected Charset getCharset() {
		return StandardCharsets.UTF_8;
	}

	/**
	 * @return
	 * @see com.lckp.jproxy.service.IIndexerService#getMinCount()
	 */
	@Override
	public int getMinCount() {
		return -1;
	}
}
