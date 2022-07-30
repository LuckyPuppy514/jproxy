/**  
* @Title: ResVo.java
* @version V1.0  
*/
package com.lckp.resp;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
* @ClassName: ResVo
* @Description: 接口通用返参
* @author LuckyPuppy514
* @date 2020年6月18日 下午3:08:10
*
*/
@ApiModel(value = "接口通用返参")
public class ResVo<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ResVo.class);

	private static final int SUCCESS_CODE = 200;
	private static final String SUCCESS_MSG = "success";
	
	private static final int FAIL_CODE = 400;
	private static final String FAIL_MSG = "fail";
	
	private static final int ERROR_CODE = 500;
	private static final String ERROR_MSG = "error";
	
    @ApiModelProperty(value = "状态码（200：成功，其他：失败）", example = "200")
    private int code;

    @ApiModelProperty(value = "数据")
	private T data;

    @ApiModelProperty(value = "状态信息", example = "success")
	private String msg;
	
	public ResVo(int code, T data, String msg){
		this.code = code;
		this.data = data;
		this.msg = msg;
	}
	
	public static <T> ResVo<T> success() {
		return response(SUCCESS_CODE, null, SUCCESS_MSG);
	}
	
	public static <T> ResVo<T> success(T data) {
		return response(SUCCESS_CODE, data, SUCCESS_MSG);
	}
	
	public static <T> ResVo<T> success(String msg, MessageSource messageSource, Locale locale) {
		return response(SUCCESS_CODE, null, messageSource.getMessage(msg, null, locale));
	}
	
	public static <T> ResVo<T> fail(){
		return response(FAIL_CODE, null, FAIL_MSG);
	}
	
	public static <T> ResVo<T> fail(String msg) {
		return response(FAIL_CODE, null, msg);
	}
	
	public static <T> ResVo<T> fail(String msg, MessageSource messageSource, Locale locale) {
		return response(FAIL_CODE, null, messageSource.getMessage(msg, null, locale));
	}
	
	public static <T> ResVo<T> error(){
		return response(ERROR_CODE, null, ERROR_MSG);
	}
	
	public static <T> ResVo<T> error(String msg) {
		return response(ERROR_CODE, null, msg);
	}
	
	public static <T> ResVo<T> response(int code, T data, String msg) {
		return new ResVo<>(code, data, msg);
	}
	
	/**
	 * 
	 * @Title: alert
	 * @Description: 弹窗
	 * @return void    返回类型
	 * @throws Exception 
	 */
	public static void alert(String msg, HttpServletResponse response) {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf8");
		try {
			response.getOutputStream()
					.write(("<script>alert('" + msg + "')</script>").getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			LOGGER.error("弹窗错误：{}", e.getMessage());
		}
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
