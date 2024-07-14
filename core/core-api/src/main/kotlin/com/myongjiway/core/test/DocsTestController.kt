package com.myongjiway.core.test

import com.myongjiway.core.test.v1.ExampleRequest
import com.myongjiway.core.test.v1.ExampleResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController



@RestController
class DocsTestController {

    @GetMapping("/example")
    fun getExample(): ExampleResponse {
        return ExampleResponse("Get Test")
    }

    @PostMapping("/example")
    fun postExample(@RequestBody request: ExampleRequest): ExampleResponse {
        return ExampleResponse("Post Test")
    }
}