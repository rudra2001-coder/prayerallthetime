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
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
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
fun PremiumHadithOfTheDayCard(
    arabicText: String = "خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ",
    englishText: String = "The best among you are those who learn the Quran and teach it.",
    translation: String = "Seeking knowledge of the Quran and imparting it to others is a highly virtuous deed.",
    reference: String = "Sahih al-Bukhari",
    bookAndNumber: String = "Book 61, Hadith 545",
    grade: String = "Sahih (Authentic)",
    isBookmarked: Boolean = false,
    bookmarkCount: Int = 0,
    onBookmarkClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onAudioClick: () -> Unit = {},
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(28.dp),
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
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF8F5F0),
                            Color(0xFFF0EDE8)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Decorative Header with Islamic pattern
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .drawBehind {
                            // Decorative pattern
                            for (i in 0..3) {
                                val x = (i + 1) * (size.width / 5)
                                drawCircle(
                                    color = Color(0xFF8B4513).copy(alpha = 0.1f),
                                    radius = 8f,
                                    center = Offset(x, 30f)
                                )
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF8B4513).copy(alpha = 0.1f))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatQuote,
                                contentDescription = "Hadith",
                                tint = Color(0xFF8B4513),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "📜 HADITH OF THE DAY",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8B4513),
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Wisdom from the Prophet ﷺ",
                                fontSize = 12.sp,
                                color = Color(0xFF8B4513).copy(alpha = 0.7f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Arabic Text with decorative borders
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF2C3E50).copy(alpha = 0.05f))
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Decorative quote marks
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "«",
                                fontSize = 32.sp,
                                color = Color(0xFFD4AF37).copy(alpha = 0.5f)
                            )
                            Text(
                                text = "»",
                                fontSize = 32.sp,
                                color = Color(0xFFD4AF37).copy(alpha = 0.5f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = arabicText,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C3E50),
                            textAlign = TextAlign.Center,
                            lineHeight = 36.sp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Bismillah separator
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

                Spacer(modifier = Modifier.height(20.dp))

                // English Translation
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.3f))
                        .padding(20.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF8B4513)
                                )
                            ) {
                                append("Translation: ")
                            }
                            append(englishText)
                        },
                        fontSize = 16.sp,
                        color = Color(0xFF2C3E50),
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Center
                    )

                    if (translation.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF8B4513).copy(alpha = 0.8f)
                                    )
                                ) {
                                    append("Meaning: ")
                                }
                                append(translation)
                            },
                            fontSize = 14.sp,
                            color = Color(0xFF2C3E50).copy(alpha = 0.8f),
                            lineHeight = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Reference and Details
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF8B4513).copy(alpha = 0.1f))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = reference,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8B4513)
                            )
                            Text(
                                text = bookAndNumber,
                                fontSize = 12.sp,
                                color = Color(0xFF8B4513).copy(alpha = 0.7f)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF2C3E50).copy(alpha = 0.1f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = grade,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF2C3E50)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Audio Button
                    IconButton(
                        onClick = onAudioClick,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF4ECDC4).copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Listen to Hadith",
                            tint = Color(0xFF4ECDC4),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Bookmark Button with count
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isBookmarked) Color(0xFFD4AF37).copy(alpha = 0.2f)
                                else Color(0xFF2C3E50).copy(alpha = 0.1f)
                            )
                            .clickable { onBookmarkClick() }
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = "Bookmark Hadith",
                                tint = if (isBookmarked) Color(0xFFD4AF37) else Color(0xFF2C3E50),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (bookmarkCount > 0) bookmarkCount.toString() else "Save",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isBookmarked) Color(0xFFD4AF37) else Color(0xFF2C3E50)
                            )
                        }
                    }

                    // Share Button
                    IconButton(
                        onClick = onShareClick,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF45B7D1).copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Hadith",
                            tint = Color(0xFF45B7D1),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Decorative Footer
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF8B4513).copy(alpha = 0.3f),
                                    Color(0xFFD4AF37).copy(alpha = 0.3f),
                                    Color(0xFF8B4513).copy(alpha = 0.3f)
                                )
                            )
                        )
                )
            }
        }
    }
}

// Original version for backward compatibility (no errors)
@Composable
fun HadithOfTheDayCard(onClick: () -> Unit) {
    PremiumHadithOfTheDayCard(
        arabicText = "خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ",
        englishText = "The best among you are those who learn the Quran and teach it.",
        translation = "Seeking knowledge of the Quran and imparting it to others is a highly virtuous deed.",
        reference = "Sahih al-Bukhari",
        bookAndNumber = "Book 61, Hadith 545",
        grade = "Sahih (Authentic)",
        isBookmarked = false,
        bookmarkCount = 0,
        onBookmarkClick = {},
        onShareClick = {},
        onAudioClick = {},
        onClick = onClick
    )
}