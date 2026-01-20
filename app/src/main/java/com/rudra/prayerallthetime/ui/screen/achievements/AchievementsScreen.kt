package com.rudra.prayerallthetime.ui.screen.achievements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsScreen(navController: NavController, prayerViewModel: PrayerViewModel) {
    val currentStreak by prayerViewModel.currentStreak.collectAsState()
    
    val achievements = listOf(
        AchievementItem("First Prayer", "Complete your first prayer", 1),
        AchievementItem("3 Day Streak", "Maintain a 3-day streak", 3),
        AchievementItem("Week Warrior", "Maintain a 7-day streak", 7),
        AchievementItem("Punctual", "Complete Fajr on time for 5 days", 5),
        AchievementItem("Community", "Add a family member", 1)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Achievements", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFFF8F9FA)),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(achievements) { achievement ->
                val isUnlocked = currentStreak >= achievement.requirement
                AchievementCard(achievement, isUnlocked)
            }
        }
    }
}

data class AchievementItem(val title: String, val desc: String, val requirement: Int)

@Composable
fun AchievementCard(achievement: AchievementItem, isUnlocked: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(60.dp).background(
                    if (isUnlocked) Color(0xFFD4AF37).copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.1f),
                    CircleShape
                ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isUnlocked) Icons.Default.EmojiEvents else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (isUnlocked) Color(0xFFD4AF37) else Color.Gray,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(achievement.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, textAlign = TextAlign.Center)
            Text(achievement.desc, fontSize = 10.sp, color = Color.Gray, textAlign = TextAlign.Center, lineHeight = 14.sp)
        }
    }
}
