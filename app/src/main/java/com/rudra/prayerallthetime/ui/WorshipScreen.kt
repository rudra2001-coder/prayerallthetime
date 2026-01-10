package com.rudra.prayerallthetime.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorshipScreen(navController: NavController, prayerViewModel: PrayerViewModel) {
    val count by prayerViewModel.tasbeehCount.collectAsState()
    var dhikrText by remember { mutableStateOf("SubhanAllah") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tasbeeh Counter") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = dhikrText, style = TextStyle(fontSize = 32.sp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "$count", style = TextStyle(fontSize = 72.sp))
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {
                prayerViewModel.incrementTasbeeh()
                if ((count + 1) % 33 == 0) {
                    when (dhikrText) {
                        "SubhanAllah" -> dhikrText = "Alhamdulillah"
                        "Alhamdulillah" -> dhikrText = "Allahu Akbar"
                        "Allahu Akbar" -> dhikrText = "SubhanAllah"
                    }
                }
            }) {
                Text("Count", style = TextStyle(fontSize = 24.sp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(onClick = { prayerViewModel.resetTasbeeh(); dhikrText = "SubhanAllah" }) {
                    Text("Reset")
                }
            }
        }
    }
}
