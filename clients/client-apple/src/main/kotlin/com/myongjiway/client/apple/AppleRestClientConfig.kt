package com.myongjiway.client.apple

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
@ConfigurationProperties(prefix = "apple")
internal class AppleRestClientConfig {
    lateinit var url: String

    @Bean
    fun appleIdentityTokenService(): AppleApi {
        val restClient = RestClient.builder().baseUrl(url).build()
        val adapter = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()

        return factory.createClient(AppleApi::class.java)
    }
}
