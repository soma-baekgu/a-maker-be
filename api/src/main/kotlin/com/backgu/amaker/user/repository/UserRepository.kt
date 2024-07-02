package com.backgu.amaker.user.repository

import com.backgu.amaker.user.jpa.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, String> {
    fun findByEmail(email: String): UserEntity?
}
