package com.myongjiway.notice

class NoticeServiceExtremeTest :
    FeatureSpec({
        lateinit var noticeRepository: NoticeRepository
        lateinit var noticeService: NoticeService

        beforeTest {
            noticeRepository = mockk()
            noticeService = NoticeService(noticeRepository)
        }

        feature("공지사항 생성 - 극한값 테스트") {
            scenario("제목이 매우 긴 경우 (500자 이상)") {
                val title = "a".repeat(500)
                val content = "Valid content"

                val exception = shouldThrow<IllegalArgumentException> {
                    noticeService.createNotice(title, content)
                }

                exception.message shouldBe "Title too long"
            }

            scenario("내용이 매우 긴 경우 (5000자 이상)") {
                val title = "Valid title"
                val content = "a".repeat(5000)

                val exception = shouldThrow<IllegalArgumentException> {
                    noticeService.createNotice(title, content)
                }

                exception.message shouldBe "Content too long"
            }

            scenario("공지사항이 매우 많을 때 (1000개 이상)") {
                val notices = (1..1000).map { Notice(id = it.toLong(), title = "Notice $it", content = "Content $it") }
                every { noticeRepository.findAll() } returns notices

                val noticeList = noticeService.getAllNotices()

                noticeList.size shouldBe 1000
                noticeList[0].title shouldBe "Notice 1"
                noticeList[999].title shouldBe "Notice 1000"
                verify { noticeRepository.findAll() }
            }
        }
    })
