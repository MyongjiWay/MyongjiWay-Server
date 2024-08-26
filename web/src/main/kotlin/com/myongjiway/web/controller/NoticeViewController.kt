package com.myongjiway.web.controller

import com.myongjiway.core.domain.notice.NoticeService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/admin/notices")
@Controller
class NoticeViewController(
    private val noticeService: NoticeService,
) {
    @GetMapping("/list")
    fun notices(model: Model): String {
        val noticeList = noticeService.getNotices()
        model.addAttribute("notices", noticeList)
        return "notices/list"
    }
}
