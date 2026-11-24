package com.example.swapcampus_geoffroykevan.domain

import kotlinx.datetime.LocalDate

data class UserAccount(
    val id: Int,
    val name: String,
    val email: String,
    val passwordHash: String,
    val createdAt: LocalDate,
    val role: UserRole,
    val active: Boolean = true
)
