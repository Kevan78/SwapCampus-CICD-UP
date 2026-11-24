package com.example.swapcampus_geoffroykevan.ui

import com.example.swapcampus_geoffroykevan.data.AuthRepository
import com.example.swapcampus_geoffroykevan.data.InMemoryListingRepository
import com.example.swapcampus_geoffroykevan.data.ListingRepository
import com.example.swapcampus_geoffroykevan.domain.Category

object AppContainer {
    val listingRepository: ListingRepository = InMemoryListingRepository().also { repo ->
        repo.postListing(
            "Polycopié Java L2",
            "Quelques surlignages, sinon nickel",
            12.0,
            Category.BOOKS,
            authorId = 1,
        )
        repo.postListing("Calculatrice Casio", "Pile encore bonne", 18.0, Category.ELECTRONICS, authorId = 1)
        repo.postListing("Vélo campus", "Freins à revoir", 75.0, Category.SPORTS_LEISURE, authorId = 2)
        repo.postListing("Lampe de bureau", "LED blanche", 6.0, Category.FURNITURE, authorId = 3)
    }

    val authRepository = AuthRepository()
}
