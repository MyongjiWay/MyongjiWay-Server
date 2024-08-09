package com.myongjiway.core.notice.controller

import com.myongjiway.core.api.support.response.ApiResponse
import com.myongjiway.core.notice.controller.v1.request.NoticeRequest
import com.myongjiway.core.notice.controller.v1.response.NoticeResponse
import com.myongjiway.notice.NoticeService
import com.myongjiway.user.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class NoticeController(
    private val noticeService: NoticeService,
) {
    @PostMapping("/domain/notices")
    fun createNotice(@RequestBody noticeRequest: NoticeRequest, @AuthenticationPrincipal user: User): ApiResponse<Any> {
        noticeService.createNotice(noticeRequest.toNotice(), user)
        return ApiResponse.success()
    }

    @PutMapping("/domain/notices/{noticeId}")
    fun updateNotice(@PathVariable noticeId: Long, @RequestBody noticeRequest: NoticeRequest, @AuthenticationPrincipal user: User): ApiResponse<Any> {
        noticeService.updateNotice(noticeId, noticeRequest.toNotice(), user)
        return ApiResponse.success()
    }

    @DeleteMapping("/domain/notices/{noticeId}")
    fun deleteNotice(@PathVariable noticeId: Long, @AuthenticationPrincipal user: User): ApiResponse<Any> {
        noticeService.deleteNotice(noticeId, user)
        return ApiResponse.success()
    }

    @GetMapping("/notices/{noticeId}")
    fun getNotice(@PathVariable noticeId: Long, @AuthenticationPrincipal user: User): ApiResponse<NoticeResponse> = ApiResponse.success(NoticeResponse.of(noticeService.getNotice(noticeId, user)))

    @GetMapping("/notices/all")
    fun getAllNotices(@AuthenticationPrincipal user: User): ApiResponse<List<NoticeResponse>> = ApiResponse.success(noticeService.getNotices(user).map { NoticeResponse.of(it) })
}
