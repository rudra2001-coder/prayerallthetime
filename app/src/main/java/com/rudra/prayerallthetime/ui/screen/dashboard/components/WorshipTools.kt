package com.rudra.prayerallthetime.ui.screen.dashboard.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudra.prayerallthetime.ui.theme.*

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
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Worship Essentials",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimaryDark
                )
            )
            
            TextButton(
                onClick = onPrayerTimesClick,
                colors = ButtonDefaults.textButtonColors(contentColor = IslamicGold)
            ) {
                Text(
                    "See All",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            item {
                WorshipToolItem(
                    title = "Tasbeeh",
                    value = "$tasbeehCount",
                    icon = Icons.Default.AutoAwesome,
                    color = IslamicGold,
                    backgroundColor = IslamicGold.copy(alpha = 0.15f),
                    onClick = onTasbeehClick
                )
            }
            item {
                WorshipToolItem(
                    title = "Qibla",
                    value = qiblaDistance,
                    icon = Icons.Default.Explore,
                    color = InfoColor,
                    backgroundColor = InfoLight.copy(alpha = 0.3f),
                    onClick = onQiblaClick
                )
            }
            item {
                WorshipToolItem(
                    title = "Quran",
                    value = currentSurah,
                    icon = Icons.Default.MenuBook,
                    color = QuranColor,
                    backgroundColor = QuranLight.copy(alpha = 0.3f),
                    onClick = onQuranClick
                )
            }
            item {
                WorshipToolItem(
                    title = "Wudu",
                    value = if (wuduStatus) "Ready" else "Check",
                    icon = Icons.Default.WaterDrop,
                    color = CharityColor,
                    backgroundColor = CharityLight.copy(alpha = 0.3f),
                    onClick = onWuduClick
                )
            }
            item {
                WorshipToolItem(
                    title = "Tahajjud",
                    value = tahajjudTime,
                    icon = Icons.Default.NightsStay,
                    color = RamadanPurple,
                    backgroundColor = RamadanLight.copy(alpha = 0.3f),
                    onClick = onTahajjudClick
                )
            }
            item {
                WorshipToolItem(
                    title = "History",
                    value = "View Log",
                    icon = Icons.Default.History,
                    color = TextSecondaryDark,
                    backgroundColor = MidnightBlueLight,
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
    backgroundColor: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "scale"
    )
    
    Card(
        modifier = Modifier
            .width(120.dp)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightBlueCard),
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
                    .background(backgroundColor),
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
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimaryDark,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 11.sp,
                color = color,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PremiumWorshipToolsPanelPreview() {
    PrayerAllTheTimeTheme {
        PremiumWorshipToolsPanel()
    }
}

@Preview(showBackground = true)
@Composable
fun WorshipToolItemPreview() {
    PrayerAllTheTimeTheme {
        WorshipToolItem(
            title = "Tasbeeh",
            value = "33",
            icon = Icons.Default.AutoAwesome,
            color = IslamicGold,
            backgroundColor = IslamicGold.copy(alpha = 0.15f),
            onClick = {}
        )
    }
}
