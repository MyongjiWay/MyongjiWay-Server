package com.myongjiway.core.notice.controller

import com.myongjiway.core.notice.controller.v1.request.NoticeRequest
import com.myongjiway.core.notice.controller.v1.response.NoticeResponse
import com.myongjiway.notice.NoticeService
import com.myongjiway.user.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/notices")
class NoticeController(
    private val noticeService: NoticeService,
) {
    @PostMapping
    fun createNotice(@RequestBody noticeRequest: NoticeRequest): ResponseEntity<Any> {
        noticeService.createNotice(noticeRequest.toNotice(), User.fixture(1))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @PutMapping("{noticeId}")
    fun updateNotice(@PathVariable noticeId: Long, @RequestBody noticeRequest: NoticeRequest): ResponseEntity<Any> {
        noticeService.updateNotice(noticeId, noticeRequest.toNotice(), User.fixture(1))
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @DeleteMapping("{noticeId}")
    fun deleteNotice(@PathVariable noticeId: Long): ResponseEntity<Any> {
        noticeService.deleteNotice(noticeId, User.fixture(1))
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @GetMapping("{noticeId}")
    fun getNotice(@PathVariable noticeId: Long): NoticeResponse = NoticeResponse.of(noticeService.getNotice(noticeId, User.fixture(1)))

    @GetMapping("/all")
    fun getAllNotices(): List<NoticeResponse> = noticeService.getNotices(User.fixture(1)).map { NoticeResponse.of(it) }
}
