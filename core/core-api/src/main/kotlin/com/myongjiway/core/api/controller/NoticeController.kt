package com.myongjiway.core.api.controller

import com.myongjiway.core.api.controller.v1.response.NoticeResponse
import com.myongjiway.core.api.support.response.ApiResponse
import com.myongjiway.core.domain.notice.NoticeService
import com.myongjiway.core.domain.user.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/notices")
@RestController
class NoticeController(
    private val noticeService: NoticeService,
) {
    @GetMapping("/{noticeId}")
    fun getNotice(@PathVariable noticeId: Long, @AuthenticationPrincipal user: User): ApiResponse<NoticeResponse> = ApiResponse.success(NoticeResponse.of(noticeService.getNotice(noticeId, user.id)))

    @GetMapping
    fun getAllNotices(@AuthenticationPrincipal user: User): ApiResponse<List<NoticeResponse>> = ApiResponse.success(noticeService.getNotices(user.id).map { NoticeResponse.of(it) })
}
