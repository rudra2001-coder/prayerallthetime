package com.rudra.prayerallthetime.ui.screen.prayer

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudra.prayerallthetime.data.Prayer
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Prayer Times", fontWeight = FontWeight.Black, fontSize = 22.sp, color = Color(0xFF206224))
                        Text(cityName, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF206224))
                    }
                },
                actions = {
                    Box(modifier = Modifier.padding(end = 12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Manual", style = MaterialTheme.typography.labelSmall, color = if (useManual) Color(0xFF206224) else Color.Gray)
                            Spacer(modifier = Modifier.width(4.dp))
                            Switch(
                                checked = useManual,
                                onCheckedChange = { prayerViewModel.setManualMode(it) },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD4AF37), checkedTrackColor = Color(0xFF206224))
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Brush.verticalGradient(listOf(Color.White, Color(0xFFE8F5E9))))
        ) {
            Column {
                // Header Banner
                AnimatedVisibility(visible = useManual) {
                    Surface(
                        color = Color(0xFFFFF9C4),
                        modifier = Modifier.fillMaxWidth().shadow(4.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.EditCalendar, contentDescription = null, tint = Color(0xFFFBC02D))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Manual Mode: Tap a time below to customize your prayer schedule.", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                if (prayers.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF206224))
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(prayers) { prayer ->
                            val isNext = prayer.name == nextPrayerName
                            EnhancedPrayerCard(
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
            onDismiss = { showTimePicker = null },
            onTimeSelected = { newTime ->
                prayerViewModel.updateManualTime(showTimePicker!!, newTime)
                showTimePicker = null
            }
        )
    }
}

@Composable
fun EnhancedPrayerCard(
    prayer: Prayer,
    isNext: Boolean,
    isEditable: Boolean,
    onEditTime: () -> Unit,
    onToggle: (Prayer) -> Unit
) {
    val cardBg = if (isNext) Color(0xFF206224) else Color.White
    val contentColor = if (isNext) Color.White else Color.Black

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (isNext) 12.dp else 2.dp, RoundedCornerShape(20.dp))
            .clickable(enabled = isEditable) { onEditTime() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg)
    ) {
        Row(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(if (isNext) Color(0xFFD4AF37) else Color(0xFFF1F8E9), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(prayer.emoji, fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(prayer.name, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = contentColor)
                    if (isNext) {
                        Text("UPCOMING", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))
                    } else {
                        Text("Daily Walk", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = prayer.time,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isNext) Color.White else Color(0xFF206224)
                    )
                    if (isEditable) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp), tint = if (isNext) Color(0xFFD4AF37) else Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                IconButton(
                    onClick = { onToggle(prayer) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (prayer.isPrayed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = if (isNext) Color(0xFFD4AF37) else Color(0xFF206224)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualTimePickerDialog(
    onDismiss: () -> Unit,
    onTimeSelected: (String) -> Unit
) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    cal.set(Calendar.MINUTE, timePickerState.minute)
                    val formatter = java.text.SimpleDateFormat("hh:mm a", Locale.getDefault())
                    onTimeSelected(formatter.format(cal.time))
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF206224))
            ) { Text("Set Prayer Time") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel", color = Color.Gray) } },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Adjust Prayer Time", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
                TimePicker(state = timePickerState, colors = TimePickerDefaults.colors(selectorColor = Color(0xFF206224)))
            }
        }
    )
}
