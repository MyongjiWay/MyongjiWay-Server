package com.myongjiway.web.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/admin")
@Controller
class HomeController {

    @GetMapping("/home")
    fun home(): String = "home"
}
