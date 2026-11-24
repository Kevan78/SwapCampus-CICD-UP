package com.example.swapcampus_geoffroykevan.domain

import kotlinx.datetime.LocalDate

enum class ModerationActionType {
    LISTING_REMOVAL, ACCOUNT_DEACTIVATION
}

data class ModerationAction(
    val id: Int,
    val targetId: Int,
    val type: ModerationActionType,
    val reason: String,
    val adminId: Int,
    val date: LocalDate
)
