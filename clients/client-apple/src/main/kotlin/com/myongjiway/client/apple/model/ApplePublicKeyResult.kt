package com.myongjiway.client.apple.model

data class ApplePublicKeyResult(
    val keys: List<ApplePublicKey>,
) {
    fun findPublicKey(kid: String, alg: String): ApplePublicKey? = keys.find { it.kid == kid && it.alg == alg }
}
