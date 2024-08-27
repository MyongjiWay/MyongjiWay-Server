package com.myongjiway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class WebTestApplication

fun main(args: Array<String>) {
    runApplication<WebTestApplication>(*args)
}
