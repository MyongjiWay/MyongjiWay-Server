package com.myongjiway.error

enum class CoreErrorType(
    val kind: CoreErrorKind,
    val code: CoreErrorCode,
    val message: String,
    val level: CoreErrorLevel,
) {
    /*
     * 공통 에러
     */
    NOT_FOUND_DATA(CoreErrorKind.SERVER_ERROR, CoreErrorCode.E1000, "해당 데이터를 찾지 못했습니다.", CoreErrorLevel.INFO),
    UNAUTHORIZED(CoreErrorKind.CLIENT_ERROR, CoreErrorCode.E4000, "권한이 없습니다.", CoreErrorLevel.INFO),

    /*
     * 유저 관련 에러
     */
    USER_NOT_FOUND(CoreErrorKind.SERVER_ERROR, CoreErrorCode.USER0001, "존재하지 않는 유저입니다.", CoreErrorLevel.INFO),

    /*
     * 토큰 관련 에러
     */
    UNAUTHORIZED_TOKEN(CoreErrorKind.CLIENT_ERROR, CoreErrorCode.TOKEN0001, "토큰이 유효하지 않습니다. 로그인을 다시 해주세요.", CoreErrorLevel.INFO),
}
