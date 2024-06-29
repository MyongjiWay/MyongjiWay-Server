@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.myongjiway.core.auth.security.jwt

import com.myongjiway.core.api.support.error.CoreApiException
import com.myongjiway.core.api.support.error.ErrorType.*
import com.myongjiway.core.auth.security.config.JwtProperty
import io.jsonwebtoken.Jwts.*
import io.jsonwebtoken.Jwts.SIG.*
import io.jsonwebtoken.security.Keys.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JwtProviderTest {

    @Autowired
    private lateinit var jwtProvider: JwtProvider

    @Autowired
    private lateinit var jwtProperty: JwtProperty

    @Test
    @DisplayName("Access Token을 생성한다.")
    fun createAccessToken() {
        // given
        val userId = "1234"

        // when
        val accessToken = jwtProvider.generateAccessTokenByUserId(userId)

        // then
        assertThat(accessToken)
            .extracting("userId").isEqualTo(userId)
    }

    @Test
    @DisplayName("생성한 Access Token의 subject는 userId와 일치하여야 한다.")
    fun subjectOfAccessTokenEqualsUserI() {
        // given
        val userId = "1234"

        // when
        val accessToken = jwtProvider.generateAccessTokenByUserId(userId)

        // then
        assertThat(
            parser().verifyWith(hmacShaKeyFor(jwtProperty.accessToken.secret.toByteArray())).build()
                .parseSignedClaims(accessToken.token).payload.subject,
        ).isEqualTo(userId)
    }

    @Test
    @DisplayName("Refresh Token을 생성한다.")
    fun createRefreshToken() {
        // given
        val userId = "1234"

        // when
        val refreshToken = jwtProvider.generateRefreshTokenByUserId(userId)

        // then
        assertThat(refreshToken).isNotNull()
    }

    @Test
    @DisplayName("토큰의 signature가 secret key로 생성이 되지 않았다면 INVALID_TOKEN_ERROR를 반환한다.")
    fun invalidSignatureOfToken() {
        // given
        val secret = jwtProperty.accessToken.secret + "invalid"
        val invalidToken = builder().subject("1234").signWith(hmacShaKeyFor(secret.toByteArray()), HS512).compact()

        // when // then
        assertThatThrownBy { jwtProvider.getAuthentication(invalidToken) }
            .isInstanceOf(CoreApiException::class.java)
            .hasMessage("토큰이 올바르지 않습니다.")
            .extracting("errorType").isEqualTo(INVALID_TOKEN_ERROR)
    }
}
