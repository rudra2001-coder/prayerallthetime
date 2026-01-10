package com.rudra.prayerallthetime.ui.components

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.History
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PremiumWorshipToolsPanel(
    tasbeehCount: Int = 0,
    qiblaDistance: String = "Detecting...",
    currentSurah: String = "Al-Baqarah",
    wuduStatus: Boolean = false,
    tahajjudTime: String = "03:45 AM",
    onTasbeehClick: () -> Unit = {},
    onQiblaClick: () -> Unit = {},
    onQuranClick: () -> Unit = {},
    onWuduClick: () -> Unit = {},
    onPrayerTimesClick: () -> Unit = {},
    onTahajjudClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = "🛠 Worship Essentials",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                WorshipToolItem(
                    title = "Tasbeeh",
                    value = "$tasbeehCount count",
                    icon = Icons.Default.AutoAwesome,
                    color = Color(0xFFD4AF37),
                    onClick = onTasbeehClick
                )
            }
            item {
                WorshipToolItem(
                    title = "Qibla Finder",
                    value = qiblaDistance,
                    icon = Icons.Default.Explore,
                    color = Color(0xFF4ECDC4),
                    onClick = onQiblaClick
                )
            }
            item {
                WorshipToolItem(
                    title = "Read Quran",
                    value = currentSurah,
                    icon = Icons.Default.Book,
                    color = Color(0xFF8B4513),
                    onClick = onQuranClick
                )
            }
            item {
                WorshipToolItem(
                    title = "Wudu Guide",
                    value = if (wuduStatus) "Ready" else "Tutorial",
                    icon = Icons.Default.WaterDrop,
                    color = Color(0xFF45B7D1),
                    onClick = onWuduClick
                )
            }
            item {
                WorshipToolItem(
                    title = "Tahajjud",
                    value = tahajjudTime,
                    icon = Icons.Default.NightsStay,
                    color = Color(0xFF2C3E50),
                    onClick = onTahajjudClick
                )
            }
            item {
                WorshipToolItem(
                    title = "History",
                    value = "Log",
                    icon = Icons.Default.History,
                    color = Color(0xFF96CEB4),
                    onClick = onPrayerTimesClick
                )
            }
        }
    }
}

@Composable
fun WorshipToolItem(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(130.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 11.sp,
                color = color,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}
