package com.backgu.amaker.infra.jpa.chat.repository

import com.backgu.amaker.infra.jpa.chat.entity.ChatRoomUserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomUserRepository : JpaRepository<ChatRoomUserEntity, Long> {
    fun existsByUserIdAndChatRoomId(
        userId: String,
        chatRoomId: Long,
    ): Boolean

    fun findByUserId(userId: String): List<ChatRoomUserEntity>

    fun findByUserIdAndChatRoomId(
        userId: String,
        chatRoomId: Long,
    ): ChatRoomUserEntity?

    fun findByChatRoomIdIn(userIds: List<Long>): List<ChatRoomUserEntity>

    fun findByUserIdInAndChatRoomId(
        userIds: List<String>,
        chatRoomId: Long,
    ): List<ChatRoomUserEntity>

    fun findAllByChatRoomId(chatRoomId: Long): List<ChatRoomUserEntity>
}
