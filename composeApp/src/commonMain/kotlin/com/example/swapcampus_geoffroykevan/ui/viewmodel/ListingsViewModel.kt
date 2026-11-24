package com.example.swapcampus_geoffroykevan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapcampus_geoffroykevan.data.AuthRepository
import com.example.swapcampus_geoffroykevan.data.ListingRepository
import com.example.swapcampus_geoffroykevan.domain.Category
import com.example.swapcampus_geoffroykevan.domain.Listing
import com.example.swapcampus_geoffroykevan.domain.available
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface ListingsUiState {
    data class Success(val listings: List<Listing>) : ListingsUiState
    data class Error(val message: String) : ListingsUiState
}

class ListingsViewModel(private val repository: ListingRepository) : ViewModel() {
    private val _keyword = MutableStateFlow("")
    val keyword: StateFlow<String> = _keyword

    private val _category = MutableStateFlow<Category?>(null)
    val category: StateFlow<Category?> = _category

    private val _uiState = MutableStateFlow<ListingsUiState>(ListingsUiState.Success(emptyList()))
    val uiState: StateFlow<ListingsUiState> = _uiState

    init {
        loadListings()
        viewModelScope.launch {
            repository.listings.collect { loadListings() }
        }
    }

    private fun loadListings() {
        val result = repository.searchListings(_keyword.value, _category.value)
        _uiState.value = ListingsUiState.Success(result.available())
    }

    fun onKeywordChange(value: String) {
        _keyword.value = value
        loadListings()
    }

    fun onCategoryChange(value: Category?) {
        _category.value = value
        loadListings()
    }
}

class MyListingsViewModel(
    private val repository: ListingRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ListingsUiState>(ListingsUiState.Success(emptyList()))
    val uiState: StateFlow<ListingsUiState> = _uiState

    init {
        loadListings()
        viewModelScope.launch {
            repository.listings.collect { loadListings() }
        }
    }

    private fun loadListings() {
        val userId = authRepository.currentUser.value?.id
        val mine = repository.allListings().filter { it.authorId == userId }
        _uiState.value = ListingsUiState.Success(mine)
    }
}
