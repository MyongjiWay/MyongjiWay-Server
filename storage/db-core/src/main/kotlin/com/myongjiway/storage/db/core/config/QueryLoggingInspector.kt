package com.myongjiway.storage.db.core.config

import org.hibernate.resource.jdbc.spi.StatementInspector
import org.slf4j.MDC
import org.springframework.stereotype.Component

@Component
class QueryLoggingInspector : StatementInspector {

    override fun inspect(sql: String): String {
        val currentQueries = MDC.get("query") ?: ""
        MDC.put("query", "$currentQueries$sql;")
        return sql
    }
}
