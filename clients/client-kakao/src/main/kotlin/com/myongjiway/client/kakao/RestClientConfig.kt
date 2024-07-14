package com.myongjiway.client.kakao

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class RestClientConfig {

    @Bean
    fun kakaoService(): KakaoApi {
        val restClient = RestClient.builder().baseUrl("http://kauth.kakao.com").build()
        val adapter = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()

        return factory.createClient(KakaoApi::class.java)
    }
}
