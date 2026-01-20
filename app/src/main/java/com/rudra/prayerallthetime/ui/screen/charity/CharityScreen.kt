package com.rudra.prayerallthetime.ui.screen.charity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharityScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Charity & Zakat", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFFF8F9FA)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF206224)),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Spiritual Impact", color = Color.White.copy(alpha = 0.7f))
                        Text("Act of Sadaqah", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "\"The believer's shade on the Day of Resurrection will be their charity.\"",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            item {
                Text("Track Your Contributions", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }

            item {
                CharityItem("Zakat Al-Mal", "Calculated based on wealth", Icons.Default.CardGiftcard)
                Spacer(modifier = Modifier.height(8.dp))
                CharityItem("Sadaqah Jariyah", "Ongoing charity acts", Icons.Default.Favorite)
            }
        }
    }
}

@Composable
fun CharityItem(title: String, desc: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    ListItem(
        headlineContent = { Text(title, fontWeight = FontWeight.Bold) },
        supportingContent = { Text(desc) },
        leadingContent = { 
            Box(
                modifier = Modifier.size(40.dp).background(Color(0xFFE8F5E9), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color(0xFF206224))
            }
        },
        modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
    )
}
