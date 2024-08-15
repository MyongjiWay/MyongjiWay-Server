package com.myongjiway.core.api.auth.security.log

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.util.UUID

@Component
class RequestResponseLoggingFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if (request.requestURI.startsWith("/docs/")) {
            filterChain.doFilter(request, response)
            return
        }

        val wrappedRequest = ContentCachingRequestWrapper(request)
        val wrappedResponse = ContentCachingResponseWrapper(response)
        val startTime = System.currentTimeMillis()

        try {
            MDC.put("method", wrappedRequest.method)
            MDC.put("requestUri", wrappedRequest.requestURI)
            MDC.put("sourceIp", wrappedRequest.remoteAddr)
            MDC.put("userAgent", wrappedRequest.getHeader("User-Agent"))
            MDC.put("referer", wrappedRequest.getHeader("Referer") ?: "Direct Access")
            MDC.put("requestId", UUID.randomUUID().toString())

            val authentication = SecurityContextHolder.getContext().authentication
            if (authentication != null && authentication.isAuthenticated) {
                MDC.put("userId", authentication.name)
            }

            // 요청 로그 기록
            logRequest(wrappedRequest)

            // 필터 체인 실행
            filterChain.doFilter(wrappedRequest, wrappedResponse)
        } finally {
            val endTime = System.currentTimeMillis()
            val responseTime = endTime - startTime
            MDC.put("responseTime", responseTime.toString())
            MDC.put("status", wrappedResponse.status.toString())

            logResponseBody()

            wrappedResponse.copyBodyToResponse()
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

    private fun logResponseBody() {
        responseLogger.info("Response Status: {}", MDC.get("status"))
    }

    companion object {
        private val requestLogger = LoggerFactory.getLogger("HttpRequestLog")
        private val responseLogger = LoggerFactory.getLogger("HttpResponseLog")
    }
}
