package com.rudra.prayerallthetime.ui.screen.calendar

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val today = LocalDate.now()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Islamic Calendar", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        selectedMonth = YearMonth.now()
                        selectedDate = LocalDate.now()
                    }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Today")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            item {
                CalendarHeader(
                    selectedMonth = selectedMonth,
                    onMonthChange = { selectedMonth = it }
                )
            }

            item {
                CalendarGrid(
                    selectedMonth = selectedMonth,
                    selectedDate = selectedDate,
                    today = today,
                    onDateSelected = { selectedDate = it }
                )
            }

            item {
                DateDetailCard(selectedDate)
            }

            item {
                SpecialDaysLegend()
            }

            item {
                UpcomingEventsSection(selectedMonth)
            }
        }
    }
}

@Composable
fun CalendarHeader(
    selectedMonth: YearMonth,
    onMonthChange: (YearMonth) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onMonthChange(selectedMonth.minusMonths(1)) },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface, CircleShape).size(40.dp)
            ) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "Prev")
            }
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = selectedMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + selectedMonth.year,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                val firstDayHijri = HijrahDate.from(selectedMonth.atDay(1))
                val hijriMonthName = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()).format(firstDayHijri)
                Text(
                    text = "$hijriMonthName AH",
                    style = MaterialTheme.typography.bodyMedium,
                    color = IslamicGold,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(
                onClick = { onMonthChange(selectedMonth.plusMonths(1)) },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface, CircleShape).size(40.dp)
            ) {
                Icon(Icons.Default.ChevronRight, contentDescription = "Next")
            }
        }
    }
}

@Composable
fun CalendarGrid(
    selectedMonth: YearMonth,
    selectedDate: LocalDate,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        // Days of the Week Header
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT").forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        val daysInMonth = selectedMonth.lengthOfMonth()
        val firstDayOfWeek = selectedMonth.atDay(1).dayOfWeek.value % 7 
        
        val days = mutableListOf<LocalDate?>()
        repeat(firstDayOfWeek) { days.add(null) }
        for (i in 1..daysInMonth) {
            days.add(selectedMonth.atDay(i))
        }

        val rows = (days.size + 6) / 7
        for (row in 0 until rows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val index = row * 7 + col
                    val date = if (index < days.size) days[index] else null
                    Box(modifier = Modifier.weight(1f)) {
                        if (date != null) {
                            CalendarDayItem(
                                date = date,
                                isToday = date == today,
                                isSelected = date == selectedDate,
                                onClick = { onDateSelected(date) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarDayItem(
    date: LocalDate,
    isToday: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val hijriDate = HijrahDate.from(date)
    val hijriDay = hijriDate.get(ChronoField.DAY_OF_MONTH)
    val hijriMonth = hijriDate.get(ChronoField.MONTH_OF_YEAR)
    
    val isSunnahFast = date.dayOfWeek.value == 1 || date.dayOfWeek.value == 4
    val isWhiteDay = hijriDay in 13..15
    val isFriday = date.dayOfWeek.value == 5
    val isRamadan = hijriMonth == 9

    Column(
        modifier = Modifier
            .aspectRatio(0.85f)
            .padding(2.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .background(
                when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    isToday -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                    else -> Color.Transparent
                }
            )
            .border(
                width = 1.dp,
                color = if (isToday && !isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
        
        Text(
            text = hijriDay.toString(),
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else IslamicGold,
            fontWeight = FontWeight.Bold
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(top = 2.dp)
        ) {
            if (isSunnahFast) DotIndicator(Color(0xFF4ECDC4))
            if (isWhiteDay) DotIndicator(IslamicGold)
            if (isRamadan) DotIndicator(Color(0xFFE91E63))
            if (isFriday) DotIndicator(Color(0xFF4CAF50))
        }
    }
}

@Composable
fun DotIndicator(color: Color) {
    Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(color))
}

@Composable
fun DateDetailCard(date: LocalDate) {
    val hijriDate = HijrahDate.from(date)
    val dayName = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val hijriMonthName = DateTimeFormatter.ofPattern("MMMM", Locale.getDefault()).format(hijriDate)
    val hijriYear = hijriDate.get(ChronoField.YEAR)
    val hijriDay = hijriDate.get(ChronoField.DAY_OF_MONTH)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(IslamicGold.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = hijriDay.toString(), style = MaterialTheme.typography.titleLarge, color = IslamicGold, fontWeight = FontWeight.Bold)
                    Text(text = "HIJRI", style = MaterialTheme.typography.labelSmall, color = IslamicGold)
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(text = dayName, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                Text(
                    text = "$hijriDay $hijriMonthName $hijriYear AH",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = DateTimeFormatter.ofPattern("dd MMMM yyyy").format(date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun SpecialDaysLegend() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Legend", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    LegendItem(Color(0xFF4ECDC4), "Sunnah Fast")
                    LegendItem(IslamicGold, "White Days")
                }
                Column(modifier = Modifier.weight(1f)) {
                    LegendItem(Color(0xFFE91E63), "Ramadan")
                    LegendItem(Color(0xFF4CAF50), "Friday")
                }
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun UpcomingEventsSection(selectedMonth: YearMonth) {
    val events = remember(selectedMonth) { getIslamicEvents(selectedMonth) }
    
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Upcoming Events",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        if (events.isEmpty()) {
            Text(
                text = "No major Islamic events this month",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            events.forEach { event ->
                EventItem(event)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun EventItem(event: IslamicEvent) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Event, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
                Text(text = event.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text(
                    text = "${event.date.dayOfMonth} ${event.date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())} (${event.hijriDate})",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

data class IslamicEvent(
    val name: String,
    val date: LocalDate,
    val hijriDate: String
)

fun getIslamicEvents(month: YearMonth): List<IslamicEvent> {
    val events = mutableListOf<IslamicEvent>()
    for (day in 1..month.lengthOfMonth()) {
        val date = month.atDay(day)
        val hijri = HijrahDate.from(date)
        val hDay = hijri.get(ChronoField.DAY_OF_MONTH)
        val hMonth = hijri.get(ChronoField.MONTH_OF_YEAR)
        
        val eventName = when {
            hMonth == 1 && hDay == 1 -> "Islamic New Year"
            hMonth == 1 && hDay == 10 -> "Ashura"
            hMonth == 3 && hDay == 12 -> "Mawlid al-Nabi"
            hMonth == 7 && hDay == 27 -> "Isra' and Mi'raj"
            hMonth == 8 && hDay == 15 -> "Mid-Sha'ban"
            hMonth == 9 && hDay == 1 -> "Ramadan Begins"
            hMonth == 9 && hDay == 27 -> "Laylat al-Qadr"
            hMonth == 10 && hDay == 1 -> "Eid al-Fitr"
            hMonth == 12 && hDay == 9 -> "Day of Arafah"
            hMonth == 12 && hDay == 10 -> "Eid al-Adha"
            else -> null
        }
        
        if (eventName != null) {
            events.add(IslamicEvent(eventName, date, "$hDay ${getHijriMonthName(hMonth)}"))
        }
    }
    return events
}

fun getHijriMonthName(month: Int): String {
    return arrayOf(
        "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
        "Jumada al-Ula", "Jumada al-Akhira", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
    )[month - 1]
}
