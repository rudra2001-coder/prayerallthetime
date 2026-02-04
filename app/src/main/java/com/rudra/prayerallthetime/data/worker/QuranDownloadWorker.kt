package com.rudra.prayerallthetime.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rudra.prayerallthetime.data.local.AyahDao
import com.rudra.prayerallthetime.data.repository.QuranRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.io.File
import java.net.URL

@HiltWorker
class QuranDownloadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val ayahDao: AyahDao
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val surahNumber = inputData.getInt("surahNumber", -1)
        if (surahNumber == -1) return Result.failure()

        return try {
            val ayahs = ayahDao.getAyahsForSurah(surahNumber).first()
            val directory = File(applicationContext.filesDir, "quran_audio/$surahNumber")
            if (!directory.exists()) directory.mkdirs()

            ayahs.forEach { ayah ->
                if (ayah.audioUrl != null && ayah.localAudioPath == null) {
                    val file = File(directory, "${ayah.ayah}.mp3")
                    URL(ayah.audioUrl).openStream().use { input ->
                        file.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    ayahDao.updateAyah(ayah.copy(
                        isDownloaded = true,
                        localAudioPath = file.absolutePath
                    ))
                }
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
