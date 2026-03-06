package com.rudra.prayerallthetime.ui.screen.explore

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.navigation.Screen
import com.rudra.prayerallthetime.ui.theme.IslamicGold

data class ExploreCategory(
    val title: String,
    val items: List<ExploreItem>
)

data class ExploreItem(
    val title: String,
    val icon: ImageVector,
    val route: String,
    val color: Color,
    val description: String = "",
    val isNew: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

    val categories = listOf(
        ExploreCategory(
            "Spiritual Core",
            listOf(
                ExploreItem("Prayer Times", Icons.Default.Mosque, Screen.Prayers.route, Color(0xFF4ECDC4), "Daily prayer schedule"),
                ExploreItem("Quran", Icons.Default.MenuBook, Screen.QuranHadith.route, Color(0xFF2C3E50), "Read the Holy Quran"),
                ExploreItem("Hadith", Icons.Default.HistoryEdu, Screen.Hadith.route, Color(0xFF8B4513), "Prophetic traditions"),
                ExploreItem("Qibla", Icons.Default.Explore, Screen.Qibla.route, Color(0xFFD4AF37), "Find Kaaba direction")
            )
        ),
        ExploreCategory(
            "Worship",
            listOf(
                ExploreItem("Tasbeeh", Icons.Default.Favorite, Screen.Tasbeeh.route, Color(0xFFFF6B6B), "Digital counter"),
                ExploreItem("Wudu Guide", Icons.Default.WaterDrop, Screen.Wudu.route, Color(0xFF45B7D1), "Step-by-step ablution"),
                ExploreItem("Tahajjud", Icons.Default.NightsStay, Screen.Tahajjud.route, Color(0xFF6A5ACD), "Night prayer guide"),
                ExploreItem("Charity", Icons.Default.VolunteerActivism, Screen.Charity.route, Color(0xFF4CAF50), "Track your Zakat & Sadaqah")
            )
        ),
        ExploreCategory(
            "Social & Analytics",
            listOf(
                ExploreItem("Analytics", Icons.Default.Analytics, Screen.Analytics.route, Color(0xFF607D8B), "Detailed spiritual stats"),
                ExploreItem("Family", Icons.Default.People, Screen.Family.route, Color(0xFF9C27B0), "Family prayer circle"),
                ExploreItem("Achievements", Icons.Default.EmojiEvents, Screen.Achievements.route, Color(0xFFFFC107), "Earn badges & rewards"),
                ExploreItem("Reports", Icons.Default.Assessment, Screen.Report.route, Color(0xFF009688), "Weekly summary")
            )
        ),
        ExploreCategory(
            "Ramadan Special",
            listOf(
                ExploreItem("Ramadan Hub", Icons.Default.Mosque, Screen.Ramadan.route, Color(0xFFE91E63), "Suhur, Iftar & Progress", isNew = true),
                ExploreItem("Taraweeh", Icons.Default.AutoAwesome, Screen.Taraweeh.route, Color(0xFF9C27B0), "Track Taraweeh prayers")
            )
        ),
        ExploreCategory(
            "Others",
            listOf(
                ExploreItem("Duas", Icons.Default.AutoAwesome, Screen.Duas.route, Color(0xFF4CAF50), "Daily Supplications"),
                ExploreItem("Habits", Icons.Default.PlaylistAddCheck, Screen.Habits.route, Color(0xFF2196F3), "Build Good Habits"),
                ExploreItem("Settings", Icons.Default.Settings, Screen.Settings.route, Color(0xFF546E7A), "Notification & theme")
            )
        )
    )

    val filteredCategories = categories.map { category ->
        category.copy(items = category.items.filter { 
            it.title.contains(searchQuery, ignoreCase = true) || 
            it.description.contains(searchQuery, ignoreCase = true) 
        })
    }.filter { it.items.isNotEmpty() }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(bottom = 8.dp)
            ) {
                TopAppBar(
                    title = { 
                        Text(
                            "More", 
                            style = MaterialTheme.typography.headlineMedium, 
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F1B4C)
                        ) 
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                    actions = {
                        IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Profile", tint = Color.Gray)
                        }
                    }
                )
                
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Search features, duas, or tools...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = IslamicGold) },
                    trailingIcon = { 
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = null)
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IslamicGold,
                        unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                    ),
                    singleLine = true
                )
            }
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
            filteredCategories.forEach { category ->
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = category.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50),
                        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                    )
                }

                items(category.items) { item ->
                    EnhancedExploreCard(item) {
                        navController.navigate(item.route)
                    }
                }
            }
            
            if (filteredCategories.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.SearchOff, null, Modifier.size(64.dp), Color.LightGray)
                            Text("No features found matching \"$searchQuery\"", textAlign = TextAlign.Center, color = Color.Gray)
                        }
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
fun EnhancedExploreCard(item: ExploreItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = item.color.copy(alpha = 0.2f)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (item.isNew) {
                Surface(
                    color = Color(0xFFFF6B6B),
                    shape = RoundedCornerShape(bottomStart = 12.dp, topEnd = 12.dp),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        "NEW",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(item.color.copy(alpha = 0.2f), item.color.copy(alpha = 0.05f))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = item.color,
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50),
                    lineHeight = 18.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    lineHeight = 14.sp,
                    maxLines = 2
                )
            }
        }
    }
}
