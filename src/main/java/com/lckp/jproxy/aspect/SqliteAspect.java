package com.lckp.jproxy.aspect;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * sqlite 读写锁
 * </p>
 *
 * @author LuckyPuppy514
 * @date 2023-04-02
 */
@Slf4j
@Aspect
@Component
public class SqliteAspect {

	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	@Pointcut("execution(* com.baomidou.mybatisplus.extension.service..*.*(..))"
			+ " || execution(* com.lckp.jproxy.mapper..*.*(..))")
	public void pointCut() {
	}

	@Around("pointCut()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		String name = pjp.getSignature().getName();
		log.trace("切入点：{}", pjp.getTarget().getClass().getName());
		if (name.contains("save") || name.contains("update") || name.contains("remove")) {
			log.trace("{} 开始获取写锁", name);
			readWriteLock.writeLock().lock();
			try {
				log.trace("{} 获得了写锁", name);
				return pjp.proceed();
			} catch (Exception e) {
				log.error("{} 写保护出错：{}", name, e.getMessage());
				throw e;
			} finally {
				readWriteLock.writeLock().unlock();
				log.trace("{} 释放了写锁", name);
			}
		} else {
			log.trace("{} 开始获取读锁", name);
			readWriteLock.readLock().lock();
			try {
				log.trace("{} 获得了读锁", name);
				return pjp.proceed();
			} catch (Exception e) {
				log.error("{} 读保护出错：{}", name, e.getMessage());
				throw e;
			} finally {
				readWriteLock.readLock().unlock();
				log.trace("{} 释放了读锁", name);
			}
		}
	}
}
