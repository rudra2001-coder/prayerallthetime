package com.rudra.prayerallthetime.ui.screen.quran

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranHadithScreen(
    navController: NavController,
    prayerViewModel: PrayerViewModel,
    viewModel: QuranHadithViewModel = hiltViewModel()
) {
    val ayatArabic by viewModel.ayatArabic.collectAsState()
    val ayatEnglish by viewModel.ayatEnglish.collectAsState()
    val surahInfo by viewModel.surahInfo.collectAsState()
    val isAyatBookmarked by viewModel.isAyatBookmarked.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Quran & Hadith") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AyatCard(
                    arabicText = ayatArabic,
                    englishText = ayatEnglish,
                    surahInfo = surahInfo,
                    isBookmarked = isAyatBookmarked,
                    onBookmarkClick = { viewModel.toggleAyatBookmark() }
                )
            }
            
            item {
                Button(
                    onClick = { navController.navigate("hadith") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Go to Hadith Explorer")
                }
            }
        }
    }
}

@Composable
fun AyatCard(
    arabicText: String,
    englishText: String,
    surahInfo: String,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = arabicText, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = englishText, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = surahInfo, style = MaterialTheme.typography.labelSmall)
                IconButton(onClick = onBookmarkClick) {
                    // Use a simple text or icon if library is missing
                    Text(if (isBookmarked) "❤️" else "🤍")
                }
            }
        }
    }
}
