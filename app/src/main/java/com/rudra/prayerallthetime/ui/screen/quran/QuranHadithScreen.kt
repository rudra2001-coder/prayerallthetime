package com.rudra.prayerallthetime.ui.screen.quran

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.screen.dashboard.components.PremiumAyatOfTheDayCard
import com.rudra.prayerallthetime.ui.screen.dashboard.components.PremiumHadithOfTheDayCard
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranHadithScreen(navController: NavController, prayerViewModel: PrayerViewModel) {
    val ayatArabic by prayerViewModel.ayatArabic.collectAsState()
    val ayatEnglish by prayerViewModel.ayatEnglish.collectAsState()
    val surahInfo by prayerViewModel.surahInfo.collectAsState()
    val isAyatBookmarked by prayerViewModel.isAyatBookmarked.collectAsState()
    val isHadithBookmarked by prayerViewModel.isHadithBookmarked.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quran & Hadith") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            PremiumAyatOfTheDayCard(
                arabicText = ayatArabic,
                englishText = ayatEnglish,
                translation = "Daily verse from the Noble Quran",
                surahInfo = surahInfo,
                isBookmarked = isAyatBookmarked,
                onBookmarkClick = { prayerViewModel.toggleAyatBookmark() },
                onShareClick = { prayerViewModel.shareContent("$ayatArabic\n\n$ayatEnglish\n($surahInfo)") },
                onClick = { /* Could navigate to full Surah view in future */ }
            )

            PremiumHadithOfTheDayCard(
                isBookmarked = isHadithBookmarked,
                onBookmarkClick = { prayerViewModel.toggleHadithBookmark() },
                onShareClick = { prayerViewModel.shareContent("The best among you are those who learn the Quran and teach it. (Sahih al-Bukhari)") },
                onAudioClick = { prayerViewModel.playAudio("خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ") },
                onClick = { /* Could navigate to full Hadith view in future */ }
            )
        }
    }
}
