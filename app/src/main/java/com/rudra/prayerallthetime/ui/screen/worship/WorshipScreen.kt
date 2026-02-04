package com.rudra.prayerallthetime.ui.screen.worship

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerViewModel
import com.rudra.prayerallthetime.ui.theme.IslamicGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorshipScreen(navController: NavController, prayerViewModel: PrayerViewModel) {
    val totalCount by prayerViewModel.tasbeehCount.collectAsState()
    val dhikrSet = listOf("SubhanAllah", "Alhamdulillah", "Allahu Akbar")
    var currentDhikrIndex by remember { mutableStateOf(0) }
    var sessionCount by remember { mutableStateOf(0) }
    val goal = 33

    fun resetSession() {
        sessionCount = 0
    }

    fun nextDhikr() {
        currentDhikrIndex = (currentDhikrIndex + 1) % dhikrSet.size
        resetSession()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tasbeeh Counter", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF8F9FA))
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            // Main Counter Display
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(250.dp)
                    .shadow(elevation = 20.dp, shape = CircleShape, spotColor = IslamicGold)
            ) {
                val progress = animateFloatAsState(targetValue = sessionCount.toFloat() / goal, label = "").value

                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color.White,
                    )
                    drawArc(
                        color = IslamicGold.copy(alpha = 0.3f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 30f)
                    )
                    drawArc(
                        color = IslamicGold,
                        startAngle = -90f,
                        sweepAngle = 360 * progress,
                        useCenter = false,
                        style = Stroke(width = 30f, cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = dhikrSet[currentDhikrIndex],
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                    Text(
                        text = "$sessionCount",
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF2C3E50)
                    )
                    Text(
                        text = "/ $goal",
                        fontSize = 20.sp,
                        color = Color.Gray
                    )
                }
            }

            // Clicker Button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(IslamicGold, Color(0xFFFFD93D))
                        )
                    )
                    .clickable {
                        if (sessionCount < goal) {
                            sessionCount++
                            prayerViewModel.incrementTasbeeh()
                        } else {
                            nextDhikr()
                        }
                    }
                    .padding(16.dp)

            ) {
                Icon(
                    imageVector = if (sessionCount == goal) Icons.Default.Refresh else Icons.Default.Add,
                    contentDescription = "Increment",
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }

            // Dhikr selection and total count
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    dhikrSet.forEachIndexed { index, dhikr ->
                        Chip(
                            label = dhikr,
                            isSelected = index == currentDhikrIndex,
                            onClick = {
                                currentDhikrIndex = index
                                resetSession()
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text("Total Dhikr: $totalCount", style = MaterialTheme.typography.bodyLarge)
                Button(onClick = {
                    prayerViewModel.resetTasbeeh()
                    resetSession()
                }) {
                    Text("Reset Total Count")
                }
            }
        }
    }
}

@Composable
fun Chip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) IslamicGold else Color.White,
        contentColor = if (isSelected) Color.White else Color.Gray,
        modifier = Modifier.clickable(onClick = onClick),
        tonalElevation = 2.dp
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
