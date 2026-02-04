package com.rudra.prayerallthetime.ui.screen.prayer

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudra.prayerallthetime.data.Prayer
import com.rudra.prayerallthetime.ui.theme.IslamicGold
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayersScreen(
    prayerViewModel: PrayerViewModel,
    navController: NavController
) {
    val prayers by prayerViewModel.prayers.collectAsState()
    val useManual by prayerViewModel.useManualPrayerTimes.collectAsState()
    val nextPrayerName by prayerViewModel.nextPrayerName.collectAsState()
    val cityName by prayerViewModel.cityName.collectAsState()

    var showTimePicker by remember { mutableStateOf<String?>(null) }

    // Observe refresh signal from settings
    LaunchedEffect(navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("refresh_prayer_times")) {
        if (navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("refresh_prayer_times") == true) {
            prayerViewModel.refreshLocation()
            navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>("refresh_prayer_times")
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Prayer Times", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = Color.White)
                        Text(cityName, style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.8f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { prayerViewModel.refreshLocation() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Switch(
                        checked = useManual,
                        onCheckedChange = { prayerViewModel.setManualMode(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = IslamicGold,
                            checkedTrackColor = Color.White.copy(alpha = 0.5f),
                            uncheckedThumbColor = Color.LightGray,
                            uncheckedTrackColor = Color.White.copy(alpha = 0.5f)
                        ),
                        thumbContent = {
                            Icon(if (useManual) Icons.Default.Edit else Icons.Default.Cloud, null, tint = Color(0xFF0F1B4C))
                        }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF0F1B4C))
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Brush.verticalGradient(listOf(Color(0xFFF8F9FA), Color.White)))
        ) {
            Column {
                AnimatedVisibility(visible = useManual) {
                    Surface(
                        color = IslamicGold.copy(alpha = 0.1f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.EditCalendar, contentDescription = null, tint = IslamicGold)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Manual Mode: Tap a prayer time to customize your schedule.", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                if (prayers.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = IslamicGold)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(prayers) { prayer ->
                            val isNext = prayer.name == nextPrayerName
                            PremiumPrayerCard(
                                prayer = prayer,
                                isNext = isNext,
                                isEditable = useManual && prayer.name != "Sunrise",
                                onEditTime = { showTimePicker = prayer.name },
                                onToggle = { prayerViewModel.togglePrayerState(it) }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(24.dp)) }
                    }
                }
            }
        }
    }

    if (showTimePicker != null) {
        ManualTimePickerDialog(
            prayerName = showTimePicker!!,
            onDismiss = { showTimePicker = null },
            onTimeSelected = {
                prayerViewModel.updateManualTime(showTimePicker!!, it)
                showTimePicker = null
            }
        )
    }
}

fun prayerIcon(prayerName: String): ImageVector {
    return when (prayerName) {
        "Fajr" -> Icons.Default.Brightness5
        "Sunrise" -> Icons.Default.WbSunny
        "Dhuhr" -> Icons.Default.WbSunny
        "Asr" -> Icons.Default.Brightness6
        "Maghrib" -> Icons.Default.Brightness4
        "Isha" -> Icons.Default.NightsStay
        else -> Icons.Default.Timelapse
    }
}

@Composable
fun PremiumPrayerCard(
    prayer: Prayer,
    isNext: Boolean,
    isEditable: Boolean,
    onEditTime: () -> Unit,
    onToggle: (Prayer) -> Unit
) {
    val cardBrush = if (isNext) Brush.linearGradient(listOf(IslamicGold, Color(0xFFFFD93D))) else Brush.linearGradient(listOf(Color.White, Color.White))
    val contentColor = if (isNext) Color.White else Color(0xFF2C3E50)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isNext) 12.dp else 4.dp, RoundedCornerShape(24.dp), spotColor = if (isNext) IslamicGold else Color.Gray)
            .clip(RoundedCornerShape(24.dp))
            .clickable(enabled = isEditable, onClick = onEditTime),
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(
            modifier = Modifier.background(cardBrush).padding(20.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(if (isNext) Color.White.copy(alpha = 0.2f) else IslamicGold.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        prayerIcon(prayer.name),
                        contentDescription = prayer.name,
                        tint = if (isNext) Color.White else IslamicGold,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(prayer.name, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = contentColor)
                    if (isNext) {
                        Text("Next Prayer", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.9f))
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = prayer.time,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = contentColor
                )
                if (prayer.name != "Sunrise") {
                    Checkbox(
                        checked = prayer.isPrayed,
                        onCheckedChange = { onToggle(prayer) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = if (isNext) Color.White else IslamicGold,
                            uncheckedColor = if (isNext) Color.White.copy(alpha = 0.7f) else IslamicGold.copy(alpha = 0.7f),
                            checkmarkColor = if (isNext) IslamicGold else Color.White
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualTimePickerDialog(
    prayerName: String,
    onDismiss: () -> Unit,
    onTimeSelected: (String) -> Unit
) {
    val timePickerState = rememberTimePickerState()

    AlertDialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Set Time for $prayerName", 
                    style = MaterialTheme.typography.titleLarge, 
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )
                Spacer(Modifier.height(24.dp))
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = Color(0xFFF8F9FA),
                        clockDialSelectedContentColor = Color.White,
                        clockDialUnselectedContentColor = Color(0xFF2C3E50),
                        selectorColor = IslamicGold,
                        periodSelectorBorderColor = IslamicGold,
                        periodSelectorSelectedContainerColor = IslamicGold,
                        periodSelectorUnselectedContainerColor = Color.White,
                        periodSelectorSelectedContentColor = Color.White,
                        periodSelectorUnselectedContentColor = Color(0xFF2C3E50),
                        timeSelectorSelectedContainerColor = IslamicGold.copy(alpha = 0.2f),
                        timeSelectorUnselectedContainerColor = Color(0xFFF8F9FA),
                        timeSelectorSelectedContentColor = IslamicGold,
                        timeSelectorUnselectedContentColor = Color(0xFF2C3E50)
                    )
                )
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color.Gray)
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val cal = Calendar.getInstance()
                            cal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                            cal.set(Calendar.MINUTE, timePickerState.minute)
                            val formatter = java.text.SimpleDateFormat("hh:mm a", Locale.getDefault())
                            onTimeSelected(formatter.format(cal.time))
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = IslamicGold)
                    ) { Text("Confirm") }
                }
            }
        }
    }
}
