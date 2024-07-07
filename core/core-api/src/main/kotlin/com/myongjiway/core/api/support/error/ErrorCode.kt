package com.myongjiway.core.api.support.error

enum class ErrorCode(private val description: String) {
    E500("해당 요청에 대한 권한이 없습니다."),

    AUTH002("해당 요청에 대한 권한이 없습니다."),
    AUTH003("로그인 후 이용가능합니다. 토큰을 입력해 주세요."),
    AUTH004("기존 토큰이 만료되었습니다. 토큰을 재발급해주세요."),
    AUTH005("모든 토큰이 만료되었습니다. 다시 로그인해주세요."),
    AUTH006("토큰이 올바르지 않습니다."),
    AUTH007("탈취된(로그아웃 된) 토큰입니다 다시 로그인 해주세요."),
    AUTH009("리프레쉬 토큰이 유효하지 않습니다. 다시 로그인 해주세요."),
    AUTH010("토큰이 비어있습니다. 토큰을 보내주세요."),
    AUTH011("해당 토큰을 가진 유저가 존재하지 않습니다."),

    U009("해당 유저가 존재하지 않습니다."),
    U010("접근 권한이 없습니다."),
    ;

    fun getDescription(): String = description
}
