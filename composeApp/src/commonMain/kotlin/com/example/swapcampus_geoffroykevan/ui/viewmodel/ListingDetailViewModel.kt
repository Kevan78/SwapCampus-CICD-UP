package com.example.swapcampus_geoffroykevan.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapcampus_geoffroykevan.data.AuthRepository
import com.example.swapcampus_geoffroykevan.data.ListingRepository
import com.example.swapcampus_geoffroykevan.domain.Listing
import com.example.swapcampus_geoffroykevan.domain.ListingStatus
import com.example.swapcampus_geoffroykevan.domain.today
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListingDetailViewModel(
    private val repository: ListingRepository,
    private val authRepository: AuthRepository,
    private val listingId: Int,
) : ViewModel() {
    private val _listing = MutableStateFlow(repository.getListing(listingId))
    val listing: StateFlow<Listing?> = _listing

    var errorMessage by mutableStateOf<String?>(null)

    init {
        viewModelScope.launch {
            repository.listings.collect { _listing.value = repository.getListing(listingId) }
        }
    }

    fun sellerName(authorId: Int): String = authRepository.findUser(authorId)?.name ?: ""

    fun reserve(buyer: String) = changeStatus(ListingStatus.Reserved(buyer))

    fun markAsSold() = changeStatus(ListingStatus.Sold(today()))

    fun cancel() = changeStatus(ListingStatus.Available)

    private fun changeStatus(newStatus: ListingStatus) {
        try {
            repository.changeStatus(listingId, newStatus)
            errorMessage = null
        } catch (e: IllegalArgumentException) {
            errorMessage = e.message
        }
    }

    fun remove() {
        repository.removeListing(listingId)
    }
}
