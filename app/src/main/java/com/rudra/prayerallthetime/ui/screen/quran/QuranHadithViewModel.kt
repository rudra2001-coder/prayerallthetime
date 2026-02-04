package com.rudra.prayerallthetime.ui.screen.quran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.repository.HadithRepository
import com.rudra.prayerallthetime.data.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuranHadithViewModel @Inject constructor(
    private val hadithRepository: HadithRepository,
    private val quranRepository: QuranRepository
) : ViewModel() {

    private val _ayatArabic = MutableStateFlow("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ")
    val ayatArabic: StateFlow<String> = _ayatArabic.asStateFlow()

    private val _ayatEnglish = MutableStateFlow("In the name of Allah, the Entirely Merciful, the Especially Merciful.")
    val ayatEnglish: StateFlow<String> = _ayatEnglish.asStateFlow()

    private val _surahInfo = MutableStateFlow("Al-Fatihah 1:1")
    val surahInfo: StateFlow<String> = _surahInfo.asStateFlow()

    private val _hadithArabic = MutableStateFlow("خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ")
    val hadithArabic: StateFlow<String> = _hadithArabic.asStateFlow()

    private val _hadithEnglish = MutableStateFlow("The best among you are those who learn the Quran and teach it.")
    val hadithEnglish: StateFlow<String> = _hadithEnglish.asStateFlow()

    private val _hadithInfo = MutableStateFlow("Sahih al-Bukhari")
    val hadithInfo: StateFlow<String> = _hadithInfo.asStateFlow()

    private val _isAyatBookmarked = MutableStateFlow(false)
    val isAyatBookmarked: StateFlow<Boolean> = _isAyatBookmarked.asStateFlow()

    private val _isHadithBookmarked = MutableStateFlow(false)
    val isHadithBookmarked: StateFlow<Boolean> = _isHadithBookmarked.asStateFlow()

    init {
        fetchDailyContent()
    }

    private fun fetchDailyContent() {
        viewModelScope.launch {
            // Fetch Ayat of the Day
            try {
                quranRepository.getRandomAyah()?.let { ayah ->
                    _ayatArabic.value = ayah.text
                    _ayatEnglish.value = ayah.translation ?: ""
                    _surahInfo.value = "${ayah.surahName ?: "Surah ${ayah.surah}"} ${ayah.surah}:${ayah.ayah}"
                    _isAyatBookmarked.value = ayah.isBookmarked
                }
            } catch (e: Exception) {
                // Fallback handled by initial values
            }

            // Fetch Hadith of the Day
            try {
                hadithRepository.getRandomHadith()?.let { hadith ->
                    _hadithArabic.value = hadith.hadithArabic ?: ""
                    _hadithEnglish.value = hadith.hadithEnglish ?: ""
                    _hadithInfo.value = "${hadith.bookName}, Hadith ${hadith.hadithNumber}"
                }
            } catch (e: Exception) {
                // Fallback handled by initial values
            }
        }
    }

    fun toggleAyatBookmark() {
        viewModelScope.launch {
            // In a real scenario, we'd need the current Ayah entity to update it in DB
            // For now we just toggle the UI state
            _isAyatBookmarked.value = !_isAyatBookmarked.value
        }
    }

    fun toggleHadithBookmark() {
        _isHadithBookmarked.value = !_isHadithBookmarked.value
    }

    fun shareContent(content: String) {
        // Implementation for sharing
    }

    fun playAudio(text: String) {
        // Implementation for playing audio
    }
}
