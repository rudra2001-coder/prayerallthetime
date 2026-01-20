package com.rudra.prayerallthetime.ui.screen.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.local.HabitEntity
import com.rudra.prayerallthetime.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    val habits: StateFlow<List<HabitEntity>> = repository.getAllHabits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.resetDailyHabitsIfNecessary()
        }
    }

    fun addHabit(title: String, goalValue: Int, unit: String, category: String) {
        viewModelScope.launch {
            repository.addHabit(title, goalValue, unit, category)
        }
    }

    fun incrementProgress(habitId: Int) {
        viewModelScope.launch {
            repository.incrementProgress(habitId)
        }
    }

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.deleteHabit(habit)
        }
    }
}
