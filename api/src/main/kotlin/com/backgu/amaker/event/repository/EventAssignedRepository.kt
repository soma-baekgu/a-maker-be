package com.backgu.amaker.event.repository

import com.backgu.amaker.event.jpa.EventAssignedUserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface EventAssignedRepository : JpaRepository<EventAssignedUserEntity, Long> {
    fun findByEventId(eventId: Long): List<EventAssignedUserEntity>

    fun findByEventIdIn(eventIds: List<Long>): List<EventAssignedUserEntity>
}
