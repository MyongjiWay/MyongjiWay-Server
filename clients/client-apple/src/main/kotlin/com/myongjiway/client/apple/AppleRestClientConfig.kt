package com.myongjiway.client.apple

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "apple")
internal class AppleRestClientConfig {
}