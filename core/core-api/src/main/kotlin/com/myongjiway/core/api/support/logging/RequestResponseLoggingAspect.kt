package com.myongjiway.core.api.support.logging

import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.UUID

@Aspect
@Component
class RequestResponseLoggingAspect {
    // core-api 모듈의 모든 컨트롤러 메서드에 적용
    @Pointcut("execution(* com.myongjiway.core.api..controller..*(..))")
    fun coreApiControllerMethods() {}

    // 요청 전 로깅
    @Before("coreApiControllerMethods()")
    fun logRequest(joinPoint: JoinPoint) {
        setMDC()
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

    private fun setMDC() {
        val request = getCurrentHttpRequest()
        MDC.put("method", request.method)
        MDC.put("requestUri", request.requestURI)
        MDC.put("sourceIp", request.getHeader("X-Real-IP") ?: request.remoteAddr)
        MDC.put("userAgent", request.getHeader("User-Agent"))
        MDC.put("xForwardedFor", request.getHeader("X-Forwarded-For"))
        MDC.put("xForwardedProto", request.getHeader("X-Forwarded-Proto"))
        MDC.put("requestId", UUID.randomUUID().toString())
        MDC.put("startTime", System.nanoTime().toString())
    }

    private fun getCurrentHttpRequest(): HttpServletRequest =
        (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request

    companion object {
        private val requestLogger = LoggerFactory.getLogger("HttpRequestLog")
        private val responseLogger = LoggerFactory.getLogger("HttpResponseLog")
    }
}
