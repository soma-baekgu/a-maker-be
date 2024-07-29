package com.backgu.amaker.api.workspace.service

import com.backgu.amaker.api.chat.dto.ChatRoomDto
import com.backgu.amaker.api.chat.service.ChatRoomService
import com.backgu.amaker.api.chat.service.ChatRoomUserService
import com.backgu.amaker.api.common.exception.BusinessException
import com.backgu.amaker.api.common.exception.StatusCode
import com.backgu.amaker.api.user.service.UserService
import com.backgu.amaker.api.workspace.dto.WorkspaceCreateDto
import com.backgu.amaker.api.workspace.dto.WorkspaceDto
import com.backgu.amaker.api.workspace.dto.WorkspaceUserDto
import com.backgu.amaker.api.workspace.dto.WorkspacesDto
import com.backgu.amaker.api.workspace.event.WorkspaceInvitedEvent
import com.backgu.amaker.api.workspace.event.WorkspaceJoinedEvent
import com.backgu.amaker.application.notification.service.NotificationEventService
import com.backgu.amaker.domain.chat.ChatRoom
import com.backgu.amaker.domain.user.User
import com.backgu.amaker.domain.workspace.Workspace
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class WorkspaceFacadeService(
    private val userService: UserService,
    private val workspaceService: WorkspaceService,
    private val workspaceUserService: WorkspaceUserService,
    private val chatRoomService: ChatRoomService,
    private val chatRoomUserService: ChatRoomUserService,
    private val notificationEventService: NotificationEventService,
) {
    @Transactional
    fun createWorkspace(
        userId: String,
        workspaceCreateDto: WorkspaceCreateDto,
    ): WorkspaceDto {
        val leader: User = userService.getById(userId)
        val workspace: Workspace = workspaceService.save(leader.createWorkspace(workspaceCreateDto.name))
        workspaceUserService.save(workspace.assignLeader(leader))
        notificationEventService.publishNotificationEvent(WorkspaceJoinedEvent(leader, workspace))

        val invitees = workspaceCreateDto.inviteesEmails.map { userService.getByEmail(it) }
        invitees.forEach {
            if (!leader.isNonInvitee(it)) throw BusinessException(StatusCode.INVALID_WORKSPACE_CREATE)
            workspaceUserService.save(workspace.inviteWorkspace(it))
            notificationEventService.publishNotificationEvent(WorkspaceInvitedEvent(it, workspace))
        }

        val chatRoom: ChatRoom = chatRoomService.save(workspace.createDefaultChatRoom())
        chatRoomUserService.save(chatRoom.addUser(leader))

        return WorkspaceDto.of(workspace)
    }

    fun findWorkspaces(userId: String): WorkspacesDto {
        val user: User = userService.getById(userId)

        val workspaces: List<Workspace> =
            workspaceService.getWorkspaceByIds(workspaceUserService.findWorkspaceIdsByUser(user))

        return WorkspacesDto(
            userId = user.id,
            workspaces = workspaces.map { WorkspaceDto.of(it) },
        )
    }

    fun getDefaultWorkspace(userId: String): WorkspaceDto {
        val user: User = userService.getById(userId)
        return workspaceService.getDefaultWorkspaceByUserId(user).let { WorkspaceDto.of(it) }
    }

    fun getDefaultChatRoom(
        workspaceId: Long,
        userId: String,
    ): ChatRoomDto {
        val user: User = userService.getById(userId)
        val workspace: Workspace = workspaceService.getWorkspaceById(workspaceId)

        workspaceUserService.validUserInWorkspace(user, workspace)

        return ChatRoomDto
            .of(chatRoomService.getDefaultChatRoomByWorkspace(workspace))
    }

    @Transactional
    fun activateWorkspaceUser(
        userId: String,
        workspaceId: Long,
    ): WorkspaceUserDto {
        val user = userService.getById(userId)
        val workspace = workspaceService.getById(workspaceId)

        val workspaceUser = workspaceUserService.getWorkspaceUser(workspace, user)
        notificationEventService.publishNotificationEvent(WorkspaceJoinedEvent(user, workspace))
        workspaceUserService.save(workspaceUser.activate())

        chatRoomUserService.save(chatRoomService.getDefaultChatRoomByWorkspaceId(workspaceId).addUser(user))

        return WorkspaceUserDto.of(workspaceUser)
    }
}
