package com.rudra.prayerallthetime.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuickWorshipToolsPanel(
    tasbeehCount: Int = 33,
    qiblaDistance: String = "12.5 km",
    currentSurah: String = "Al-Fatihah",
    wuduStatus: Boolean = true,
    tahajjudTime: String = "03:45 AM",
    onTasbeehClick: () -> Unit,
    onQiblaClick: () -> Unit,
    onQuranClick: () -> Unit,
    onWuduClick: () -> Unit,
    onPrayerTimesClick: () -> Unit,
    onTahajjudClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
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
                            Color(0xFFF9F7F3),
                            Color(0xFFF0EDE8)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🕌 Worship Tools",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                    Text(
                        text = "Quick access to essential Islamic tools",
                        fontSize = 12.sp,
                        color = Color(0xFF2C3E50).copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Status Indicator
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (wuduStatus) Color(0xFF4ECDC4).copy(alpha = 0.2f)
                            else Color(0xFFFF6B6B).copy(alpha = 0.2f)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (wuduStatus) Color(0xFF4ECDC4)
                                    else Color(0xFFFF6B6B)
                                )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (wuduStatus) "Pure" else "Wudu Needed",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (wuduStatus) Color(0xFF4ECDC4) else Color(0xFFFF6B6B)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Main Tools Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Column 1
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickWorshipToolItemCard(
                        icon = Icons.Default.FormatListBulleted,
                        title = "Digital Tasbeeh",
                        subtitle = "Count your dhikr",
                        count = "$tasbeehCount/33",
                        iconColor = Color(0xFF8B4513),
                        backgroundColor = Color(0xFF8B4513).copy(alpha = 0.1f),
                        onClick = onTasbeehClick
                    )

                    QuickWorshipToolItemCard(
                        icon = Icons.Default.MenuBook,
                        title = "Quran Reader",
                        subtitle = "Continue reading",
                        status = currentSurah,
                        iconColor = Color(0xFF2C3E50),
                        backgroundColor = Color(0xFF2C3E50).copy(alpha = 0.1f),
                        onClick = onQuranClick
                    )
                }

                // Column 2
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickWorshipToolItemCard(
                        icon = Icons.Default.Explore,
                        title = "Qibla Finder",
                        subtitle = "Direction to Kaaba",
                        status = qiblaDistance,
                        iconColor = Color(0xFFD4AF37),
                        backgroundColor = Color(0xFFD4AF37).copy(alpha = 0.1f),
                        onClick = onQiblaClick
                    )

                    QuickWorshipToolItemCard(
                        icon = Icons.Default.WaterDrop,
                        title = "Wudu Guide",
                        subtitle = "Ablution steps",
                        status = if (wuduStatus) "Clean" else "Required",
                        iconColor = Color(0xFF4ECDC4),
                        backgroundColor = Color(0xFF4ECDC4).copy(alpha = 0.1f),
                        onClick = onWuduClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Actions Row
            Text(
                text = "Quick Actions",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2C3E50),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF2C3E50).copy(alpha = 0.05f))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickActionButton(
                    icon = Icons.Default.NightsStay,
                    title = "Prayer Times",
                    color = Color(0xFF45B7D1),
                    onClick = onPrayerTimesClick
                )

                QuickActionButton(
                    icon = Icons.Default.NightsStay,
                    title = "Tahajjud",
                    subtitle = tahajjudTime,
                    color = Color(0xFF6A5ACD),
                    onClick = onTahajjudClick
                )

                QuickActionButton(
                    icon = Icons.Default.Lightbulb,
                    title = "Duas",
                    subtitle = "Daily",
                    color = Color(0xFFFFD93D),
                    onClick = { /* Handle Dua click */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Today's Tip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF8B4513).copy(alpha = 0.1f))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFD4AF37).copy(alpha = 0.2f))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Circle,
                            contentDescription = "Tip",
                            tint = Color(0xFFD4AF37),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Today's Spiritual Tip",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF8B4513)
                        )
                        Text(
                            text = "Recite Ayat-ul-Kursi after every prayer for protection",
                            fontSize = 12.sp,
                            color = Color(0xFF2C3E50).copy(alpha = 0.8f),
                            lineHeight = 16.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "More",
                        tint = Color(0xFF8B4513).copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun QuickWorshipToolItemCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    count: String? = null,
    status: String? = null,
    iconColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icon Container
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(iconColor.copy(alpha = 0.2f))
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Text Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2C3E50)
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF2C3E50).copy(alpha = 0.6f)
                )
            }

            // Count/Status Badge
            if (count != null || status != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF2C3E50).copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = count ?: status ?: "",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = iconColor
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color.copy(alpha = 0.2f))
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2C3E50),
            textAlign = TextAlign.Center
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = Color(0xFF2C3E50).copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

// Original version for backward compatibility
@Composable
fun QuickWorshipTools(
    onTasbeehClick: () -> Unit,
    onQiblaClick: () -> Unit,
    onQuranClick: () -> Unit
) {
    QuickWorshipToolsPanel(
        onTasbeehClick = onTasbeehClick,
        onQiblaClick = onQiblaClick,
        onQuranClick = onQuranClick,
        onWuduClick = { /* Handle wudu click */ },
        onPrayerTimesClick = { /* Handle prayer times click */ },
        onTahajjudClick = { /* Handle tahajjud click */ }
    )
}
