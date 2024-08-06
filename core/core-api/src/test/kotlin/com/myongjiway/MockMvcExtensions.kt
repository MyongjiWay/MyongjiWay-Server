package com.example.util.com.myongjiway

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.lang.reflect.Type

object MockMvcExtensions {

    val objectMapper: ObjectMapper = jacksonObjectMapper().apply {
        registerModule(KotlinModule.Builder().build())
    }

    inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}

    inline fun <reified T> ParameterizedTypeReference<T>.toJacksonTypeRef(): TypeReference<T> {
        val type: Type = this.type
        return object : TypeReference<T>() {
            override fun getType(): Type = type
        }
    }

    inline fun <reified T> ResultActions.andReturnAs(): T =
        kotlin.runCatching {
            this.run {
                objectMapper.readValue(
                    andReturn().response.getContentAsString(java.nio.charset.StandardCharsets.UTF_8),
                    typeReference<T>().toJacksonTypeRef(),
                )
            }
        }.onSuccess {
            this.andDo(MockMvcResultHandlers.print())
        }.onFailure {
            this.andDo(MockMvcResultHandlers.print())
        }.getOrElse {
            throw IllegalArgumentException("failed response mapping string to object", it)
        }

    fun <BODY : Any> MockMvc.post(
        url: String,
        body: BODY,
        headers: HttpHeaders = HttpHeaders(),
        contentType: MediaType = MediaType.APPLICATION_JSON,
    ): ResultActions =
        MockMvcRequestBuilders.post(url)
            .headers(headers)
            .contentType(contentType)
            .content(objectMapper.writeValueAsString(body))
            .run { perform(this) }

    fun MockMvc.get(
        url: String,
        headers: HttpHeaders = HttpHeaders(),
    ): ResultActions =
        MockMvcRequestBuilders.get(url)
            .headers(headers)
            .run { perform(this) }

    fun ResultActions.expectStatus(status: HttpStatus): ResultActions =
        this.andExpect(MockMvcResultMatchers.status().`is`(status.value()))
}
