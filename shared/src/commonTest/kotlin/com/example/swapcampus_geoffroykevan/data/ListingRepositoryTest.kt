package com.example.swapcampus_geoffroykevan.data

import com.example.swapcampus_geoffroykevan.domain.Category
import com.example.swapcampus_geoffroykevan.domain.ListingStatus
import com.example.swapcampus_geoffroykevan.domain.today
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertThrows
import org.junit.Test

class ListingRepositoryTest {

    private val repository = InMemoryListingRepository()

    private fun post(title: String = "Calculator", price: Double = 10.0) =
        repository.postListing(title, "Description", price, Category.ELECTRONICS, authorId = 1)

    @Test
    fun emptyTitleIsRejected() {
        assertThrows(IllegalArgumentException::class.java) { post(title = "   ") }
        assertTrue(repository.allListings().isEmpty())
    }

    @Test
    fun priceMustBeGreaterThanZero() {
        assertThrows(IllegalArgumentException::class.java) { post(price = 0.0) }
        assertThrows(IllegalArgumentException::class.java) { post(price = -5.0) }
    }

    @Test
    fun validListingIsPostedAsAvailable() {
        val listing = post()
        assertEquals(ListingStatus.Available, listing.status)
        assertEquals(1, repository.allListings().size)
    }

    @Test
    fun statusTransitionsFollowTheRules() {
        val id = post().id
        assertThrows(IllegalArgumentException::class.java) {
            repository.changeStatus(id, ListingStatus.Sold(today()))
        }
        repository.changeStatus(id, ListingStatus.Reserved("Bob"))
        repository.changeStatus(id, ListingStatus.Sold(today()))
        repository.changeStatus(id, ListingStatus.Available)
        assertEquals(ListingStatus.Available, repository.getListing(id)?.status)
    }

    @Test
    fun removedListingDisappears() {
        val id = post().id
        assertTrue(repository.removeListing(id))
        assertTrue(repository.allListings().isEmpty())
    }
}
