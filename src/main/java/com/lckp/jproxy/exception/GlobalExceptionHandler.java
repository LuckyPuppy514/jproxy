package com.lckp.jproxy.exception;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

import com.lckp.jproxy.constant.Messages;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 全局异常处理
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-03-23
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	private final MessageSource messageSource;

	/**
	 * 
	 * 处理请求参数异常
	 *
	 * @param e
	 * @param request
	 * @return ResponseEntity<Object>
	 */
	@ResponseBody
	@ExceptionHandler(BindException.class)
	public ResponseEntity<Object> dealBindException(BindException e, HttpServletRequest request) {
		StringBuilder builder = new StringBuilder();
		BindingResult bindingResult = e.getBindingResult();
		for (ObjectError objectError : bindingResult.getAllErrors()) {
			FieldError fieldError = (FieldError) objectError;
			builder.append("\n" + fieldError.getField() + ": " + fieldError.getDefaultMessage());
		}
		String message = builder.toString();
		if (message.length() > 0) {
			message = message.substring(1);
		}
		log.error("请求参数异常：{}", message);
		return ResponseEntity.badRequest().body(message);
	}

	/**
	 * 
	 * 处理系统配置异常
	 *
	 * @param e
	 * @param request
	 * @return ResponseEntity<Object>
	 */
	@ResponseBody
	@ExceptionHandler(SystemConfigException.class)
	public ResponseEntity<Object> dealSystemConfigException(SystemConfigException e,
			HttpServletRequest request) {
		String message = messageSource.getMessage(e.getMessage(), null, request.getLocale());
		log.error("系统配置异常：{}", message);
		return ResponseEntity.internalServerError().body(message);
	}

	/**
	 * 
	 * 处理 Http 请求异常
	 *
	 * @param e
	 * @param request
	 * @return ResponseEntity<Object>
	 */
	@ResponseBody
	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<Object> dealHttpClientErrorException(HttpClientErrorException e,
			HttpServletRequest request) {
		log.error("Http 请求异常：{}", e.getMessage());
		return ResponseEntity.internalServerError().body(e.getMessage());
	}

	/**
	 * 
	 * 处理数据库异常
	 *
	 * @param e
	 * @param request
	 * @return ResponseEntity<Object>
	 */
	@ResponseBody
	@ExceptionHandler(UncategorizedSQLException.class)
	public ResponseEntity<Object> dealUncategorizedSQLException(UncategorizedSQLException e,
			HttpServletRequest request) {
		String message = e.getMessage();
		if (message.contains("SQLITE_BUSY")) {
			message = messageSource.getMessage(Messages.DATABASE_BUSY, null, request.getLocale());
		}
		log.error("数据库异常：{}", message);
		return ResponseEntity.internalServerError().body(message);
	}

	/**
	 * 
	 * 处理其他异常
	 *
	 * @param e
	 * @param request
	 * @return ResponseEntity<Object>
	 */
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> dealException(Exception e, HttpServletRequest request) {
		log.error("其他异常：{}", e.getMessage(), e);
		return ResponseEntity.internalServerError().body(e.getMessage());
	}
}
