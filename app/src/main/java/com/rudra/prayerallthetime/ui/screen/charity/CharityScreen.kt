package com.rudra.prayerallthetime.ui.screen.charity

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rudra.prayerallthetime.data.local.CharityRecord
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharityScreen(
    navController: NavController,
    viewModel: CharityViewModel = hiltViewModel()
) {
    val totalCharity by viewModel.totalCharity.collectAsState()
    val monthlyCharity by viewModel.monthlyCharity.collectAsState()
    val records by viewModel.allRecords.collectAsState()
    
    var showZakatCalc by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Charity & Impact", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showZakatCalc = true }) {
                        Icon(Icons.Default.Calculate, contentDescription = "Zakat Calculator", tint = Color(0xFF2E7D32))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF2E7D32),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Log Charity")
            }
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
            item {
                ImpactSummaryCard(totalCharity, monthlyCharity)
            }

            item {
                Text(
                    text = "Recent Contributions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                )
            }

            if (records.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.VolunteerActivism, null, Modifier.size(64.dp), Color.LightGray)
                            Text("No contributions logged yet", color = Color.Gray)
                        }
                    }
                }
            } else {
                items(records) { record ->
                    CharityRecordItem(record) { viewModel.deleteRecord(record) }
                }
            }
        }
    }

    if (showZakatCalc) {
        ZakatCalculatorDialog(viewModel) { showZakatCalc = false }
    }

    if (showAddDialog) {
        AddCharityDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { amount, type, desc ->
                viewModel.logCharity(amount, type, desc)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun ImpactSummaryCard(total: Double, monthly: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF206224))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, null, tint = Color(0xFFFFD700), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Spiritual Impact", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.labelMedium)
            }
            
            Text(
                text = "$${String.format("%.2f", total)}",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Black
            )
            Text("Total contributions shared", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.1f))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("This Month", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    Text("$${String.format("%.2f", monthly)}", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.TrendingUp, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun CharityRecordItem(record: CharityRecord, onDelete: () -> Unit) {
    val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(record.date))
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when(record.type) {
                        "Zakat" -> Icons.Default.Calculate
                        "Fitrana" -> Icons.Default.BakeryDining
                        else -> Icons.Default.Favorite
                    },
                    contentDescription = null,
                    tint = Color(0xFF2E7D32)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(record.type, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Text(record.description ?: "No description", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                Text(dateStr, color = Color.LightGray, style = MaterialTheme.typography.labelSmall)
            }
            
            Text(
                text = "+$${record.amount}",
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun ZakatCalculatorDialog(viewModel: CharityViewModel, onDismiss: () -> Unit) {
    val assets by viewModel.zakatAssets.collectAsState()
    val liabilities by viewModel.zakatLiability.collectAsState()
    val result by viewModel.calculatedZakat.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Zakat Calculator", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = if (assets == 0.0) "" else assets.toString(),
                    onValueChange = { viewModel.updateAssets(it.toDoubleOrNull() ?: 0.0) },
                    label = { Text("Total Assets") },
                    prefix = { Text("$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = if (liabilities == 0.0) "" else liabilities.toString(),
                    onValueChange = { viewModel.updateLiabilities(it.toDoubleOrNull() ?: 0.0) },
                    label = { Text("Liabilities / Debts") },
                    prefix = { Text("$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE8F5E9))
                        .padding(16.dp)
                ) {
                    Column {
                        Text("Estimated Zakat (2.5%)", fontSize = 12.sp, color = Color(0xFF2E7D32))
                        Text("$${String.format("%.2f", result)}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))) {
                Text("Close")
            }
        }
    )
}

@Composable
fun AddCharityDialog(onDismiss: () -> Unit, onConfirm: (Double, String, String) -> Unit) {
    var amount by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Sadaqah") }
    var desc by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Contribution", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    prefix = { Text("$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                        Text("Type: $type")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        listOf("Sadaqah", "Zakat", "Fitrana").forEach { t ->
                            DropdownMenuItem(text = { Text(t) }, onClick = { type = t; expanded = false })
                        }
                    }
                }

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(amount.toDoubleOrNull() ?: 0.0, type, desc) },
                enabled = amount.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                Text("Log Contribution")
            }
        }
    )
}
