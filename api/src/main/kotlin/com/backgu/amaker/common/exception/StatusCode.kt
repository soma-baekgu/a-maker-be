package com.backgu.amaker.common.exception

enum class StatusCode(
    val code: String,
    val message: String,
) {
    // OK
    SUCCESS("2000", "성공"),

    // INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR("5000", "죄송합니다. 다음에 다시 시도해주세요."),

    // general
    INVALID_INPUT_VALUE("4000", "잘못된 입력입니다."),

    // user
    USER_NOT_FOUND("4000", "사용자를 찾을 수 없습니다."),

    // auth
    OAUTH_SOCIAL_LOGIN_FAILED("4000", "로그인에 실패했습니다."),

    // chatRoom
    CHAT_ROOM_NOT_FOUND("4000", "채팅방이 존재하지 않습니다."),

    // chatRoomUser
    CHAT_ROOM_USER_NOT_FOUND("4000", "접근할 수 없는 채팅방입니다."),

    // chat
    CHAT_NOT_FOUND("4000", "아직 채팅방의 채팅이 없습니다."),

    // workspace
    WORKSPACE_NOT_FOUND("4000", "워크스페이스를 찾을 수 없습니다."),
    INVALID_WORKSPACE_CREATE("4000", "잘못된 워크스페이스 생성 요청입니다."),

    // workspaceUser
    WORKSPACE_UNREACHABLE("4000", "워크스페이스에 접근할 수 없습니다."),
}
