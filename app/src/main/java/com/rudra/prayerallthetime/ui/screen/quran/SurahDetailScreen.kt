package com.rudra.prayerallthetime.ui.screen.quran

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rudra.prayerallthetime.data.local.AyahEntity
import com.rudra.prayerallthetime.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahDetailScreen(
    surahNumber: Int,
    navController: NavController,
    viewModel: SurahDetailViewModel = hiltViewModel()
) {
    val ayahs by viewModel.ayahs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentAyahPlaying by viewModel.currentAyahPlaying.collectAsState()

    LaunchedEffect(surahNumber) {
        viewModel.loadSurah(surahNumber)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = ayahs.firstOrNull()?.surahName ?: "Surah Details",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Back", 
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.downloadSurah(surahNumber) },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(IslamicGold.copy(alpha = 0.15f))
                            .padding(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Download, 
                            contentDescription = "Download Surah", 
                            tint = IslamicGold
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MidnightBlue
                )
            )
        },
        containerColor = MidnightBlue
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MidnightBlue)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(), 
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = IslamicGold,
                        strokeWidth = 3.dp
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = ayahs,
                        key = { it.ayah }
                    ) { ayah ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) + 
                                    slideInVertically()
                        ) {
                            AyahItem(
                                ayah = ayah,
                                isPlaying = isPlaying && currentAyahPlaying == ayah.number,
                                onPlayPauseClick = { viewModel.playAyah(ayah) }
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
        }
    }
}

@Composable
fun AyahItem(
    ayah: AyahEntity,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp, 
                shape = RoundedCornerShape(24.dp), 
                spotColor = ShadowDark
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightBlueCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ayah Number Badge
                Surface(
                    color = MidnightBlueLight,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Ayah ${ayah.ayah}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                    )
                }

                // Play/Pause Button
                IconButton(
                    onClick = onPlayPauseClick,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            if (isPlaying) EmeraldGreen 
                            else IslamicGold.copy(alpha = 0.15f)
                        )
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = if (isPlaying) Color.White else IslamicGold,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Arabic Text
            Text(
                text = ayah.text,
                style = ExtendedTypography.arabicLarge.copy(
                    textAlign = TextAlign.Right,
                    lineHeight = 48.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimaryDark
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(color = MidnightBlueLight)

            Spacer(modifier = Modifier.height(16.dp))

            // Translation
            Text(
                text = ayah.translationBn ?: "",
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 28.sp,
                    color = TextSecondaryDark,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
