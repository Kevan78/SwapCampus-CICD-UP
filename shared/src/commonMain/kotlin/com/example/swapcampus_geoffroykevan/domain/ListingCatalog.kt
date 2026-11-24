package com.example.swapcampus_geoffroykevan.domain

import kotlinx.datetime.LocalDate

fun List<Listing>.available(): List<Listing> = filter { it.status is ListingStatus.Available }

class ListingCatalog {
    private val listings = mutableListOf<Listing>()
    private var nextId = 1

    fun allListings(): List<Listing> = listings.toList()

    fun postListing(
        title: String,
        description: String,
        price: Double,
        category: Category,
        authorId: Int,
        createdAt: LocalDate,
        photoUrl: String? = null
    ): Listing {
        return Listing(
            id = nextId++,
            title = title,
            description = description,
            price = price,
            category = category,
            photoUrl = photoUrl,
            authorId = authorId,
            createdAt = createdAt
        ).apply {
            listings.add(this)
        }
    }

    fun searchListings(keyword: String? = null, category: Category? = null): List<Listing> {
        return listings.filter { listing ->
            val matchesKeyword = keyword.isNullOrBlank() ||
                listing.title.contains(keyword, ignoreCase = true) ||
                listing.description.contains(keyword, ignoreCase = true)
            val matchesCategory = category == null || listing.category == category
            matchesKeyword && matchesCategory
        }
    }

    fun updateListing(
        id: Int,
        title: String,
        description: String,
        price: Double,
        category: Category,
        photoUrl: String?
    ): Listing? {
        val index = listings.indexOfFirst { it.id == id }
        if (index == -1) return null
        val updated = listings[index].copy(
            title = title,
            description = description,
            price = price,
            category = category,
            photoUrl = photoUrl
        )
        listings[index] = updated
        return updated
    }

    fun changeStatus(id: Int, newStatus: ListingStatus): Listing? {
        val index = listings.indexOfFirst { it.id == id }
        if (index == -1) return null
        val updated = listings[index].copy(status = newStatus)
        listings[index] = updated
        return updated
    }

    fun removeListing(id: Int): Boolean = listings.removeAll { it.id == id }
}
