package com.myongjiway.storage.db.core.notice

import com.myongjiway.error.CoreErrorType
import com.myongjiway.error.CoreException
import com.myongjiway.notice.Notice
import com.myongjiway.notice.NoticeRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class NoticeCoreRepository(
    private var noticeJpaRepository: NoticeJpaRepository,
) : NoticeRepository {

    @Transactional
    override fun save(notice: Notice) {
        val noticeEntity = NoticeEntity(
            title = notice.title,
            author = notice.author,
            content = notice.content,
        )
        noticeJpaRepository.save(noticeEntity)
    }

    @Transactional
    override fun update(notice: Notice) {
        val noticeEntity = noticeJpaRepository.findById(notice.id!!).orElseThrow { throw CoreException(CoreErrorType.NOT_FOUND_DATA) }
        noticeEntity.update(notice.title, notice.author, notice.content)
    }

    @Transactional
    override fun delete(noticeId: Long) {
        val noticeEntity = noticeJpaRepository.findById(noticeId).orElseThrow { throw CoreException(CoreErrorType.NOT_FOUND_DATA) }
        noticeJpaRepository.delete(noticeEntity)
    }

    override fun findById(noticeId: Long): Notice {
        val noticeEntity = noticeJpaRepository.findById(noticeId).orElseThrow { throw CoreException(CoreErrorType.NOT_FOUND_DATA) }
        return noticeEntity.toNotice()
    }

    override fun findAll(): List<Notice> = noticeJpaRepository.findAll().map { it.toNotice() }
}
