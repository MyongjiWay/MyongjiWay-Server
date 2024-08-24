package com.myongjiway.core.domain.token

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Jwts.SIG.HS512
import io.jsonwebtoken.security.Keys
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldNotBe
import java.util.Date

class TokenValidatorTest :
    FeatureSpec(
        {
            lateinit var sut: TokenValidator

            beforeTest {
                sut = TokenValidator()
            }

            feature("Public Key 토큰 유효성 검사") {
                scenario("토큰이 유효하면 Claim을 반환한다.") {
                    // given
                    val secret =
                        "lnp1ISeIafo9E+UHxZ4xrXkaRGDaUuNVCT1tiJ8gXmq4vpseL7JoBC9EjAy0z296NVajinLxbCkopzT+DZJazy3Pg=="
                    val refreshToken = Jwts.builder()
                        .subject(1000L.toString())
                        .expiration(Date(System.currentTimeMillis() + 100000))
                        .signWith(Keys.hmacShaKeyFor(secret.toByteArray()), HS512)
                        .compact()

                    // when
                    val actual = sut.validate(Keys.hmacShaKeyFor(secret.toByteArray()), refreshToken)

                    // then
                    actual shouldNotBe null
                }

                scenario("토큰이 유효하지 않으면 Exception을 반환한다.") {
                    // given
                    val secret =
                        "lnp1ISeIafo9E+UHxZ4xrXkaRGDaUuNVCT1tiJ8gXmq4vpseL7JoBC9EjAy0z296NVajinLxbCkopzT+DZJazy3Pg=="
                    val refreshToken = Jwts.builder()
                        .subject(1000L.toString())
                        .expiration(Date(System.currentTimeMillis() + 100000))
                        .signWith(Keys.hmacShaKeyFor((secret + "invalid").toByteArray()), HS512)
                        .compact()

                    // when
                    val actual = kotlin.runCatching {
                        sut.validate(
                            Keys.hmacShaKeyFor(secret.toByteArray()),
                            refreshToken,
                        )
                    }.exceptionOrNull()

                    // then
                    actual shouldNotBe null
                }
            }
        },
    )
