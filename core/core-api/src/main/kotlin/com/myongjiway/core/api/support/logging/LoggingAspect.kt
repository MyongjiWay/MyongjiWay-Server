package com.myongjiway.core.api.support.logging

import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.util.ContentCachingRequestWrapper
import java.util.UUID

@Aspect
@Component
class LoggingAspect {
    @Around("execution(* com.myongjiway.core.api..controller..*(..))")
    fun logAndHandleRequest(joinPoint: ProceedingJoinPoint): Any? {
        val request = getCurrentHttpRequest()
        val wrappedRequest = ContentCachingRequestWrapper(request)
        val startTime = System.currentTimeMillis()

        try {
            // 요청 전 로깅 및 MDC 설정
            MDC.put("method", wrappedRequest.method)
            MDC.put("requestUri", wrappedRequest.requestURI)
            MDC.put("sourceIp", wrappedRequest.remoteAddr)
            MDC.put("userAgent", wrappedRequest.getHeader("User-Agent"))
            MDC.put("referer", wrappedRequest.getHeader("Referer") ?: "Direct Access")
            MDC.put("requestId", UUID.randomUUID().toString())

            val authentication = SecurityContextHolder.getContext().authentication
            if (authentication != null && authentication.isAuthenticated) {
                val userString = authentication.name.toString()
                val userId = parseUserIdFromPrincipal(userString)
                MDC.put("userId", userId)
            }

            // 요청 정보 로깅
            logRequest(wrappedRequest)

            // 실제 메서드 실행
            val result = joinPoint.proceed()

            // 성공적인 실행 후 로깅
            logger.info("Method ${joinPoint.signature.name} executed successfully.")
            return result
        } catch (e: Throwable) {
            // 예외 처리 및 로깅
            logExceptionDetails(joinPoint, e)
            throw e
        } finally {
            // 응답 후 MDC 정리
            val endTime = System.currentTimeMillis()
            val responseTime = endTime - startTime
            MDC.put("responseTime", responseTime.toString())
            MDC.clear()
        }
    }

    private fun logRequest(request: ContentCachingRequestWrapper) {
        val content = request.contentAsByteArray
        if (content.isNotEmpty()) {
            val requestBody = String(content, Charsets.UTF_8)
            requestLogger.info("Request Body: {}", requestBody)
        } else {
            requestLogger.info("No Request Body")
        }
    }

    private fun logExceptionDetails(joinPoint: ProceedingJoinPoint, e: Throwable) {
        MDC.put("exception", e.message)
        MDC.put("exceptionClass", e.javaClass.name)
        MDC.put("method", joinPoint.signature.name)
        MDC.put("class", joinPoint.signature.declaringTypeName)

        // 스택 트레이스에서 발생한 위치 찾기
        val stackTraceElement = e.stackTrace.firstOrNull { it.className == joinPoint.signature.declaringTypeName }
        if (stackTraceElement != null) {
            MDC.put("line", stackTraceElement.lineNumber.toString())
        }

        logger.error("Exception in method: ${joinPoint.signature.name} at ${stackTraceElement?.fileName}:${stackTraceElement?.lineNumber}", e)
    }

    private fun getCurrentHttpRequest(): HttpServletRequest = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request

    private fun parseUserIdFromPrincipal(userString: String): String {
        // User(id=1, ...) 형식에서 id= 뒤의 숫자를 파싱
        val idPattern = """id=(\d+)""".toRegex()
        val matchResult = idPattern.find(userString)
        return matchResult?.groupValues?.get(1) ?: "unknown"
    }

    companion object {
        private val logger = LoggerFactory.getLogger(LoggingAspect::class.java)
        private val requestLogger = LoggerFactory.getLogger("HttpRequestLog")
    }
}
