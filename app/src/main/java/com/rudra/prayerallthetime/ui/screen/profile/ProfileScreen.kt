package com.rudra.prayerallthetime.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, prayerViewModel: PrayerViewModel) {
    val cityName by prayerViewModel.cityName.collectAsState()
    val currentStreak by prayerViewModel.currentStreak.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.Bold) },
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF206224)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👤", fontSize = 48.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Assalamu Alaikum", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(cityName, color = Color.Gray)
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    ProfileStatCard("Streak", "$currentStreak Days", Icons.Default.Whatshot)
                    ProfileStatCard("City", cityName, Icons.Default.LocationCity)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                ProfileMenuItem("Personal Information", Icons.Default.Person)
                ProfileMenuItem("Account Settings", Icons.Default.Settings)
                ProfileMenuItem("Spiritual Goals", Icons.Default.Flag)
                ProfileMenuItem("Privacy Policy", Icons.Default.PrivacyTip)
                ProfileMenuItem("Log Out", Icons.Default.Logout, Color.Red)
            }
        }
    }
}

@Composable
fun ProfileStatCard(label: String, value: String, icon: ImageVector) {
    Card(
        modifier = Modifier.width(150.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = Color(0xFF206224))
            Text(label, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun ProfileMenuItem(title: String, icon: ImageVector, tint: Color = Color.Black) {
    ListItem(
        headlineContent = { Text(title, color = tint) },
        leadingContent = { Icon(icon, contentDescription = null, tint = if (tint == Color.Red) Color.Red else Color(0xFF206224)) },
        trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
        modifier = Modifier.padding(vertical = 4.dp).clip(RoundedCornerShape(12.dp)).background(Color.White)
    )
}
