package com.backgu.amaker.api.event.controller

import com.backgu.amaker.api.event.dto.request.ReactionEventCreateRequest
import com.backgu.amaker.api.event.dto.request.ReplyEventCreateRequest
import com.backgu.amaker.api.event.dto.request.TaskEventCreateRequest
import com.backgu.amaker.api.event.dto.response.ReactionEventDetailResponse
import com.backgu.amaker.api.event.dto.response.ReplyEventDetailResponse
import com.backgu.amaker.api.event.dto.response.TaskEventDetailResponse
import com.backgu.amaker.api.event.service.EventFacadeService
import com.backgu.amaker.common.http.ApiHandler
import com.backgu.amaker.common.http.response.ApiResult
import com.backgu.amaker.common.security.jwt.authentication.JwtAuthentication
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1/chat-rooms/{chat-room-id}")
class EventController(
    private val eventFacadeService: EventFacadeService,
    private val apiHandler: ApiHandler,
) : EventSwagger {
    @GetMapping("/events/{event-id}/reply")
    override fun getReplyEvent(
        @AuthenticationPrincipal token: JwtAuthentication,
        @PathVariable("chat-room-id") chatRoomId: Long,
        @PathVariable("event-id") eventId: Long,
    ): ResponseEntity<ApiResult<ReplyEventDetailResponse>> =
        ResponseEntity
            .ok()
            .body(
                apiHandler.onSuccess(
                    ReplyEventDetailResponse.of(
                        eventFacadeService.getReplyEvent(
                            token.id,
                            chatRoomId,
                            eventId,
                        ),
                    ),
                ),
            )

    @GetMapping("/events/{event-id}/reaction")
    override fun getReactionEvent(
        @AuthenticationPrincipal token: JwtAuthentication,
        @PathVariable("chat-room-id") chatRoomId: Long,
        @PathVariable("event-id") eventId: Long,
    ): ResponseEntity<ApiResult<ReactionEventDetailResponse>> =
        ResponseEntity
            .ok()
            .body(
                apiHandler.onSuccess(
                    ReactionEventDetailResponse.of(
                        eventFacadeService.getReactionEvent(
                            token.id,
                            chatRoomId,
                            eventId,
                        ),
                    ),
                ),
            )

    @GetMapping("/events/{event-id}/task")
    override fun geTaskEvent(
        @AuthenticationPrincipal token: JwtAuthentication,
        @PathVariable("chat-room-id") chatRoomId: Long,
        @PathVariable("event-id") eventId: Long,
    ): ResponseEntity<ApiResult<TaskEventDetailResponse>> =
        ResponseEntity
            .ok()
            .body(
                apiHandler.onSuccess(
                    TaskEventDetailResponse.of(
                        eventFacadeService.getTaskEvent(
                            token.id,
                            chatRoomId,
                            eventId,
                        ),
                    ),
                ),
            )

    @PostMapping("/events/reply")
    override fun createReplyEvent(
        @AuthenticationPrincipal token: JwtAuthentication,
        @PathVariable("chat-room-id") chatRoomId: Long,
        @RequestBody @Valid request: ReplyEventCreateRequest,
    ): ResponseEntity<Unit> =
        ResponseEntity
            .created(
                ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/v1/events/{id}/reply")
                    .buildAndExpand(
                        eventFacadeService
                            .createReplyEvent(
                                token.id,
                                chatRoomId,
                                request.toDto(),
                            ).id,
                    ).toUri(),
            ).build()

    @PostMapping("/events/reaction")
    override fun createReactionEvent(
        @AuthenticationPrincipal token: JwtAuthentication,
        @PathVariable("chat-room-id") chatRoomId: Long,
        @RequestBody @Valid request: ReactionEventCreateRequest,
    ): ResponseEntity<Unit> =
        ResponseEntity
            .created(
                ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/v1/events/{id}/reply")
                    .buildAndExpand(
                        eventFacadeService
                            .createReactionEvent(
                                token.id,
                                chatRoomId,
                                request.toDto(),
                            ).id,
                    ).toUri(),
            ).build()

    @PostMapping("/events/task")
    override fun createTaskEvent(
        @AuthenticationPrincipal token: JwtAuthentication,
        @PathVariable("chat-room-id") chatRoomId: Long,
        @RequestBody @Valid request: TaskEventCreateRequest,
    ): ResponseEntity<Unit> =
        ResponseEntity
            .created(
                ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/v1/events/{id}/task")
                    .buildAndExpand(
                        eventFacadeService
                            .createTaskEvent(
                                token.id,
                                chatRoomId,
                                request.toDto(),
                            ).id,
                    ).toUri(),
            ).build()
}
