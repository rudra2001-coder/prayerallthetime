package com.rudra.prayerallthetime.ui.screen.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.navigation.Screen

data class ExploreCategory(
    val title: String,
    val items: List<ExploreItem>
)

data class ExploreItem(
    val title: String,
    val icon: ImageVector,
    val route: String,
    val color: Color,
    val description: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(navController: NavController) {
    val categories = listOf(
        ExploreCategory(
            "Faith Essentials",
            listOf(
                ExploreItem("Prayer Times", Icons.Default.Mosque, Screen.Prayers.route, Color(0xFF4ECDC4), "Daily prayer schedule"),
                ExploreItem("Quran", Icons.Default.MenuBook, Screen.QuranHadith.route, Color(0xFF2C3E50), "Read the Holy Quran"),
                ExploreItem("Hadith", Icons.Default.HistoryEdu, Screen.Hadith.route, Color(0xFF8B4513), "Prophetic traditions"),
                ExploreItem("Dua Library", Icons.Default.MenuBook, Screen.Duas.route, Color(0xFFE91E63), "Collection of supplications"),
                ExploreItem("Qibla", Icons.Default.Explore, Screen.Qibla.route, Color(0xFFD4AF37), "Find Kaaba direction"),
                ExploreItem("Ramadan", Icons.Default.NightsStay, Screen.Ramadan.route, Color(0xFF6A5ACD), "Fasting & prayer guide")
            )
        ),
        ExploreCategory(
            "Worship & Practice",
            listOf(
                ExploreItem("Tasbeeh", Icons.Default.Favorite, Screen.Tasbeeh.route, Color(0xFFFF6B6B), "Digital counter"),
                ExploreItem("Wudu Guide", Icons.Default.WaterDrop, Screen.Wudu.route, Color(0xFF45B7D1), "Step-by-step ablution"),
                ExploreItem("Tahajjud", Icons.Default.NightsStay, Screen.Tahajjud.route, Color(0xFF3F51B5), "Night prayer guide"),
                ExploreItem("Faith Habits", Icons.Default.AssignmentTurnedIn, Screen.Habits.route, Color(0xFF4CAF50), "Track your daily deeds"),
                ExploreItem("Worship", Icons.Default.FormatListBulleted, Screen.Worship.route, Color(0xFF795548), "Daily worship checklist")
            )
        ),
        ExploreCategory(
            "Progress & Insights",
            listOf(
                ExploreItem("Analytics", Icons.Default.Analytics, Screen.Analytics.route, Color(0xFF607D8B), "Detailed spiritual stats"),
                ExploreItem("Report", Icons.Default.Assessment, Screen.Report.route, Color(0xFFFF9800), "Weekly summary"),
                ExploreItem("Achievements", Icons.Default.EmojiEvents, Screen.Achievements.route, Color(0xFFFFC107), "Earn badges & rewards"),
                ExploreItem("Streaks", Icons.Default.Bolt, Screen.Streaks.route, Color(0xFFFF5722), "Your consistency level")
            )
        ),
        ExploreCategory(
            "Community & Discovery",
            listOf(
                ExploreItem("Family", Icons.Default.People, Screen.Family.route, Color(0xFF9C27B0), "Family prayer circle"),
                ExploreItem("Charity", Icons.Default.VolunteerActivism, Screen.Charity.route, Color(0xFFF44336), "Track your Zakat & Sadaqah"),
                ExploreItem("Calendar", Icons.Default.CalendarMonth, Screen.Calendar.route, Color(0xFF8D6E63), "Islamic events"),
                ExploreItem("Nearby Mosques", Icons.Default.Place, Screen.NearbyMosques.route, Color(0xFF009688), "Find a place to pray")
            )
        ),
        ExploreCategory(
            "Settings & Profile",
            listOf(
                ExploreItem("Profile", Icons.Default.Person, Screen.Profile.route, Color(0xFF3F51B5), "Your account info"),
                ExploreItem("Notifications", Icons.Default.Notifications, Screen.Notifications.route, Color(0xFFFFC107), "Manage prayer alerts"),
                ExploreItem("Settings", Icons.Default.Settings, Screen.Settings.route, Color(0xFF546E7A), "App preferences")
            )
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explore", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA)),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            categories.forEach { category ->
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = category.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50),
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                items(category.items) { item ->
                    ExploreCard(item) {
                        navController.navigate(item.route)
                    }
                }
            }
            
            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ExploreCard(item: ExploreItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(item.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = item.color,
                    modifier = Modifier.size(22.dp)
                )
            }
            
            Column {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50),
                    maxLines = 1
                )
                
                Text(
                    text = item.description,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    lineHeight = 14.sp,
                    maxLines = 2
                )
            }
        }
    }
}
