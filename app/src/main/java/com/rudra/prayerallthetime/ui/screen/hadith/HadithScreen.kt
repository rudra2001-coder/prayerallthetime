package com.rudra.prayerallthetime.ui.screen.hadith

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rudra.prayerallthetime.data.HadithItem

import com.rudra.prayerallthetime.ui.theme.IslamicGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HadithScreen(
    navController: NavController,
    viewModel: HadithViewModel = hiltViewModel()
) {
    val hadiths by viewModel.hadiths.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hadith Explorer", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.White, Color(0xFFF8F5F0))
                    )
                )
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search hadiths...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = IslamicGold,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            if (isLoading && hadiths.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = IslamicGold)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(hadiths.filter { 
                        it.hadithEnglish?.contains(searchQuery, ignoreCase = true) == true ||
                        it.hadithArabic?.contains(searchQuery, ignoreCase = true) == true
                    }) { hadith ->

                    }
                    
                    if (hadiths.isNotEmpty()) {
                        item {
                            Button(
                                onClick = { viewModel.loadMore() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = IslamicGold)
                            ) {
                                Text("Load More")
                            }
                        }
                    }
                }
            }
        }
    }
}
