package com.rudra.prayerallthetime.ui.screen.dashboard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PremiumAyatOfTheDayCard(
    arabicText: String,
    englishText: String,
    translation: String,
    surahInfo: String,
    bookmarkCount: Int = 0,
    isBookmarked: Boolean = false,
    onBookmarkClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF8F5F0),
                            Color(0xFFF0EDE8)
                        )
                    )
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Decorative Header with Islamic pattern
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .drawBehind {
                        drawCircle(
                            color = Color(0xFF8B4513).copy(alpha = 0.1f),
                            radius = 20f,
                            center = Offset(50f, 30f)
                        )
                        drawCircle(
                            color = Color(0xFF8B4513).copy(alpha = 0.1f),
                            radius = 20f,
                            center = Offset(size.width - 50f, 30f)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🕋 AYAT OF THE DAY",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = Color(0xFF8B4513)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Arabic Text with decorative borders
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .drawBehind {
                        val strokeWidth = 2f
                        val y = size.height - strokeWidth / 2
                        drawLine(
                            color = Color(0xFFD4AF37).copy(alpha = 0.3f),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth
                        )
                    }
            ) {
                Text(
                    text = arabicText,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        lineHeight = 48.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    color = Color(0xFF2C3E50),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }

            // Bismillah decoration
            Text(
                text = "﷽",
                fontSize = 32.sp,
                color = Color(0xFFD4AF37).copy(alpha = 0.8f),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // English Translation with better styling
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF2C3E50).copy(alpha = 0.05f))
                    .padding(20.dp)
            ) {
                Text(
                    text = "Translation",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = Color(0xFF8B4513),
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = englishText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 24.sp
                    ),
                    color = Color(0xFF2C3E50),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                if (translation.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF8B4513)
                                )
                            ) {
                                append("Meaning: ")
                            }
                            append(translation)
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2C3E50).copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Footer with Surah Info and Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Surah Info
                Column {
                    Text(
                        text = surahInfo,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Color(0xFF8B4513)
                    )
                    Text(
                        text = "Tap to read full Surah",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF2C3E50).copy(alpha = 0.6f)
                    )
                }

                // Action Buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Bookmark Button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isBookmarked) Color(0xFFD4AF37).copy(alpha = 0.2f)
                                else Color(0xFF2C3E50).copy(alpha = 0.05f)
                            )
                            .clickable { onBookmarkClick() }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = "Bookmark",
                                tint = if (isBookmarked) Color(0xFFD4AF37) else Color(0xFF2C3E50),
                                modifier = Modifier.size(16.dp)
                            )
                            if (bookmarkCount > 0) {
                                Text(
                                    text = bookmarkCount.toString(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isBookmarked) Color(0xFFD4AF37) else Color(0xFF2C3E50),
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Share Button
                    IconButton(
                        onClick = onShareClick,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF2C3E50).copy(alpha = 0.05f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color(0xFF2C3E50),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Decorative Footer Pattern
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xFFD4AF37).copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }
    }
}

// Optional: Keep original for compatibility
@Composable
fun AyatOfTheDayCard(
    arabicText: String,
    englishText: String,
    surahInfo: String,
    onClick: () -> Unit
) {
    PremiumAyatOfTheDayCard(
        arabicText = arabicText,
        englishText = englishText,
        translation = "",
        surahInfo = surahInfo,
        onClick = onClick
    )
}