package com.rudra.prayerallthetime.ui.screen.duas

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rudra.prayerallthetime.data.local.DuaEntity
import com.rudra.prayerallthetime.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuasScreen(
    navController: NavController,
    viewModel: DuasViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filteredDuas by viewModel.filteredDuas.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(MidnightBlue)) {
                TopAppBar(
                    title = { 
                        Text(
                            "Dua Library",
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
                    actions = {
                        IconButton(onClick = { viewModel.refreshDuas() }) {
                            Icon(
                                Icons.Default.Refresh, 
                                contentDescription = "Refresh",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MidnightBlue)
                )
                
                // Search Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { 
                        Text(
                            "Search Duas...",
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
                            IconButton(onClick = { viewModel.updateSearchQuery("") }) {
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
                        focusedBorderColor = IslamicGold,
                        unfocusedBorderColor = MidnightBlueLight,
                        focusedContainerColor = MidnightBlueCard,
                        unfocusedContainerColor = MidnightBlueCard,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                // Category Filter Chips
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { viewModel.selectCategory(null) },
                            label = { Text("All Duas") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = IslamicGold,
                                selectedLabelColor = Color.White,
                                containerColor = MidnightBlueCard
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedCategory == null,
                                borderColor = MidnightBlueLight
                            )
                        )
                    }
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { viewModel.selectCategory(category) },
                            label = { Text(category) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = IslamicGold,
                                selectedLabelColor = Color.White,
                                containerColor = MidnightBlueCard
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedCategory == category,
                                borderColor = MidnightBlueLight
                            )
                        )
                    }
                }
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
            if (isLoading && filteredDuas.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center), 
                    color = IslamicGold
                )
            } else if (filteredDuas.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.SearchOff, 
                        null, 
                        modifier = Modifier.size(64.dp), 
                        tint = TextTertiaryDark
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No Duas found",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = TextSecondaryDark
                        )
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = filteredDuas,
                        key = { it.id }
                    ) { dua ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) + 
                                    slideInVertically()
                        ) {
                            DuaCard(
                                dua = dua,
                                onFavoriteToggle = { viewModel.toggleFavorite(dua) }
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
        }
    }
}

@Composable
fun DuaCard(
    dua: DuaEntity,
    onFavoriteToggle: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = ShadowDark
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightBlueCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Badge
                Surface(
                    color = EmeraldGreen.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = dua.category.uppercase(),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen
                        )
                    )
                }
                
                IconButton(onClick = onFavoriteToggle) {
                    Icon(
                        imageVector = if (dua.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (dua.isFavorite) ErrorColor else TextTertiaryDark,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Title
            Text(
                text = dua.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimaryDark
                ),
                modifier = Modifier.padding(vertical = 12.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Arabic Text
            Text(
                text = dua.arabicText,
                style = ExtendedTypography.arabicMedium.copy(
                    textAlign = TextAlign.Right,
                    lineHeight = 42.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimaryDark
                ),
                modifier = Modifier.fillMaxWidth()
            )
            
            // Transliteration
            if (!dua.transliteration.isNullOrBlank()) {
                Text(
                    text = dua.transliteration,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = TextSecondaryDark
                    ),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MidnightBlueLight)
            Spacer(modifier = Modifier.height(12.dp))
            
            // Translation
            Text(
                text = dua.translation,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = TextSecondaryDark,
                    lineHeight = 26.sp
                )
            )

            // Bengali Translation
            if (!dua.translationBn.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = dua.translationBn,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = EmeraldGreen.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            
            // Reference
            if (!dua.reference.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Source: ${dua.reference}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextTertiaryDark
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { 
                        val textToCopy = "${dua.title}\n\n${dua.arabicText}\n\n${dua.translation}\n\n${dua.translationBn ?: ""}"
                        clipboardManager.setText(AnnotatedString(textToCopy)) 
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MidnightBlueLight)
                        .size(40.dp)
                ) {
                    Icon(
                        Icons.Default.ContentCopy, 
                        contentDescription = "Copy", 
                        tint = TextSecondaryDark, 
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                IconButton(
                    onClick = { /* Handle share */ },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MidnightBlueLight)
                        .size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Share, 
                        contentDescription = "Share", 
                        tint = TextSecondaryDark, 
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
