package com.example.swapcampus_geoffroykevan.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.swapcampus_geoffroykevan.data.AuthRepository
import com.example.swapcampus_geoffroykevan.data.ListingRepository
import com.example.swapcampus_geoffroykevan.domain.Category

class ListingFormViewModel(
    private val repository: ListingRepository,
    private val authRepository: AuthRepository,
    private val listingId: Int?,
) : ViewModel() {
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var priceText by mutableStateOf("")
    var category by mutableStateOf(Category.OTHER)
    var photoUrl by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)
    var mustLogin by mutableStateOf(false)

    var forbidden by mutableStateOf(false)
        private set

    val isEditing: Boolean = listingId != null

    init {
        if (listingId != null) {
            val existing = repository.getListing(listingId)
            if (existing == null || existing.authorId != authRepository.currentUser.value?.id) {
                forbidden = true
            } else {
                title = existing.title
                description = existing.description
                priceText = existing.price.toString()
                category = existing.category
                photoUrl = existing.photoUrl ?: ""
            }
        }
    }

    fun save(): Boolean {
        val user = authRepository.currentUser.value
        if (user == null) {
            mustLogin = true
            return false
        }
        val price = priceText.replace(',', '.').toDoubleOrNull()
        if (price == null) {
            errorMessage = "The price must be a number"
            return false
        }
        return try {
            if (listingId == null) {
                repository.postListing(title, description, price, category, user.id, photoUrl)
            } else {
                repository.updateListing(listingId, title, description, price, category, photoUrl)
            }
            errorMessage = null
            true
        } catch (e: IllegalArgumentException) {
            errorMessage = e.message
            false
        }
    }
}
