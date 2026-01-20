package com.rudra.prayerallthetime.ui.screen.quran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.repository.HadithRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuranHadithViewModel @Inject constructor(
    private val hadithRepository: HadithRepository
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
        fetchHadith()
    }

    private fun fetchHadith() {
        viewModelScope.launch {
            val hadith = hadithRepository.getRandomHadith()
            if (hadith != null) {
                _hadithArabic.value = hadith.hadithArabic ?: ""
                _hadithEnglish.value = hadith.hadithEnglish ?: ""
                _hadithInfo.value = "${hadith.book.bookName}, Hadith ${hadith.hadithNumber}"
            }
        }
    }

    fun toggleAyatBookmark() {
        _isAyatBookmarked.value = !_isAyatBookmarked.value
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
