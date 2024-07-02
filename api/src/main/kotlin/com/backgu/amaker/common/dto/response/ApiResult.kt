package com.backgu.amaker.common.dto.response

import io.swagger.v3.oas.annotations.media.Schema

open class ApiResult<T>(
    @Schema(description = "KST 응답 타임스탬프", example = "2024-07-02T19:56:05.624+09:00")
    val timestamp: String,
    @Schema(description = "응답 status code", example = "200")
    val status: Int,
    @Schema(description = "요청 URL", example = "/api/v1/test")
    val path: String,
    val data: T,
)