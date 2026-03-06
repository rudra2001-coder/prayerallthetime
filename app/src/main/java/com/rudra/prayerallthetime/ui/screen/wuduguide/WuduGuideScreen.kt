package com.rudra.prayerallthetime.ui.screen.wuduguide

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.theme.*

data class WuduStep(
    val title: String,
    val description: String,
    val imageResId: Int? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WuduGuideScreen(navController: NavController) {
    val steps = listOf(
        WuduStep("1. Niyyah (Intention)", "Make the intention in your heart to perform Wudu for the sake of Allah."),
        WuduStep("2. Bismillah", "Say 'Bismillah' (In the name of Allah) before starting."),
        WuduStep("3. Wash Hands", "Wash your hands up to the wrists three times, making sure to clean between the fingers."),
        WuduStep("4. Rinse Mouth", "Rinse your mouth three times, using your right hand to put water in."),
        WuduStep("5. Clean Nose", "Sniff water into your nostrils and blow it out three times, using your left hand to clear the nose."),
        WuduStep("6. Wash Face", "Wash your entire face three times, from the hairline to the chin and from ear to ear."),
        WuduStep("7. Wash Arms", "Wash your right arm up to and including the elbow three times, then do the same for the left arm."),
        WuduStep("8. Wipe Head", "Wipe your wet hands over your head once, starting from the front to the back and back to the front."),
        WuduStep("9. Clean Ears", "Wipe the inside of your ears with your index fingers and the back with your thumbs once."),
        WuduStep("10. Wash Feet", "Wash your right foot up to and including the ankle three times, cleaning between the toes. Then do the same for the left foot."),
        WuduStep("11. Dua After Wudu", "Recite: 'Ash-hadu alla ilaha illallah wahdahu la sharika lah, wa ash-hadu anna Muhammadan 'abduhu wa rasuluh.'")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Wudu Guide",
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
        },
        containerColor = MidnightBlue
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MidnightBlue)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(steps) { index, step ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) + 
                                slideInVertically()
                    ) {
                        WuduStepCard(index + 1, step)
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(20.dp),
                                spotColor = ShadowDark
                            ),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = IslamicGold.copy(alpha = 0.1f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Important Note",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = IslamicGold
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Ensure that water touches every part of the required areas. If any part remains dry, the Wudu is incomplete.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextSecondaryDark,
                                    lineHeight = 24.sp
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun WuduStepCard(number: Int, step: WuduStep) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = ShadowDark
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightBlueCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(CharityColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = step.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimaryDark
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = step.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondaryDark,
                        lineHeight = 24.sp
                    )
                )
            }
        }
    }
}
