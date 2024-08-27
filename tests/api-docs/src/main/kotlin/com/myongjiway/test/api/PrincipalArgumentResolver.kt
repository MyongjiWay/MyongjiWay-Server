package com.myongjiway.test.api

import com.myongjiway.core.domain.user.ProviderType
import com.myongjiway.core.domain.user.Role
import com.myongjiway.core.domain.user.User
import org.springframework.core.MethodParameter
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.time.LocalDateTime

class PrincipalArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean = parameter.hasParameterAnnotation(AuthenticationPrincipal::class.java) &&
        User::class.java.isAssignableFrom(parameter.parameterType)

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any = User(
        id = 1000L,
        profileImg = "test.img",
        name = "test",
        password = "",
        providerId = "1234",
        providerType = ProviderType.KAKAO,
        role = Role.ADMIN,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
    )
}
