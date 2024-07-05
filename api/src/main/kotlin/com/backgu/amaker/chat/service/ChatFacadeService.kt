package com.backgu.amaker.chat.service

import com.backgu.amaker.chat.domain.ChatRoom
import com.backgu.amaker.chat.dto.ChatDto
import com.backgu.amaker.chat.dto.GeneralChatCreateDto
import com.backgu.amaker.user.domain.User
import com.backgu.amaker.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ChatFacadeService(
    private val chatRoomService: ChatRoomService,
    private val chatRoomUserService: ChatRoomUserService,
    private val chatService: ChatService,
    private val userService: UserService,
) {
    @Transactional
    fun createGeneralChat(
        generalChatCreateDto: GeneralChatCreateDto,
        chatRoomId: Long,
    ): ChatDto {
        val user: User = userService.getById(generalChatCreateDto.userId)
        val chatRoom: ChatRoom = chatRoomService.getById(chatRoomId)

        chatRoomUserService.validateUserInChatRoom(user, chatRoom)

        return ChatDto.of(
            chatService.save(
                chatRoom.createGeneralChat(
                    user = user,
                    chatRoom = chatRoom,
                    content = generalChatCreateDto.content,
                ),
            ),
        )
    }
}