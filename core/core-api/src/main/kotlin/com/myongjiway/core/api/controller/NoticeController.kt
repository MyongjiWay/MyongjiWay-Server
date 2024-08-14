package com.myongjiway.core.api.controller

import com.myongjiway.core.api.controller.v1.request.NoticeRequest
import com.myongjiway.core.api.controller.v1.response.NoticeResponse
import com.myongjiway.core.api.support.error.CoreApiException
import com.myongjiway.core.api.support.error.ErrorType
import com.myongjiway.core.api.support.response.ApiResponse
import com.myongjiway.core.domain.notice.NoticeService
import com.myongjiway.core.domain.user.Role
import com.myongjiway.core.domain.user.User
import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/notices")
@RestController
class NoticeController(
    private val noticeService: NoticeService,
) {
    @PostMapping
    fun createNotice(@RequestBody noticeRequest: NoticeRequest, @AuthenticationPrincipal user: User): ApiResponse<Any> {
        authenticate(user)
        noticeService.createNotice(noticeRequest.toNotice())
        return ApiResponse.success()
    }

    @PutMapping("/{noticeId}")
    fun updateNotice(@PathVariable noticeId: Long, @RequestBody noticeRequest: NoticeRequest, @AuthenticationPrincipal user: User): ApiResponse<Any> {
        authenticate(user)
        noticeService.updateNotice(noticeRequest.toNotice(), noticeId)
        return ApiResponse.success()
    }

    @DeleteMapping("/{noticeId}")
    fun deleteNotice(@PathVariable noticeId: Long, @AuthenticationPrincipal user: User): ApiResponse<Any> {
        authenticate(user)
        noticeService.deleteNotice(noticeId)
        return ApiResponse.success()
    }

    @GetMapping("/{noticeId}")
    fun getNotice(@PathVariable noticeId: Long, @AuthenticationPrincipal user: User): ApiResponse<NoticeResponse> = ApiResponse.success(NoticeResponse.of(noticeService.getNotice(noticeId, user.id!!)))

    @GetMapping
    fun getAllNotices(@AuthenticationPrincipal user: User): ApiResponse<List<NoticeResponse>> = ApiResponse.success(noticeService.getNotices(user.id!!).map { NoticeResponse.of(it) })

    private fun authenticate(user: User) {
        if (user.role != Role.ADMIN) {
            logger.info("${user.id}는 잘못된 권한으로 접근하였습니다.")
            throw CoreApiException(ErrorType.NOT_ALLOWED_ACCESS_ERROR)
        }
    }

    companion object {
        val logger = LoggerFactory.getLogger("AuthenticationLog")
    }
}
