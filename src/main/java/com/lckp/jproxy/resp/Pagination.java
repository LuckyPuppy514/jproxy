/**  
* @Title: Pagination.java
* @version V1.0  
*/
package com.lckp.jproxy.resp;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: Pagination
 * @Description: description
 * @author Liujiawang
 * @date 2020年7月7日 下午3:49:59
 *
 */
@ApiModel("分页")
public class Pagination<T> {
	@ApiModelProperty(value = "结果数组")
	private List<T> list;
	@ApiModelProperty(value = "页码")
	private long pageIndex;
	@ApiModelProperty(value = "页长")
	private long pageSize;
	@ApiModelProperty(value = "总条数")
	private long total;
	@ApiModelProperty(value = "总页数")
	private long pageCount;
	
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public long getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(long pageIndex) {
		this.pageIndex = pageIndex;
	}
	public long getPageSize() {
		return pageSize;
	}
	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public long getPageCount() {
		return pageCount;
	}
	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}

}
