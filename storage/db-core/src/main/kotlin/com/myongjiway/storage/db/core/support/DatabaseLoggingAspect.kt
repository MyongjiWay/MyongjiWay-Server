package com.myongjiway.storage.db.core.support

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.stereotype.Component

@Aspect
@Component
class DatabaseLoggingAspect {
    @Pointcut("execution(* com.myongjiway.storage.db.core..*CoreRepository.*(..))")
    fun coreRepositoryMethods() {}

    @Before("coreRepositoryMethods()")
    fun logQueryExecution(joinPoint: JoinPoint) {
        MDC.put("class", joinPoint.signature.declaringTypeName)
        MDC.put("queryStartTime", System.nanoTime().toString())
    }

    @AfterReturning(pointcut = "coreRepositoryMethods()")
    fun logExecutionTime(joinPoint: JoinPoint) {
        val startTime = MDC.get("queryStartTime")?.toLong() ?: System.nanoTime()
        val executionTime = (System.nanoTime() - startTime) / 1_000_000_000.0
        MDC.put("executionTime", String.format("%.3f초", executionTime))
        logger.info("query exit")

        // 사용 후 MDC에서 제거
        MDC.remove("query")
        MDC.remove("queryStartTime")
        MDC.remove("executionTime")
    }

    companion object {
        private val logger = LoggerFactory.getLogger("DatabaseLog")
    }
}
