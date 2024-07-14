package com.myongjiway.client.kakao

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "kakao")
class KakaoProperty {
    lateinit var adminKey: String
}
