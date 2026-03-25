package com.drish.moneytracker.data.models

enum class MemberRole {
    ADMIN,
    EDITOR,
    VIEWER
}

data class GroupMember(
    val id: String = "",
    val groupId: String = "",
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val role: MemberRole = MemberRole.VIEWER,
    val joinedAt: Long = System.currentTimeMillis()
)
