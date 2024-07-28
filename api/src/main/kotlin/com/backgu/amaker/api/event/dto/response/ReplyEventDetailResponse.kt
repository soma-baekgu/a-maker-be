package com.backgu.amaker.api.event.dto.response

import com.backgu.amaker.api.event.dto.ReplyEventDetailDto
import com.backgu.amaker.api.user.dto.response.UserResponse
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class ReplyEventDetailResponse(
    @Schema(description = "이벤트 id", example = "1")
    val id: Long,
    @Schema(description = "이벤트 제목", example = "우리 어디서 만날지")
    val eventTitle: String,
    @Schema(description = "이벤트 디테일 한 정보", example = "우리 어디서 만날지 정해봅시다")
    val eventDetails: String,
    @Schema(description = "데드라인", example = "2024-07-24T07:39:37.598")
    val deadLine: LocalDateTime,
    @Schema(description = "알림 보낼 시작 시간", example = "2024-07-24T06:09:37.598")
    val notificationStartTime: LocalDateTime,
    @Schema(description = "알림 주기", example = "15")
    val notificationInterval: Int,
    @Schema(description = "이벤트 생성자")
    val eventCreator: UserResponse,
    @Schema(description = "이벤트를 수행한 유저")
    val finishUser: List<UserResponse>,
    @Schema(description = "이벤트 수행 대기중인 유저")
    val waitingUser: List<UserResponse>,
) {
    companion object {
        fun of(replyEventDetailDto: ReplyEventDetailDto) =
            ReplyEventDetailResponse(
                id = replyEventDetailDto.id,
                eventTitle = replyEventDetailDto.eventTitle,
                eventDetails = replyEventDetailDto.eventDetails,
                deadLine = replyEventDetailDto.deadLine,
                notificationStartTime = replyEventDetailDto.notificationStartTime,
                notificationInterval = replyEventDetailDto.notificationInterval,
                eventCreator = UserResponse.of(replyEventDetailDto.eventCreator),
                finishUser = replyEventDetailDto.finishUser.map { UserResponse.of(it) },
                waitingUser = replyEventDetailDto.waitingUser.map { UserResponse.of(it) },
            )
    }
}