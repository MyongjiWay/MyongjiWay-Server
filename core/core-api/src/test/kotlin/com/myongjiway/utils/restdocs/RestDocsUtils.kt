package com.myongjiway.utils.restdocs

import com.myongjiway.common.security.MockSecurityFilter
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter

fun restDocMockMvcBuilder(
    context: WebApplicationContext,
    provider: RestDocumentationContextProvider,
): MockMvc = MockMvcBuilders
    .webAppContextSetup(context)
    .apply<DefaultMockMvcBuilder>(
        MockMvcRestDocumentation.documentationConfiguration(provider)
            .operationPreprocessors()
            .withRequestDefaults(Preprocessors.prettyPrint())
            .withResponseDefaults(Preprocessors.prettyPrint()),
    )
    .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity(MockSecurityFilter()))
    .addFilters<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
    .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
    .build()
