package com.rudra.prayerallthetime.ui.screen.hadith

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rudra.prayerallthetime.data.local.HadithEntity
import com.rudra.prayerallthetime.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HadithScreen(
    navController: NavController,
    viewModel: HadithViewModel = hiltViewModel()
) {
    val hadiths by viewModel.hadiths.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Prophetic Traditions",
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MidnightBlue
                )
            )
        },
        containerColor = MidnightBlue
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MidnightBlue)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = hadiths,
                    key = { it.id }
                ) { hadith ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) + 
                                slideInVertically()
                    ) {
                        HadithListItem(hadith)
                    }
                }
                
                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp), 
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = IslamicGold)
                        }
                    }
                } else {
                    item {
                        Button(
                            onClick = { viewModel.loadMore() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EmeraldGreen.copy(alpha = 0.2f),
                                contentColor = EmeraldGreen
                            ),
                            shape = RoundedCornerShape(16.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Text(
                                "Load More Hadiths", 
                                color = EmeraldGreen, 
                                fontWeight = FontWeight.SemiBold, 
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
fun HadithListItem(hadith: HadithEntity) {
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
                // Book Name
                Text(
                    text = hadith.bookName.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = IslamicGold,
                        letterSpacing = 1.sp
                    )
                )
                
                // Status Badge
                Surface(
                    color = when (hadith.status?.lowercase()) {
                        "sahih" -> SuccessColor.copy(alpha = 0.15f)
                        "hasan" -> InfoColor.copy(alpha = 0.15f)
                        "daif" -> WarningColor.copy(alpha = 0.15f)
                        else -> MidnightBlueLight
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = hadith.status ?: "Sahih",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = when (hadith.status?.lowercase()) {
                                "sahih" -> SuccessColor
                                "hasan" -> InfoColor
                                "daif" -> WarningColor
                                else -> TextPrimaryDark
                            }
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Arabic Text
            if (!hadith.hadithArabic.isNullOrBlank()) {
                Text(
                    text = hadith.hadithArabic,
                    style = ExtendedTypography.arabicLarge.copy(
                        fontWeight = FontWeight.Medium,
                        color = TextPrimaryDark,
                        lineHeight = 38.sp
                    ),
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
            
            HorizontalDivider(color = MidnightBlueLight)
            Spacer(modifier = Modifier.height(16.dp))
            
            // English Translation
            Text(
                text = hadith.hadithEnglish ?: "",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal,
                    lineHeight = 26.sp,
                    color = TextSecondaryDark
                )
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            HorizontalDivider(color = MidnightBlueLight)
            Spacer(modifier = Modifier.height(16.dp))
            
            // Narrator and Hadith Number
            Text(
                text = "Narrated by: ${hadith.englishNarrator ?: "Unknown"}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = TextSecondaryDark,
                    fontWeight = FontWeight.Medium
                )
            )
            
            Text(
                text = "Hadith No: ${hadith.hadithNumber}",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextTertiaryDark,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
