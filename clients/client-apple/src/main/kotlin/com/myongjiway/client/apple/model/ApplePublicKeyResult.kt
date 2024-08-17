package com.myongjiway.client.apple.model

data class ApplePublicKeyResult(
    val keys: List<ApplePublicKey>,
) {
    data class ApplePublicKey(
        val kty: String,
        val kid: String,
        val use: String,
        val alg: String,
        val n: String,
        val e: String,
    )

    fun findPublicKey(kid: String, alg: String): ApplePublicKey? = keys.find { it.kid == kid && it.alg == alg }
}
