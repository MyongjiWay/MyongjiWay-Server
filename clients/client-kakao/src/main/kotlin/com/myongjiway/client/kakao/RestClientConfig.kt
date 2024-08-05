package com.myongjiway.client.kakao

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
@ConfigurationProperties(prefix = "kakao")
internal class RestClientConfig {
    lateinit var url: String
    lateinit var adminKey: String

    @Bean
    fun kakaoService(): KakaoApi {
        val restClient = RestClient.builder().baseUrl(url).build()
        val adapter = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()

        return factory.createClient(KakaoApi::class.java)
    }
}
