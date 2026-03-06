package com.rudra.prayerallthetime.ui.screen.dashboard.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudra.prayerallthetime.ui.theme.*

@Composable
fun EnhancedProgressCard(
    completionPercentage: Float,
    completed: Int,
    total: Int,
    onAnalyticsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = completionPercentage,
        animationSpec = tween(durationMillis = 1000),
        label = "progress"
    )
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = ShadowDark
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightBlueCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular Progress Ring
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(100.dp)
            ) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.fillMaxSize(),
                    color = MidnightBlueLight,
                    strokeWidth = 10.dp,
                    strokeCap = StrokeCap.Round,
                )
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxSize(),
                    color = EmeraldGreen,
                    strokeWidth = 10.dp,
                    strokeCap = StrokeCap.Round,
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${(animatedProgress * 100).toInt()}%",
                        style = ExtendedTypography.statMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                    )
                    Text(
                        text = "Complete",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondaryDark
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(24.dp))

            // Stats and Action
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Daily Progress",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimaryDark
                    )
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                StatRow("Completed", "$completed", SuccessColor)
                Spacer(modifier = Modifier.height(4.dp))
                StatRow("Remaining", "${total - completed}", WarningColor)
                Spacer(modifier = Modifier.height(4.dp))
                StatRow("Total Target", "$total", TextPrimaryDark)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onAnalyticsClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldGreen.copy(alpha = 0.2f),
                        contentColor = EmeraldGreen
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        "View Analytics", 
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward, 
                        contentDescription = null, 
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label, 
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondaryDark
            )
        )
        Text(
            text = value, 
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold, 
                color = color
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EnhancedProgressCardPreview() {
    PrayerAllTheTimeTheme {
        EnhancedProgressCard(
            completionPercentage = 0.6f,
            completed = 3,
            total = 5,
            onAnalyticsClick = {}
        )
    }
}
