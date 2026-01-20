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

    val categories: StateFlow<List<String>> = repository.getCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredDuas: StateFlow<List<DuaEntity>> = _selectedCategory
        .flatMapLatest { category ->
            if (category == null) repository.getAllDuas()
            else repository.getDuasByCategory(category)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.preloadDuasIfEmpty()
        }
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }
}
