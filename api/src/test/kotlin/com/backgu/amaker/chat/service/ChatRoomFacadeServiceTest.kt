package com.backgu.amaker.chat.service

import com.backgu.amaker.chat.domain.Chat
import com.backgu.amaker.chat.domain.ChatRoom
import com.backgu.amaker.chat.domain.ChatRoomType
import com.backgu.amaker.chat.dto.BriefChatRoomViewDto
import com.backgu.amaker.chat.dto.ChatRoomCreateDto
import com.backgu.amaker.chat.dto.ChatRoomsViewDto
import com.backgu.amaker.fixture.ChatRoomFacadeFixture
import com.backgu.amaker.workspace.domain.Workspace
import com.backgu.amaker.workspace.domain.WorkspaceUserStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@DisplayName("ChatRoomFacadeService 테스트")
@Transactional
@SpringBootTest
class ChatRoomFacadeServiceTest {
    @Autowired
    lateinit var chatRoomFacadeService: ChatRoomFacadeService

    companion object {
        lateinit var fixtures: ChatRoomFacadeFixture
        private const val TEST_USER_ID: String = "test-user"
        lateinit var chatRoom: ChatRoom
        lateinit var workspace: Workspace

        @JvmStatic
        @BeforeAll
        fun setUp(
            @Autowired fixtures: ChatRoomFacadeFixture,
        ) {
            val chatRoomFixtureDto = fixtures.setUp(TEST_USER_ID)
            chatRoom = chatRoomFixtureDto.chatRoom
            workspace = chatRoomFixtureDto.workspace
            this.fixtures = fixtures
        }
    }

    @Test
    @DisplayName("채팅방 생성 테스트")
    fun createChatRoom() {
        // given
        val leaderId = "tester"
        val workspace = fixtures.setUp(userId = leaderId).workspace
        val activeWorkspaceMember = fixtures.userFixture.createPersistedUsers(10)
        fixtures.workspaceUserFixture.createPersistedWorkspaceMember(
            workspaceId = workspace.id,
            memberIds = activeWorkspaceMember.map { it.id },
        )
        val inactiveWorkspaceMember = fixtures.userFixture.createPersistedUsers(10)
        fixtures.workspaceUserFixture.createPersistedWorkspaceMember(
            workspaceId = workspace.id,
            memberIds = inactiveWorkspaceMember.map { it.id },
            WorkspaceUserStatus.PENDING,
        )

        // when
        val chatRoom = chatRoomFacadeService.createChatRoom(leaderId, workspace.id, ChatRoomCreateDto("test"))

        // then
        assertThat(chatRoom).isNotNull()
        assertThat(chatRoom.workspaceId).isEqualTo(workspace.id)
    }

    @Test
    @DisplayName("유저가 속한 채팅방 조회 성공")
    fun findChatRoomsJoined() {
        // given
        val lastChat: Chat = fixtures.chatFixture.createPersistedChat(chatRoom.id, TEST_USER_ID, "content")
        fixtures.chatRoomFixture.save(chatRoom.updateLastChatId(lastChat))

        // when
        val result: ChatRoomsViewDto = chatRoomFacadeService.findChatRoomsJoined(TEST_USER_ID, workspace.id)

        // then
        assertThat(result.chatRooms).isNotNull
        assertThat(result.chatRooms.size).isOne()
        assertThat(result.chatRooms[0].unreadChatCount).isEqualTo(1)
        assertThat(result.chatRooms[0].lastChat?.content).isEqualTo("content")
        assertThat(result.chatRooms[0].participants.size).isEqualTo(11)
    }

    @Test
    @DisplayName("워크스페이스에 속한 채팅방 조회 성공")
    fun findChatRooms() {
        // given
        fixtures.setUp(userId = "other1")
        fixtures.setUp(userId = "other2")
        fixtures.setUp(userId = "other3")

        val leaderId = "leader"
        val leader = fixtures.userFixture.createPersistedUser(leaderId)
        val member = fixtures.userFixture.createPersistedUsers(10)
        val workspace = fixtures.workspaceFixture.createPersistedWorkspace(name = "test-workspace")
        fixtures.workspaceUserFixture.createPersistedWorkspaceUser(workspace.id, leader.id, member.map { it.id })

        val defaultChatRoom =
            fixtures.chatRoomFixture.createPersistedChatRoom(workspace.id, ChatRoomType.DEFAULT)
        fixtures.chatRoomUserFixture.createPersistedChatRoomUser(
            defaultChatRoom.id,
            member.map { it.id }.plus(leader.id),
        )

        val leaderNotRegistered1 =
            fixtures.chatRoomFixture.createPersistedChatRoom(workspace.id, ChatRoomType.CUSTOM)
        fixtures.chatRoomUserFixture.createPersistedChatRoomUser(leaderNotRegistered1.id, member.map { it.id })
        val leaderNotRegistered2 =
            fixtures.chatRoomFixture.createPersistedChatRoom(workspace.id, ChatRoomType.CUSTOM)
        fixtures.chatRoomUserFixture.createPersistedChatRoomUser(leaderNotRegistered2.id, member.map { it.id })
        val leaderNotRegistered3 =
            fixtures.chatRoomFixture.createPersistedChatRoom(workspace.id, ChatRoomType.CUSTOM)
        fixtures.chatRoomUserFixture.createPersistedChatRoomUser(leaderNotRegistered3.id, member.map { it.id })

        val leadRegistered1 =
            fixtures.chatRoomFixture.createPersistedChatRoom(workspace.id, ChatRoomType.CUSTOM)
        fixtures.chatRoomUserFixture.createPersistedChatRoomUser(
            leadRegistered1.id,
            member.map { it.id }.plus(leader.id),
        )
        val leadRegistered2 =
            fixtures.chatRoomFixture.createPersistedChatRoom(workspace.id, ChatRoomType.CUSTOM)
        fixtures.chatRoomUserFixture.createPersistedChatRoomUser(
            leadRegistered2.id,
            member.map { it.id }.plus(leader.id),
        )

        // when
        val result: BriefChatRoomViewDto = chatRoomFacadeService.findChatRooms(leaderId, workspace.id)

        // then
        assertThat(result.chatRooms).isNotNull
        assertThat(result.chatRooms.size).isEqualTo(6)

        assertThat(result.chatRooms[0].participants.size).isEqualTo(11)
    }

    @Test
    @DisplayName("워크스페이스에 채팅방이 없을 때 채팅방 조회 성공")
    fun findChatRoomsNoChatRoom() {
        // given
        fixtures.setUp(userId = "other1")
        fixtures.setUp(userId = "other2")
        fixtures.setUp(userId = "other3")

        val leaderId = "leader"
        val leader = fixtures.userFixture.createPersistedUser(leaderId)
        val member = fixtures.userFixture.createPersistedUsers(10)
        val workspace = fixtures.workspaceFixture.createPersistedWorkspace(name = "test-workspace")
        fixtures.workspaceUserFixture.createPersistedWorkspaceUser(workspace.id, leader.id, member.map { it.id })

        // when
        val result: BriefChatRoomViewDto = chatRoomFacadeService.findChatRooms(leaderId, workspace.id)

        // then
        assertThat(result.chatRooms).isNotNull
        assertThat(result.chatRooms.size).isEqualTo(0)
    }
}
