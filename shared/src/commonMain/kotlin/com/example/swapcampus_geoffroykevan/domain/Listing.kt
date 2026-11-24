package com.example.swapcampus_geoffroykevan.domain

import kotlinx.datetime.LocalDate

data class Listing(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val category: Category,
    val photoUrl: String? = null,
    val authorId: Int,
    val status: ListingStatus = ListingStatus.Available,
    val createdAt: LocalDate
) {

    fun photoOrDefault(): String = photoUrl ?: "No photo"
}
