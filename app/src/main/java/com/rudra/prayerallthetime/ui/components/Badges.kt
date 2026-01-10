package com.rudra.prayerallthetime.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudra.prayerallthetime.ui.PrayerViewModel

@Composable
fun PremiumBadgesCard(
    earnedBadges: List<Badge>,
    upcomingBadges: List<Badge>,
    onBadgeClick: (Badge) -> Unit = {}
) {
    val totalAchievements = earnedBadges.size + upcomingBadges.size
    val completedAchievements = earnedBadges.size

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🏆 Achievements & Badges",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                    Text(
                        text = "Track your spiritual journey milestones",
                        fontSize = 12.sp,
                        color = Color(0xFF2C3E50).copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF8B4513).copy(alpha = 0.1f))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "$completedAchievements/$totalAchievements",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B4513)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = if (totalAchievements > 0) completedAchievements.toFloat() / totalAchievements.toFloat() else 0f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = Color(0xFFD4AF37),
                trackColor = Color(0xFF2C3E50).copy(alpha = 0.1f)
            )

            Spacer(modifier = Modifier.height(28.dp))

            if (earnedBadges.isNotEmpty()) {
                Text(
                    text = "🌟 Your Badges",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2C3E50),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                ) {
                    items(earnedBadges) { badge ->
                        BadgeItem(
                            badge = badge,
                            isLocked = false,
                            onClick = { onBadgeClick(badge) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            if (upcomingBadges.isNotEmpty()) {
                Text(
                    text = "🔜 Next Achievements",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2C3E50),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                ) {
                    items(upcomingBadges) { badge ->
                        BadgeItem(
                            badge = badge,
                            isLocked = true,
                            onClick = { onBadgeClick(badge) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BadgeItem(
    badge: Badge,
    isLocked: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(120.dp)
            .shadow(
                elevation = if (isLocked) 4.dp else 8.dp,
                shape = RoundedCornerShape(20.dp),
                clip = true
            )
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isLocked) Color(0xFFE0E0E0)
                else badge.color.copy(alpha = 0.1f)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(
                        if (isLocked) Color(0xFFBDBDBD)
                        else badge.color.copy(alpha = 0.2f)
                    )
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isLocked) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color(0xFFFFFFFF),
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .drawBehind {
                                drawCircle(
                                    color = badge.color.copy(alpha = 0.3f),
                                    radius = size.minDimension / 2,
                                    center = Offset(size.width / 2, size.height / 2)
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        badge.icon?.let {
                            Text(
                                text = it,
                                fontSize = 24.sp
                            )
                        } ?: badge.iconImage?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = badge.title,
                                tint = badge.color,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = badge.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isLocked) Color(0xFF757575) else Color(0xFF2C3E50),
                textAlign = TextAlign.Center,
                maxLines = 2
            )

            if (!isLocked && badge.dateEarned.isNotEmpty()) {
                Text(
                    text = badge.dateEarned,
                    fontSize = 10.sp,
                    color = Color(0xFF2C3E50).copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            } else if (isLocked && badge.requirement.isNotEmpty()) {
                Text(
                    text = badge.requirement,
                    fontSize = 9.sp,
                    color = Color(0xFF2C3E50).copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

data class Badge(
    val id: Int,
    val title: String,
    val description: String,
    val color: Color,
    val icon: String? = null,
    val iconImage: ImageVector? = null,
    val dateEarned: String = "",
    val requirement: String = ""
)

@Composable
fun Badges(prayerViewModel: PrayerViewModel) {
    val earnedBadges by prayerViewModel.earnedBadges.collectAsState()
    val upcomingBadges by prayerViewModel.upcomingBadges.collectAsState()
    
    PremiumBadgesCard(
        earnedBadges = earnedBadges,
        upcomingBadges = upcomingBadges
    )
}
