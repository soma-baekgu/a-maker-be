package com.backgu.amaker.api.event.dto

import java.time.LocalDateTime

data class TaskEventCreateDto(
    val eventTitle: String,
    val eventDetails: String,
    val assignees: List<String>,
    val deadLine: LocalDateTime,
    val notificationStartHour: Int,
    val notificationStartMinute: Int,
    val interval: Int,
)
