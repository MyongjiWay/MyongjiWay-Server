package com.myongjiway.storage.db.core.config

import org.hibernate.cfg.AvailableSettings
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HibernateConfig(
    private val queryLoggingInspector: QueryLoggingInspector,
) {
    @Bean
    fun hibernatePropertyConfig(): HibernatePropertiesCustomizer = HibernatePropertiesCustomizer { hibernateProperties ->
        hibernateProperties[AvailableSettings.STATEMENT_INSPECTOR] = queryLoggingInspector
    }
}
