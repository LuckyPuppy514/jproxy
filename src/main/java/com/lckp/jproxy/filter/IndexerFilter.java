package com.lckp.jproxy.filter;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.lckp.jproxy.constant.ApiField;
import com.lckp.jproxy.filter.wrapper.RequestWrapper;
import com.lckp.jproxy.model.request.IndexerRequest;
import com.lckp.jproxy.service.IIndexerService;
import com.lckp.jproxy.util.XmlUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 索引器过滤器
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-20
 */
@Slf4j
@RequiredArgsConstructor
public abstract class IndexerFilter extends BaseFilter {

	private final IIndexerService indexerService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		RequestWrapper requestWrapper = new RequestWrapper((HttpServletRequest) request);
		IndexerRequest indexerRequest = getIndexerRequest(requestWrapper);

		// 处理查询
		String xml = "";
		String searchKey = indexerRequest.getSearchKey();
		if (StringUtils.isNotBlank(searchKey)) {
			// 无绝对集数，去除 00
			searchKey = searchKey.replaceAll(" 00$", "");
			indexerRequest.setSearchKey(searchKey);
			// 获取所有待查询标题
			String title = indexerService.getTitle(indexerRequest.getSearchKey());
			List<String> searchTitleList = indexerService.getSearchTitle(title);
			// 计算 offset
			int offset = indexerRequest.getOffset();
			int size = searchTitleList.size();
			String offsetKey = indexerService.generateOffsetKey(requestWrapper);
			List<Integer> offsetList = indexerService.getOffsetList(offsetKey, size);
			// 计算当前标题下标
			int index = indexerService.calculateCurrentIndex(offset, offsetList);
			// 更新参数
			indexerService.updateIndexerRequest(index, searchTitleList, offsetList, indexerRequest);
			updateRequestWrapper(indexerRequest, requestWrapper);
			// 请求
			xml = indexerService.executeNewRequest(requestWrapper);
			int count = XmlUtil.count(xml);
			index++;
			while (index < size && indexerRequest.getLimit() - count > 0) {
				// 更新参数
				offset = offset + count;
				if (StringUtils.isNotBlank(indexerRequest.getSeasonNumber()) && index == size - 1
						&& offset < indexerRequest.getLimit()) {
					indexerRequest.setSeasonNumber(null);
					indexerRequest.setEpisodeNumber(null);
				} else {
					break;
				}
				indexerRequest.setOffset(offset);
				indexerRequest.setLimit(indexerRequest.getLimit() - count);
				offsetList.set(index - 1, offset);
				indexerService.updateOffsetList(offsetKey, offsetList);
				indexerService.updateIndexerRequest(index, searchTitleList, offsetList, indexerRequest);
				updateRequestWrapper(indexerRequest, requestWrapper);
				// 重新请求
				String newXml = indexerService.executeNewRequest(requestWrapper);
				count = XmlUtil.count(newXml);
				if (count > 0) {
					xml = XmlUtil.merger(xml, newXml);
				}
				index++;
			}
		} else {
			xml = indexerService.executeNewRequest(requestWrapper);
		}
		// 处理结果
		xml = indexerService.executeFormatRule(xml);
		log.debug("\n{}", xml);
		// 返回
		writeToResponse(xml, response);
	}

	/**
	 * 
	 * 获取索引器参数
	 *
	 * @param requestWrapper
	 * @return IndexerRequest
	 */
	public IndexerRequest getIndexerRequest(RequestWrapper requestWrapper) {
		IndexerRequest indexerRequest = new IndexerRequest();
		indexerRequest.setSearchKey(requestWrapper.getParameter(ApiField.INDEXER_SEARCH_KEY));
		indexerRequest.setSearchType(requestWrapper.getParameter(ApiField.INDEXER_SEARCH_TYPE));
		String seasonNumber = requestWrapper.getParameter(ApiField.INDEXER_SEASON_NUMBER);
		indexerRequest.setSeasonNumber(seasonNumber);
		String episodeNumber = requestWrapper.getParameter(ApiField.INDEXER_EPISODE_NUMBER);
		indexerRequest.setEpisodeNumber(episodeNumber);
		String offset = requestWrapper.getParameter(ApiField.INDEXER_OFFSET);
		indexerRequest.setOffset(offset == null ? null : Integer.valueOf(offset));
		String limit = requestWrapper.getParameter(ApiField.INDEXER_LIMIT);
		indexerRequest.setLimit(limit == null ? null : Integer.valueOf(limit));
		return indexerRequest;
	}

	/**
	 * 
	 * 更新请求参数
	 *
	 * @param indexerRequest
	 * @param requestWrapper void
	 */
	public void updateRequestWrapper(IndexerRequest indexerRequest, RequestWrapper requestWrapper) {
		if (StringUtils.isNotBlank(indexerRequest.getSearchKey())) {
			requestWrapper.setParameter(ApiField.INDEXER_SEARCH_KEY, indexerRequest.getSearchKey());
		}
		if (StringUtils.isNotBlank(indexerRequest.getSearchType())) {
			requestWrapper.setParameter(ApiField.INDEXER_SEARCH_TYPE, indexerRequest.getSearchType());
		}
		if (indexerRequest.getSeasonNumber() != null) {
			requestWrapper.setParameter(ApiField.INDEXER_SEASON_NUMBER, indexerRequest.getSeasonNumber());
		}
		if (indexerRequest.getEpisodeNumber() != null) {
			requestWrapper.setParameter(ApiField.INDEXER_EPISODE_NUMBER, indexerRequest.getEpisodeNumber());
		}
		if (indexerRequest.getOffset() != null) {
			requestWrapper.setParameter(ApiField.INDEXER_OFFSET, String.valueOf(indexerRequest.getOffset()));
		}
		if (indexerRequest.getLimit() != null) {
			requestWrapper.setParameter(ApiField.INDEXER_LIMIT, String.valueOf(indexerRequest.getLimit()));
		}
	}
}
