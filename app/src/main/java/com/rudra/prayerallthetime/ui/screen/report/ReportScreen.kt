package com.rudra.prayerallthetime.ui.screen.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    navController: NavController,
    viewModel: ReportViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prayer Reports") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Custom Filter")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Report Type Selector
            ReportTypeTabs(
                selectedType = state.reportType,
                onTypeSelected = { viewModel.updateReport(it) }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    ReportSummaryCard(state)
                }



                if (state.reportType != ReportType.DAILY) {
                    item {
                        ProgressChartCard(state.dailyProgress)
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        CustomDateRangePicker(
            onDismiss = { showDatePicker = false },
            onDateRangeSelected = { start, end ->
                viewModel.updateReport(ReportType.CUSTOM, start, end)
                showDatePicker = false
            }
        )
    }
}

@Composable
fun ReportTypeTabs(
    selectedType: ReportType,
    onTypeSelected: (ReportType) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedType.ordinal,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        ReportType.entries.forEach { type ->
            Tab(
                selected = selectedType == type,
                onClick = { onTypeSelected(type) },
                text = {
                    Text(
                        text = type.name.lowercase().capitalize(),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
        }
    }
}

@Composable
fun ReportSummaryCard(state: ReportState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Summary",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryItem("Total Prayers", state.totalPrayers.toString())
                SummaryItem("Completion", "${(state.completionPercentage * 100).toInt()}%")
                val dateRange = if (state.startDate == state.endDate) {
                    state.startDate.format(DateTimeFormatter.ofPattern("MMM dd"))
                } else {
                    "${state.startDate.format(DateTimeFormatter.ofPattern("MMM dd"))} - ${state.endDate.format(DateTimeFormatter.ofPattern("MMM dd"))}"
                }
                SummaryItem("Period", dateRange)
            }
        }
    }
}

@Composable
fun SummaryItem(label: String, value: String) {
    Column {
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}


@Composable
fun ProgressChartCard(progress: List<Pair<String, Float>>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Daily Progress",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                progress.forEach { (day, value) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(value.coerceAtLeast(0.05f))
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                progress.forEach { (day, _) ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        fontSize = 10.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDateRangePicker(
    onDismiss: () -> Unit,
    onDateRangeSelected: (LocalDate, LocalDate) -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val startMillis = dateRangePickerState.selectedStartDateMillis
                    val endMillis = dateRangePickerState.selectedEndDateMillis
                    if (startMillis != null && endMillis != null) {
                        val startDate = Instant.ofEpochMilli(startMillis).atZone(ZoneId.systemDefault()).toLocalDate()
                        val endDate = Instant.ofEpochMilli(endMillis).atZone(ZoneId.systemDefault()).toLocalDate()
                        onDateRangeSelected(startDate, endDate)
                    }
                },
                enabled = dateRangePickerState.selectedEndDateMillis != null
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            modifier = Modifier.weight(1f)
        )
    }
}

fun getPrayerColor(name: String): Color {
    return when (name) {
        "Fajr" -> Color(0xFF4ECDC4)
        "Dhuhr" -> Color(0xFF45B7D1)
        "Asr" -> Color(0xFF96CEB4)
        "Maghrib" -> Color(0xFFFFD93D)
        else -> Color(0xFFFF6B6B)
    }
}
