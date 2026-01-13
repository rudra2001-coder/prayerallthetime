package com.rudra.prayerallthetime.ui.screen.family

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.local.FamilyMemberRecord
import com.rudra.prayerallthetime.data.local.PrayerDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FamilyViewModel @Inject constructor(
    private val prayerDao: PrayerDao
) : ViewModel() {

    val familyMembers: StateFlow<List<FamilyMemberRecord>> = prayerDao.getAllFamilyMembers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addFamilyMember(name: String) {
        viewModelScope.launch {
            prayerDao.insertFamilyMember(FamilyMemberRecord(name = name, completedPrayers = 0))
        }
    }

    fun removeFamilyMember(member: FamilyMemberRecord) {
        viewModelScope.launch {
            prayerDao.deleteFamilyMember(member)
        }
    }
}
