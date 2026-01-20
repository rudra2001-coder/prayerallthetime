package com.rudra.prayerallthetime.ui.screen.quran

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.navigation.Screen
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerViewModel
import kotlinx.coroutines.launch

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

    val hadithArabic by viewModel.hadithArabic.collectAsState()
    val hadithEnglish by viewModel.hadithEnglish.collectAsState()
    val hadithInfo by viewModel.hadithInfo.collectAsState()
    val isHadithBookmarked by viewModel.isHadithBookmarked.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFFBFBFB),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                QuranHeader()
            }

            item {
                SectionHeader(title = "Ayat of the Day", icon = Icons.Default.MenuBook)
                ContentCard(
                    arabicText = ayatArabic,
                    translationText = ayatEnglish,
                    infoText = surahInfo,
                    isBookmarked = isAyatBookmarked,
                    onBookmarkClick = { viewModel.toggleAyatBookmark() },
                    onShareClick = { viewModel.shareContent(ayatEnglish) },
                    onPlayClick = { viewModel.playAudio(ayatArabic) },
                    accentColor = Color(0xFF4ECDC4)
                )
            }

            item {
                SectionHeader(title = "Hadith of the Day", icon = Icons.Default.HistoryEdu)
                ContentCard(
                    arabicText = hadithArabic,
                    translationText = hadithEnglish,
                    infoText = hadithInfo,
                    isBookmarked = isHadithBookmarked,
                    onBookmarkClick = { viewModel.toggleHadithBookmark() },
                    onShareClick = { viewModel.shareContent(hadithEnglish) },
                    onPlayClick = { viewModel.playAudio(hadithArabic) },
                    accentColor = Color(0xFFD4AF37)
                )
            }

            item {
                SectionHeader(title = "Explore Library", icon = Icons.Default.AutoAwesome)
                ExploreGrid(navController) { message ->
                    scope.launch {
                        snackbarHostState.showSnackbar(message)
                    }
                }
            }
        }
    }
}

@Composable
fun QuranHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = "Quran & Wisdom",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF2C3E50)
            )
        )
        Text(
            text = "Daily inspiration from the Divine",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun SectionHeader(title: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF2C3E50),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )
        )
    }
}

@Composable
fun ContentCard(
    arabicText: String,
    translationText: String,
    infoText: String,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    onShareClick: () -> Unit,
    onPlayClick: () -> Unit,
    accentColor: Color
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = accentColor.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = arabicText,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 38.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF2C3E50)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .height(2.dp)
                    .background(accentColor.copy(alpha = 0.3f))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = translationText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    color = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = accentColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = infoText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onPlayClick) {
                        Icon(Icons.Default.PlayArrow, "Play", tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                    IconButton(onClick = onShareClick) {
                        Icon(Icons.Default.Share, "Share", tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                    IconButton(onClick = onBookmarkClick) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) accentColor else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExploreGrid(navController: NavController, onFeatureComingSoon: (String) -> Unit) {
    val items = listOf(
        ExploreGridItem("Surah List", Icons.Default.List, Color(0xFF4ECDC4), ""),
        ExploreGridItem("Juz List", Icons.Default.FormatListNumbered, Color(0xFF45B7D1), ""),
        ExploreGridItem("Bookmarks", Icons.Default.Bookmarks, Color(0xFFFF6B6B), ""),
        ExploreGridItem("Hadith Books", Icons.Default.LibraryBooks, Color(0xFFD4AF37), Screen.Hadith.route)
    )

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ExploreButton(items[0], Modifier.weight(1f)) { 
                if (items[0].route.isNotEmpty()) navController.navigate(items[0].route) 
                else onFeatureComingSoon("Surah List feature coming soon")
            }
            ExploreButton(items[1], Modifier.weight(1f)) { 
                if (items[1].route.isNotEmpty()) navController.navigate(items[1].route) 
                else onFeatureComingSoon("Juz List feature coming soon")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ExploreButton(items[2], Modifier.weight(1f)) { 
                if (items[2].route.isNotEmpty()) navController.navigate(items[2].route) 
                else onFeatureComingSoon("Bookmarks feature coming soon")
            }
            ExploreButton(items[3], Modifier.weight(1f)) { 
                if (items[3].route.isNotEmpty()) navController.navigate(items[3].route) 
                else onFeatureComingSoon("Hadith feature coming soon")
            }
        }
    }
}

@Composable
fun ExploreButton(item: ExploreGridItem, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(item.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(item.icon, null, tint = item.color, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )
            )
        }
    }
}

data class ExploreGridItem(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val route: String
)
