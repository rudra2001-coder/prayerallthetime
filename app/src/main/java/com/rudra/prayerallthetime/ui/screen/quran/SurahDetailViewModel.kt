package com.rudra.prayerallthetime.ui.screen.quran

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.rudra.prayerallthetime.data.local.AyahEntity
import com.rudra.prayerallthetime.data.repository.QuranRepository
import com.rudra.prayerallthetime.data.worker.QuranDownloadWorker
import com.rudra.prayerallthetime.util.QuranAudioPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurahDetailViewModel @Inject constructor(
    private val repository: QuranRepository,
    private val audioPlayer: QuranAudioPlayer,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _ayahs = MutableStateFlow<List<AyahEntity>>(emptyList())
    val ayahs: StateFlow<List<AyahEntity>> = _ayahs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val isPlaying = audioPlayer.isPlaying
    val currentAyahPlaying = audioPlayer.currentAyahNumber

    fun loadSurah(surahNumber: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Check if we have ayahs in DB
            repository.getAyahsForSurah(surahNumber).collect { list ->
                if (list.isEmpty()) {
                    repository.fetchAndSaveSurah(surahNumber)
                } else {
                    _ayahs.value = list
                    _isLoading.value = false
                }
            }
        }
    }

    fun playAyah(ayah: AyahEntity) {
        val audioSource = if (ayah.isDownloaded && ayah.localAudioPath != null) {
            ayah.localAudioPath
        } else {
            ayah.audioUrl
        }

        audioSource?.let {
            audioPlayer.playAyah(it, ayah.number)
        }
    }

    fun togglePlayPause() {
        audioPlayer.togglePlayPause()
    }

    fun downloadSurah(surahNumber: Int) {
        val workManager = WorkManager.getInstance(context)
        val downloadRequest = OneTimeWorkRequestBuilder<QuranDownloadWorker>()
            .setInputData(Data.Builder().putInt("surahNumber", surahNumber).build())
            .build()
        workManager.enqueue(downloadRequest)
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.stop()
    }
}
