package com.myongjiway.core.domain

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class CoreDomainTestApplication {

    fun main(args: Array<String>) {
        runApplication<CoreDomainTestApplication>(*args)
    }
}
