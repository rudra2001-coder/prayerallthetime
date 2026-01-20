package com.rudra.prayerallthetime.ui.screen.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rudra.prayerallthetime.data.local.HabitEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(
    navController: NavController,
    viewModel: HabitsViewModel = hiltViewModel()
) {
    val habits by viewModel.habits.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Spiritual Goals", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF206224),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Goal")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA))
        ) {
            if (habits.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No goals set yet. Start your journey today!", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(habits) { habit ->
                        HabitCard(
                            habit = habit,
                            onIncrement = { viewModel.incrementProgress(habit.id) },
                            onDelete = { viewModel.deleteHabit(habit) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddHabitDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, goal, unit, category ->
                viewModel.addHabit(title, goal, unit, category)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun HabitCard(
    habit: HabitEntity,
    onIncrement: () -> Unit,
    onDelete: () -> Unit
) {
    val progress = if (habit.goalValue > 0) habit.currentProgress.toFloat() / habit.goalValue else 0f
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(habit.category.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF206224))
                    Text(habit.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = Color.LightGray)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(
                    progress = progress.coerceIn(0f, 1f),
                    modifier = Modifier.weight(1f).height(8.dp),
                    color = Color(0xFF206224),
                    trackColor = Color(0xFFE8F5E9)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text("${habit.currentProgress}/${habit.goalValue} ${habit.unit}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = onIncrement,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F8E9)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("+ Log Progress", color = Color(0xFF206224), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("min") }
    var category by remember { mutableStateOf("Quran") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Spiritual Goal", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Goal Title") })
                OutlinedTextField(
                    value = goal, 
                    onValueChange = { goal = it }, 
                    label = { Text("Daily Target") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                
                Text("Category", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Quran", "Dhikr", "Tahajjud").forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    if (title.isNotBlank() && goal.isNotEmpty()) {
                        onConfirm(title, goal.toIntOrNull() ?: 0, unit, category)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF206224))
            ) { Text("Create Goal") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
