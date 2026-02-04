package com.rudra.prayerallthetime.ui.screen.quran

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
import com.rudra.prayerallthetime.ui.theme.IslamicGold
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
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            TopAppBar(
                title = { Text("Quran & Wisdom", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                ReadingProgressCard {
                    navController.navigate(Screen.SurahList.route)
                }
            }

            item {
                ContentSection(
                    title = "Ayat of the Day",
                    arabicText = ayatArabic,
                    translationText = ayatEnglish,
                    infoText = surahInfo,
                    isBookmarked = isAyatBookmarked,
                    accentColor = Color(0xFF4ECDC4),
                    onBookmark = { viewModel.toggleAyatBookmark() },
                    onShare = { viewModel.shareContent(ayatEnglish) },
                    onPlay = { viewModel.playAudio(ayatArabic) }
                )
            }

            item {
                ContentSection(
                    title = "Prophetic Tradition",
                    arabicText = hadithArabic,
                    translationText = hadithEnglish,
                    infoText = hadithInfo,
                    isBookmarked = isHadithBookmarked,
                    accentColor = IslamicGold,
                    onBookmark = { viewModel.toggleHadithBookmark() },
                    onShare = { viewModel.shareContent(hadithEnglish) },
                    onPlay = { viewModel.playAudio(hadithArabic) }
                )
            }

            item {
                LibraryCategories(navController) { message ->
                    scope.launch { snackbarHostState.showSnackbar(message) }
                }
            }
        }
    }
}

@Composable
fun ReadingProgressCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C3E50))
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Continue Reading", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                Text("Holy Quran", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("114 Surahs with Bangla Translation", color = IslamicGold, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(IslamicGold),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.MenuBook, null, tint = Color.White)
            }
        }
    }
}

@Composable
fun ContentSection(
    title: String,
    arabicText: String,
    translationText: String,
    infoText: String,
    isBookmarked: Boolean,
    accentColor: Color,
    onBookmark: () -> Unit,
    onShare: () -> Unit,
    onPlay: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50),
            modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(28.dp), spotColor = accentColor.copy(alpha = 0.2f)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = arabicText,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        textAlign = TextAlign.Center,
                        lineHeight = 42.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF2C3E50)
                )

                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    text = translationText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        color = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = accentColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = infoText,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = accentColor
                            )
                        )
                    }

                    Row {
                        ContentActionButton(Icons.Default.PlayArrow, onPlay)
                        ContentActionButton(Icons.Default.Share, onShare)
                        ContentActionButton(
                            if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            onBookmark,
                            tint = if (isBookmarked) accentColor else Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContentActionButton(icon: ImageVector, onClick: () -> Unit, tint: Color = Color.Gray) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .padding(start = 8.dp)
            .size(36.dp)
            .background(Color(0xFFF5F5F5), CircleShape)
    ) {
        Icon(icon, null, modifier = Modifier.size(18.dp), tint = tint)
    }
}

@Composable
fun LibraryCategories(navController: NavController, onFeatureComingSoon: (String) -> Unit) {
    val items = listOf(
        LibraryItem("Surah List", Icons.Default.FormatListBulleted, Color(0xFF4ECDC4), Screen.SurahList.route),
        LibraryItem("Juz List", Icons.Default.GridView, Color(0xFF45B7D1), ""),
        LibraryItem("Hadith Books", Icons.Default.MenuBook, IslamicGold, Screen.Hadith.route),
        LibraryItem("Bookmarks", Icons.Default.Bookmarks, Color(0xFFFF6B6B), "")
    )

    Column(modifier = Modifier.padding(20.dp)) {
        Text(
            text = "Explore Library",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50),
            modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
        )
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            LibraryCard(items[0], Modifier.weight(1f)) { navController.navigate(items[0].route) }
            LibraryCard(items[1], Modifier.weight(1f)) { onFeatureComingSoon("Juz List coming soon") }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            LibraryCard(items[2], Modifier.weight(1f)) { navController.navigate(items[2].route) }
            LibraryCard(items[3], Modifier.weight(1f)) { onFeatureComingSoon("Bookmarks coming soon") }
        }
    }
}

@Composable
fun LibraryCard(item: LibraryItem, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(item.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(item.icon, null, tint = item.color, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )
        }
    }
}

data class LibraryItem(val title: String, val icon: ImageVector, val color: Color, val route: String)
