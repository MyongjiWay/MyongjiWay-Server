package com.myongjiway.core.api.support.logging

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Component

@Aspect
@Component
class RequestLoggingAspect {
    // core-api 모듈의 모든 컨트롤러 메서드에 적용
    @Pointcut("execution(* com.myongjiway.core.api..controller..*(..))")
    fun coreApiControllerMethods() {}

    // 요청 전 로깅
    @Before("coreApiControllerMethods()")
    fun logRequest(joinPoint: JoinPoint) {
        requestLogger.info("Request received for method: ${joinPoint.signature.name}")
    }

    @AfterReturning(pointcut = "coreApiControllerMethods()")
    fun logResponse() {
        val startTime = MDC.get("startTime")?.toLong() ?: 0L
        val executionTime = (System.nanoTime() - startTime) / 1_000_000_000.0
        MDC.put("responseTime", String.format("%.3f초", executionTime))
        responseLogger.info("Response sent successfully")
    }

    @After("coreApiControllerMethods()")
    fun clearMDC() {
        MDC.clear()
    }

    companion object {
        private val requestLogger = LoggerFactory.getLogger("HttpRequestLog")
        private val responseLogger = LoggerFactory.getLogger("HttpResponseLog")
    }
}
