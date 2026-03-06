package com.rudra.prayerallthetime.ui.screen.quran

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rudra.prayerallthetime.data.SurahSummary
import com.rudra.prayerallthetime.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahListScreen(
    navController: NavController,
    viewModel: SurahListViewModel = hiltViewModel()
) {
    val surahList by viewModel.surahList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredList = surahList.filter {
        it.englishName.contains(searchQuery, ignoreCase = true) ||
        it.number.toString() == searchQuery
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(MidnightBlue)) {
                TopAppBar(
                    title = { 
                        Text(
                            "Holy Quran",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
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
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MidnightBlue)
                )
                
                // Search Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { 
                        Text(
                            "Search Surah...",
                            color = TextSecondaryDark
                        ) 
                    },
                    leadingIcon = { 
                        Icon(
                            Icons.Default.Search, 
                            contentDescription = null, 
                            tint = IslamicGold
                        ) 
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    Icons.Default.Close, 
                                    contentDescription = null,
                                    tint = TextSecondaryDark
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = IslamicGold,
                        unfocusedBorderColor = MidnightBlueLight,
                        cursorColor = IslamicGold,
                        focusedContainerColor = MidnightBlueCard,
                        unfocusedContainerColor = MidnightBlueCard
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        containerColor = MidnightBlue
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MidnightBlue)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(), 
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = IslamicGold,
                        strokeWidth = 3.dp
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = filteredList,
                        key = { it.number }
                    ) { surah ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) + 
                                    slideInVertically()
                        ) {
                            SurahListItem(surah) {
                                navController.navigate("surah_detail/${surah.number}")
                            }
                        }
                    }
                    
                    if (filteredList.isEmpty() && searchQuery.isNotEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp),
                                        tint = TextTertiaryDark
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        "No Surah found",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = TextSecondaryDark
                                        )
                                    )
                                }
                            }
                        }
                    }
                    
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
        }
    }
}

@Composable
fun SurahListItem(surah: SurahSummary, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp, 
                shape = RoundedCornerShape(20.dp), 
                spotColor = ShadowDark
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightBlueCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Surah Number
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(IslamicGold.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = surah.number.toString(),
                    color = IslamicGold,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Surah Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = surah.englishName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimaryDark
                    )
                )
                Text(
                    text = "${surah.revelationType} • ${surah.numberOfAyahs} Ayahs",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondaryDark,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            
            // Arabic Name
            Text(
                text = surah.name,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = IslamicGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            )
        }
    }
}
