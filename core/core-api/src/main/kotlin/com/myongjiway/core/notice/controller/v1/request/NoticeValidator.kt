package com.myongjiway.core.notice.controller.v1.request

object NoticeValidator {
    const val MAX_TITLE_LENGTH = 100
    const val MAX_CONTENT_LENGTH = 1000

    val sqlInjectionPattern = Regex(
        "(?:--|;|--|\\b(select|update|delete|insert|drop|alter|create|truncate|exec|union|merge|call|execute|explain|declare|grant|revoke|commit|rollback|savepoint|lock|unlock)\\b|['\"`]).*",
        RegexOption.IGNORE_CASE,
    )

    fun validateTitle(title: String?) {
        require(!title.isNullOrBlank()) { "제목은 비어있을 수 없습니다." }
        require(title.length <= MAX_TITLE_LENGTH) { "제목의 길이는 ${MAX_TITLE_LENGTH}자를 초과할 수 없습니다." }
        require(!title.matches(Regex("^\\d+$"))) { "숫자만 포함될 수 없습니다." }
        require(!title.matches(Regex("^[\\s]+$"))) { "제목은 공백일 수 없습니다." }
        require(!title.matches(Regex("^[^\\w\\s]+$"))) { "특수 문자만 포함될 수 없습니다." }
        require(!title.matches(sqlInjectionPattern)) { "SQL Injection이 포함되어 있습니다." }
    }

    fun validateContent(content: String?) {
        require(!content.isNullOrBlank()) { "내용은 비어있을 수 없습니다." }
        require(content.length <= MAX_CONTENT_LENGTH) { "내용의 길이는 ${MAX_CONTENT_LENGTH}자를 초과할 수 없습니다." }
        require(!content.matches(Regex("^\\d+$"))) { "숫자만 포함될 수 없습니다." }
        require(!content.matches(Regex("^[\\s]+$"))) { "내용은 공백일 수 없습니다." }
        require(!content.matches(Regex("^[^\\w\\s]+$"))) { "특수 문자만 포함될 수 없습니다." }
        require(!content.matches(sqlInjectionPattern)) { "SQL Injection이 포함되어 있습니다." }
    }
}
