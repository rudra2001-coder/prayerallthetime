package com.rudra.prayerallthetime.ui.screen.prayer

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudra.prayerallthetime.data.Prayer
import com.rudra.prayerallthetime.ui.theme.*
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
                        Text(
                            "Prayer Times", 
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold, 
                                color = Color.White
                            )
                        )
                        Text(
                            cityName, 
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = TextSecondaryDark
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Back", 
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { prayerViewModel.refreshLocation() }) {
                        Icon(
                            Icons.Default.Refresh, 
                            contentDescription = "Refresh", 
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Switch(
                        checked = useManual,
                        onCheckedChange = { prayerViewModel.setManualMode(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = IslamicGold,
                            checkedTrackColor = IslamicGold.copy(alpha = 0.5f),
                            uncheckedThumbColor = TextSecondaryDark,
                            uncheckedTrackColor = MidnightBlueLight
                        ),
                        thumbContent = {
                            Icon(
                                if (useManual) Icons.Default.Edit else Icons.Default.Cloud, 
                                null, 
                                tint = MidnightBlue,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MidnightBlue
                )
            )
        },
        containerColor = MidnightBlue
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MidnightBlue)
        ) {
            Column {
                AnimatedVisibility(visible = useManual) {
                    Surface(
                        color = IslamicGold.copy(alpha = 0.1f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.EditCalendar, 
                                contentDescription = null, 
                                tint = IslamicGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Manual Mode: Tap a prayer time to customize your schedule.", 
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextSecondaryDark
                                )
                            )
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
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(prayers, key = { it.name }) { prayer ->
                            val isNext = prayer.name == nextPrayerName
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + slideInVertically()
                            ) {
                                PremiumPrayerCard(
                                    prayer = prayer,
                                    isNext = isNext,
                                    isEditable = useManual && prayer.name != "Sunrise",
                                    onEditTime = { showTimePicker = prayer.name },
                                    onToggle = { prayerViewModel.togglePrayerState(it) }
                                )
                            }
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
    val prayerColor = getPrayerColor(prayer.name)
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "scale"
    )
    
    val cardBackground = if (isNext) {
        Brush.linearGradient(
            colors = listOf(IslamicGold, IslamicGoldDark),
            start = androidx.compose.ui.geometry.Offset(0f, 0f),
            end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        )
    } else {
        Brush.linearGradient(listOf(MidnightBlueCard, MidnightBlueLight))
    }
    
    val contentColor = if (isNext) Color.White else TextPrimaryDark
    val secondaryContentColor = if (isNext) Color.White.copy(alpha = 0.8f) else TextSecondaryDark

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .shadow(
                elevation = if (isNext) 12.dp else 6.dp, 
                shape = RoundedCornerShape(20.dp), 
                spotColor = if (isNext) IslamicGold else ShadowDark
            )
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = isEditable, 
                onClick = onEditTime
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .background(cardBackground)
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            if (isNext) Color.White.copy(alpha = 0.2f) 
                            else prayerColor.copy(alpha = 0.15f), 
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        prayerIcon(prayer.name),
                        contentDescription = prayer.name,
                        tint = if (isNext) Color.White else prayerColor,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        prayer.name, 
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = contentColor
                        )
                    )
                    if (isNext) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                "Next Prayer", 
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically, 
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = prayer.time,
                    style = ExtendedTypography.prayerTime.copy(
                        color = contentColor
                    )
                )
                if (prayer.name != "Sunrise") {
                    Checkbox(
                        checked = prayer.isPrayed,
                        onCheckedChange = { onToggle(prayer) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = if (isNext) Color.White else SuccessColor,
                            uncheckedColor = if (isNext) Color.White.copy(alpha = 0.7f) else TextTertiaryDark,
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
            color = MidnightBlueCard,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp), 
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Set Time for $prayerName", 
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )
                )
                Spacer(Modifier.height(24.dp))
                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = MidnightBlueLight,
                        clockDialSelectedContentColor = Color.White,
                        clockDialUnselectedContentColor = TextPrimaryDark,
                        selectorColor = IslamicGold,
                        periodSelectorBorderColor = IslamicGold,
                        periodSelectorSelectedContainerColor = IslamicGold,
                        periodSelectorUnselectedContainerColor = MidnightBlueLight,
                        periodSelectorSelectedContentColor = Color.White,
                        periodSelectorUnselectedContentColor = TextPrimaryDark,
                        timeSelectorSelectedContainerColor = IslamicGold.copy(alpha = 0.2f),
                        timeSelectorUnselectedContainerColor = MidnightBlueLight,
                        timeSelectorSelectedContentColor = IslamicGold,
                        timeSelectorUnselectedContentColor = TextPrimaryDark
                    )
                )
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            "Cancel", 
                            color = TextSecondaryDark
                        )
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
                        colors = ButtonDefaults.buttonColors(containerColor = IslamicGold),
                        shape = RoundedCornerShape(12.dp)
                    ) { 
                        Text(
                            "Confirm",
                            fontWeight = FontWeight.SemiBold
                        ) 
                    }
                }
            }
        }
    }
}
