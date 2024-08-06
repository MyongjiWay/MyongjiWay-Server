package com.myongjiway.test.api

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors

object RestDocsUtils {
    fun requestPreprocessor(): OperationRequestPreprocessor = Preprocessors.preprocessRequest(
        Preprocessors.modifyUris().scheme("https").host("api.myongjiway.store").removePort(),
        Preprocessors.prettyPrint(),
    )

    fun responsePreprocessor(): OperationResponsePreprocessor = Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
}
