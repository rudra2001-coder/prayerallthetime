package com.rudra.prayerallthetime.ui.screen.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(navController: NavController, prayerViewModel: PrayerViewModel) {
    val prayerStats by prayerViewModel.prayerStats.collectAsState()
    val completionRate by prayerViewModel.completionRate.collectAsState()
    val currentStreak by prayerViewModel.currentStreak.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Spiritual Analytics", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Overall Score Card
            item {
                AnalyticsScoreCard(completionRate, currentStreak)
            }

            // Prayer Breakdown
            item {
                Text("Consistency by Prayer", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            items(prayerStats.toList()) { (name, score) ->
                ConsistencyItem(name, score)
            }
            
            // Insight Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.BarChart, contentDescription = null, tint = Color(0xFF1976D2))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Your consistency is highest for Maghrib. Try setting an earlier alarm for Fajr to improve your morning streak!",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF1976D2)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnalyticsScoreCard(rate: Float, streak: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF206224)),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Overall Progress", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                Text("${(rate * 100).toInt()}%", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Black)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Current Streak", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("$streak Days", color = Color(0xFFD4AF37), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFD4AF37), modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun ConsistencyItem(name: String, score: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(name, fontWeight = FontWeight.Bold)
                Text("${(score * 100).toInt()}%", color = Color(0xFF206224), fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = score,
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = Color(0xFF206224),
                trackColor = Color(0xFFE8F5E9)
            )
        }
    }
}
