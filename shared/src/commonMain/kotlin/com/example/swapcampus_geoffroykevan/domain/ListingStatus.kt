package com.example.swapcampus_geoffroykevan.domain

import kotlinx.datetime.LocalDate

sealed class ListingStatus {
    data object Available : ListingStatus()
    data class Reserved(val buyer: String) : ListingStatus()
    data class Sold(val saleDate: LocalDate) : ListingStatus()
}

fun ListingStatus.label(): String = when (this) {
    is ListingStatus.Available -> "Available"
    is ListingStatus.Reserved -> "Reserved"
    is ListingStatus.Sold -> "Sold"
}
