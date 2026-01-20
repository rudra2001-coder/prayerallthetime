package com.rudra.prayerallthetime.ui.screen.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerViewModel
import com.rudra.prayerallthetime.ui.theme.IslamicGold
import java.time.LocalDate
import java.time.YearMonth
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoField
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(navController: NavController, prayerViewModel: PrayerViewModel) {
    var selectedMonth by remember { mutableStateOf(YearMonth.now()) }
    val today = LocalDate.now()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Islamic Calendar", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFF0F4FF), Color.White)
                    )
                )
        ) {
            // Calendar Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { selectedMonth = selectedMonth.minusMonths(1) }) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Prev")
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = selectedMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + selectedMonth.year,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                    
                    // Hijri Month for the first day of selectedMonth
                    val firstDayHijri = HijrahDate.from(selectedMonth.atDay(1))
                    val hijriMonthName = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()).format(firstDayHijri)
                    Text(
                        text = hijriMonthName + " AH",
                        fontSize = 14.sp,
                        color = IslamicGold,
                        fontWeight = FontWeight.Medium
                    )
                }

                IconButton(onClick = { selectedMonth = selectedMonth.plusMonths(1) }) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Next")
                }
            }

            // Days of the Week
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Calendar Grid
            val daysInMonth = selectedMonth.lengthOfMonth()
            val firstDayOfWeek = selectedMonth.atDay(1).dayOfWeek.value % 7 // 0 for Sunday
            
            val days = mutableListOf<LocalDate?>()
            repeat(firstDayOfWeek) { days.add(null) }
            for (i in 1..daysInMonth) {
                days.add(selectedMonth.atDay(i))
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.padding(horizontal = 12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(days) { date ->
                    if (date != null) {
                        CalendarDayItem(
                            date = date,
                            isToday = date == today,
                            isSelected = false // Add selection logic if needed
                        )
                    } else {
                        Spacer(modifier = Modifier.aspectRatio(1f))
                    }
                }
            }
            
            // Special Days Legend
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Islamic Events", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    EventLegendItem(color = Color(0xFF4ECDC4), text = "Sunnah Fasting (Mon/Thu)")
                    EventLegendItem(color = IslamicGold, text = "White Days (13, 14, 15)")
                    EventLegendItem(color = Color(0xFFFF6B6B), text = "Important Islamic Dates")
                }
            }
        }
    }
}

@Composable
fun CalendarDayItem(
    date: LocalDate,
    isToday: Boolean,
    isSelected: Boolean
) {
    val hijriDate = HijrahDate.from(date)
    val hijriDay = hijriDate.get(ChronoField.DAY_OF_MONTH)
    
    // Check for special days (Sunnah fasting, White days etc)
    val dayOfWeek = date.dayOfWeek.value // 1 (Mon) to 7 (Sun)
    val isSunnahFast = dayOfWeek == 1 || dayOfWeek == 4
    val isWhiteDay = hijriDay in 13..15

    Box(
        modifier = Modifier
            .aspectRatio(0.8f)
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                when {
                    isToday -> IslamicGold.copy(alpha = 0.1f)
                    isSelected -> IslamicGold
                    else -> Color.Transparent
                }
            )
            .border(
                width = 1.dp,
                color = if (isToday) IslamicGold else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = date.dayOfMonth.toString(),
                fontSize = 16.sp,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFF2C3E50)
            )
            
            Text(
                text = hijriDay.toString(),
                fontSize = 11.sp,
                color = if (isSelected) Color.White.copy(alpha = 0.8f) else IslamicGold,
                fontWeight = FontWeight.Bold
            )
            
            // Indicators
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.padding(top = 2.dp)) {
                if (isSunnahFast) {
                    Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Color(0xFF4ECDC4)))
                }
                if (isWhiteDay) {
                    Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(IslamicGold))
                }
            }
        }
    }
}

@Composable
fun EventLegendItem(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 12.sp, color = Color.Gray)
    }
}
