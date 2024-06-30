package com.myongjiway.token

import com.myongjiway.core.auth.security.config.JwtProperty
import com.myongjiway.core.auth.security.jwt.JwtProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.Date

@SpringBootTest
class TokenTypeTest {

    @Autowired
    private lateinit var jwtProperty: JwtProperty

    @Autowired
    private lateinit var jwtProvider: JwtProvider

    @Test
    @DisplayName("토큰 타입이 ACCESS인 경우, AccessToken을 생성한다.")
    fun generateAccessTokenByTokenType() {
        // given
        val userId = "1234"
        val tokenType = TokenType.ACCESS
        val now = System.currentTimeMillis()

        // when
        val token = tokenType.generate(
            Date(now + jwtProperty.accessToken.expiration),
            jwtProvider.generateAccessTokenByUserId(userId).token,
            userId,
        )

        // then
        assertThat(token)
            .isInstanceOf(AccessToken::class.java)
            .extracting("userId", "expiration").contains(userId, now + jwtProperty.accessToken.expiration)
    }

    @Test
    @DisplayName("토큰 타입이 REFRESH인 경우, RefreshToken을 생성한다.")
    fun generateRefreshTokenByTokenType() {
        // given
        val userId = "1234"
        val tokenType = TokenType.REFRESH
        val now = System.currentTimeMillis()

        // when
        val token = tokenType.generate(
            Date(now + jwtProperty.refreshToken.expiration),
            jwtProvider.generateRefreshTokenByUserId(userId).token,
            userId,
        )

        // then
        assertThat(token)
            .isInstanceOf(RefreshToken::class.java)
            .extracting("userId", "expiration").contains(userId, now + jwtProperty.refreshToken.expiration)
    }
}
