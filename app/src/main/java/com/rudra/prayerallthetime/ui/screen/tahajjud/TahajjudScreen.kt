package com.rudra.prayerallthetime.ui.screen.tahajjud

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerViewModel
import com.rudra.prayerallthetime.ui.theme.IslamicGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TahajjudScreen(navController: NavController, prayerViewModel: PrayerViewModel) {
    val tahajjudTime by prayerViewModel.tahajjudTimeStr.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tahajjud Guide", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF0F1B4C), Color(0xFF1A237E))
                    )
                )
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Hero Time Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Nightlight, contentDescription = null, tint = IslamicGold, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Best Time for Tahajjud",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                            Text(
                                text = tahajjudTime,
                                color = IslamicGold,
                                fontSize = 42.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "(Last third of the night)",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // What is Tahajjud
                item {
                    TahajjudInfoCard(
                        title = "What is Tahajjud?",
                        description = "Tahajjud is a voluntary prayer performed at night after waking up from sleep. it is one of the most virtuous voluntary prayers.",
                        icon = Icons.Default.Info
                    )
                }

                // How to Perform
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "How to Perform",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF2C3E50)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            val steps = listOf(
                                "1. Sleep for a portion of the night after Isha.",
                                "2. Wake up in the last third of the night.",
                                "3. Perform Wudu and make Niyyah (Intention).",
                                "4. Pray in sets of 2 Rakat (minimum 2, maximum 12).",
                                "5. End with Witr prayer if not performed yet.",
                                "6. Spend time in Dua and Dhikr."
                            )
                            steps.forEach { step ->
                                Text(
                                    text = step,
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                // Virtues
                item {
                    TahajjudInfoCard(
                        title = "Virtue of Tahajjud",
                        description = "\"Our Lord descends every night to the lowest heaven when the last third of the night remains and says: 'Who will call upon Me, that I may answer him?'\" (Bukhari)",
                        icon = Icons.Default.AutoAwesome
                    )
                }
            }
        }
    }
}

@Composable
fun TahajjudInfoCard(title: String, description: String, icon: ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(20.dp)) {
            Icon(icon, contentDescription = null, tint = IslamicGold, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = description, color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp, lineHeight = 20.sp)
            }
        }
    }
}
