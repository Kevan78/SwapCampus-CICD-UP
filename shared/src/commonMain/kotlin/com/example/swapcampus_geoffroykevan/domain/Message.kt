package com.example.swapcampus_geoffroykevan.domain

import kotlinx.datetime.LocalDate

data class Message(
    val id: Int,
    val listingId: Int,
    val senderId: Int,
    val recipientId: Int,
    val content: String,
    val sentAt: LocalDate
)
