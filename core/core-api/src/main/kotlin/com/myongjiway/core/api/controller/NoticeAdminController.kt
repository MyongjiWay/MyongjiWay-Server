package com.myongjiway.core.api.controller

import com.myongjiway.core.api.controller.v1.request.NoticeRequest
import com.myongjiway.core.api.support.response.ApiResponse
import com.myongjiway.core.domain.notice.NoticeService
import com.myongjiway.core.domain.user.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/admin/api/v1/notices")
@RestController
class NoticeAdminController(
    private val noticeService: NoticeService,
) {
    @PostMapping
    fun createNotice(@RequestBody noticeRequest: NoticeRequest, @AuthenticationPrincipal user: User): ApiResponse<Any> {
        noticeService.createNotice(noticeRequest.toMetaData(user.name))
        return ApiResponse.success()
    }

    @PatchMapping("/{noticeId}")
    fun updateNotice(@PathVariable noticeId: Long, @RequestBody noticeRequest: NoticeRequest, @AuthenticationPrincipal user: User): ApiResponse<Any> {
        noticeService.updateNotice(noticeRequest.toMetaData(user.name), noticeId)
        return ApiResponse.success()
    }

    @DeleteMapping("/{noticeId}")
    fun deleteNotice(@PathVariable noticeId: Long): ApiResponse<Any> {
        noticeService.deleteNotice(noticeId)
        return ApiResponse.success()
    }
}
