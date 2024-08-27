package com.myongjiway.web.controller

import com.myongjiway.core.domain.notice.NoticeMetadata
import com.myongjiway.core.domain.notice.NoticeService
import com.myongjiway.core.domain.notice.NoticeView
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
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

    @GetMapping("/detail/{id}")
    fun noticeDetail(@PathVariable id: Long, model: Model): String {
        val notice = noticeService.getNotice(id)
        model.addAttribute("notice", notice)
        return "notices/detail"
    }

    @GetMapping("/edit/{id}")
    fun editNotice(@PathVariable id: Long, model: Model): String {
        val notice = noticeService.getNotice(id)
        model.addAttribute("notice", notice)
        return "notices/edit"
    }

    @PostMapping("/update")
    fun updateNotice(@ModelAttribute notice: NoticeView): String {
        noticeService.updateNotice(notice.metadata, notice.id)
        return "redirect:/admin/notices/list"
    }

    @GetMapping("/create")
    fun createNoticeForm(model: Model): String {
        model.addAttribute("noticeMetadata", NoticeMetadata(title = "", author = "", content = ""))
        return "notices/create"
    }

    @PostMapping("/save")
    fun saveNotice(@ModelAttribute notice: NoticeMetadata): String {
        noticeService.createNotice(notice)
        return "redirect:/admin/notices/list"
    }

    @PostMapping("/delete/{id}")
    fun deleteNotice(@PathVariable id: Long): String {
        noticeService.deleteNotice(id)
        return "redirect:/admin/notices/list"
    }
}
