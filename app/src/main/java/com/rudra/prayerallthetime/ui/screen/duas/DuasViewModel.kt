package com.rudra.prayerallthetime.ui.screen.duas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.local.DuaEntity
import com.rudra.prayerallthetime.data.repository.DuaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DuasViewModel @Inject constructor(
    private val repository: DuaRepository
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val categories: StateFlow<List<String>> = repository.getCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredDuas: StateFlow<List<DuaEntity>> = combine(
        _selectedCategory,
        _searchQuery,
        repository.getAllDuas()
    ) { category, query, allDuas ->
        allDuas.filter { dua ->
            (category == null || dua.category == category) &&
            (query.isEmpty() || dua.title.contains(query, ignoreCase = true) || 
             dua.translation.contains(query, ignoreCase = true) ||
             dua.translationBn?.contains(query, ignoreCase = true) == true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            _isLoading.value = true
            repository.preloadDuasIfEmpty()
            repository.fetchRemoteDuas()
            _isLoading.value = false
        }
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavorite(dua: DuaEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(dua.id, !dua.isFavorite)
        }
    }

    fun refreshDuas() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.fetchRemoteDuas()
            _isLoading.value = false
        }
    }
}
