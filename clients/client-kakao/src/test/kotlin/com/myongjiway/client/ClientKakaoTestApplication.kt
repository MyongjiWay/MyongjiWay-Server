package com.myongjiway.client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class ClientKakaoTestApplication

fun main(args: Array<String>) {
    runApplication<ClientKakaoTestApplication>(*args)
}
