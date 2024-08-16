package com.myongjiway.storage.db.core.notice

import com.myongjiway.core.domain.error.CoreErrorType
import com.myongjiway.core.domain.error.CoreException
import com.myongjiway.core.domain.notice.NoticeMetadata
import com.myongjiway.core.domain.notice.NoticeRepository
import com.myongjiway.core.domain.notice.NoticeView
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class NoticeCoreRepository(
    private var noticeJpaRepository: NoticeJpaRepository,
) : NoticeRepository {

    @Transactional
    override fun save(noticeMetadata: NoticeMetadata) {
        val noticeEntity = NoticeEntity(
            title = noticeMetadata.title,
            author = noticeMetadata.author,
            content = noticeMetadata.content,
        )
        noticeJpaRepository.save(noticeEntity)
    }

    @Transactional
    override fun update(noticeMetadata: NoticeMetadata, noticeId: Long) {
        val noticeEntity = noticeJpaRepository.findByIdOrNull(noticeId)
            ?: throw CoreException(CoreErrorType.NOTICE_NOT_FOUND)
        noticeEntity.update(noticeMetadata.title, noticeMetadata.author, noticeMetadata.content)
    }

    @Transactional
    override fun delete(noticeId: Long) {
        val noticeEntity = noticeJpaRepository.findByIdOrNull(noticeId)
            ?: throw CoreException(CoreErrorType.NOTICE_NOT_FOUND)
        noticeEntity.inactive()
    }

    override fun findById(noticeId: Long): NoticeView {
        val noticeEntity = noticeJpaRepository.findByIdOrNull(noticeId)
            ?: throw CoreException(CoreErrorType.NOTICE_NOT_FOUND)
        return noticeEntity.toNoticeView()
    }

    override fun findAll(): List<NoticeView> = noticeJpaRepository.findAll().map { it.toNoticeView() }
}
