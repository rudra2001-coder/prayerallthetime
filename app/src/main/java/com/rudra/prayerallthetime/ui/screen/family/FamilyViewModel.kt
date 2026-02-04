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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class FamilyViewModel @Inject constructor(
    private val prayerDao: PrayerDao
) : ViewModel() {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val familyMembers: StateFlow<List<FamilyMemberRecord>> = prayerDao.getAllFamilyMembers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addFamilyMember(name: String, relationship: String) {
        viewModelScope.launch {
            val today = LocalDate.now().format(dateFormatter)
            prayerDao.insertFamilyMember(
                FamilyMemberRecord(
                    name = name,
                    relationship = relationship,
                    lastActiveDate = today
                )
            )
        }
    }

    fun incrementMemberPrayer(member: FamilyMemberRecord) {
        viewModelScope.launch {
            val today = LocalDate.now().format(dateFormatter)
            val updatedMember = if (member.lastActiveDate == today) {
                member.copy(
                    completedPrayersToday = (member.completedPrayersToday + 1).coerceAtMost(5),
                    totalCompletedPrayers = member.totalCompletedPrayers + 1
                )
            } else {
                member.copy(
                    completedPrayersToday = 1,
                    totalCompletedPrayers = member.totalCompletedPrayers + 1,
                    lastActiveDate = today
                )
            }
            prayerDao.insertFamilyMember(updatedMember)
        }
    }

    fun resetMemberPrayer(member: FamilyMemberRecord) {
        viewModelScope.launch {
            val today = LocalDate.now().format(dateFormatter)
            val updatedMember = member.copy(
                completedPrayersToday = 0,
                lastActiveDate = today
            )
            prayerDao.insertFamilyMember(updatedMember)
        }
    }

    fun removeFamilyMember(member: FamilyMemberRecord) {
        viewModelScope.launch {
            prayerDao.deleteFamilyMember(member)
        }
    }
}
