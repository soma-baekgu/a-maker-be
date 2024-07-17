package com.backgu.amaker.chat.repository

import com.backgu.amaker.chat.dto.ChatWithUserDto
import com.backgu.amaker.chat.dto.QChatWithUserDto
import com.backgu.amaker.chat.jpa.QChatEntity.chatEntity
import com.backgu.amaker.user.jpa.QUserEntity.userEntity
import com.querydsl.jpa.impl.JPAQueryFactory

class ChatQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : ChatQueryRepository {
    override fun findTopByChatRoomIdLittleThanCursorLimitCountWithUser(
        chatRoomId: Long,
        cursor: Long,
        size: Int,
    ): List<ChatWithUserDto> =
        queryFactory
            .select(
                QChatWithUserDto(
                    chatEntity.id,
                    chatEntity.chatRoomId,
                    chatEntity.content,
                    chatEntity.chatType,
                    chatEntity.createdAt,
                    chatEntity.updatedAt,
                    userEntity.id,
                    userEntity.name,
                    userEntity.email,
                    userEntity.picture,
                ),
            ).from(chatEntity)
            .innerJoin(userEntity)
            .on(chatEntity.userId.eq(userEntity.id))
            .where(chatEntity.chatRoomId.eq(chatRoomId).and(chatEntity.id.lt(cursor)))
            .orderBy(chatEntity.id.desc())
            .limit(size.toLong())
            .fetch()

    override fun findTopByChatRoomIdGreaterThanCursorLimitCountWithUser(
        chatRoomId: Long,
        cursor: Long,
        size: Int,
    ): List<ChatWithUserDto> =
        queryFactory
            .select(
                QChatWithUserDto(
                    chatEntity.id,
                    chatEntity.chatRoomId,
                    chatEntity.content,
                    chatEntity.chatType,
                    chatEntity.createdAt,
                    chatEntity.updatedAt,
                    userEntity.id,
                    userEntity.name,
                    userEntity.email,
                    userEntity.picture,
                ),
            ).from(chatEntity)
            .innerJoin(userEntity)
            .on(chatEntity.userId.eq(userEntity.id))
            .where(chatEntity.chatRoomId.eq(chatRoomId).and(chatEntity.id.gt(cursor)))
            .orderBy(chatEntity.id.asc())
            .limit(size.toLong())
            .fetch()

    override fun findByIdWithUser(chatId: Long): ChatWithUserDto? =
        queryFactory
            .select(
                QChatWithUserDto(
                    chatEntity.id,
                    chatEntity.chatRoomId,
                    chatEntity.content,
                    chatEntity.chatType,
                    chatEntity.createdAt,
                    chatEntity.updatedAt,
                    userEntity.id,
                    userEntity.name,
                    userEntity.email,
                    userEntity.picture,
                ),
            ).from(chatEntity)
            .innerJoin(userEntity)
            .on(chatEntity.userId.eq(userEntity.id))
            .where(chatEntity.id.eq(chatId))
            .fetchOne()
}
