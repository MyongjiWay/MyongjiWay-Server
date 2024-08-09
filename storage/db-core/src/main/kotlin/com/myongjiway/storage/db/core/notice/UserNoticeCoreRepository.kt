package com.myongjiway.storage.db.core.notice

import com.myongjiway.usernotice.UserNotice
import com.myongjiway.usernotice.UserNoticeRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class UserNoticeCoreRepository(
    private val userNoticeJpaRepository: UserNoticeJpaRepository,
) : UserNoticeRepository {

    @Transactional
    override fun save(noticeId: Long, userId: Long): UserNotice {
        val userNoticeEntity = userNoticeJpaRepository.save(
            UserNoticeEntity(
                noticeId = noticeId,
                userId = userId,
            ),
        )
        return userNoticeEntity.toUserNotice()
    }

    override fun findByUserIdAndNoticeId(userId: Long, noticeId: Long): UserNotice? {
        val userNoticeEntity = userNoticeJpaRepository.findByUserIdAndNoticeId(userId, noticeId) ?: return null
        return userNoticeEntity.toUserNotice()
    }

    override fun findByUserId(userId: Long): List<UserNotice> = userNoticeJpaRepository.findByUserId(userId).map { it.toUserNotice() }

    @Transactional
    override fun deleteByNoticeId(noticeId: Long) {
        userNoticeJpaRepository.deleteAllByNoticeId(noticeId)
    }
}
