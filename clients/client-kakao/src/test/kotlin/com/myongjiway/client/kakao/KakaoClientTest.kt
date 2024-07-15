package com.myongjiway.client.kakao

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class KakaoClientTest(
    @Autowired
    private val kakaoClient: KakaoClient,
) {

    @Test
    fun unlink() {
        // given
        val providerId = "123123123"

        // when
        kakaoClient.unlink(providerId)

        // then
//        verify(kakaoApi).unlink("", KakaoUnlinkRequest("user_id", providerId.toLong()))
    }
}
