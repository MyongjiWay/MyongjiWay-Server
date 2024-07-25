package com.myongjiway.storage.db.core.notice

import com.myongjiway.domain.notice.NoticeRepository

class NoticeCoreRepository(
    private var noticeJpaRepository: NoticeJpaRepository,
) : NoticeRepository
