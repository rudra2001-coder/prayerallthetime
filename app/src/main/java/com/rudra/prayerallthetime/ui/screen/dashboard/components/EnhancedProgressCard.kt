package com.rudra.prayerallthetime.ui.screen.dashboard.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudra.prayerallthetime.ui.theme.PrayerAllTheTimeTheme

@Composable
fun EnhancedProgressCard(
    completionPercentage: Float,
    completed: Int,
    total: Int,
    onAnalyticsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
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
                    progress = 1f,
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                    strokeWidth = 8.dp,
                    strokeCap = StrokeCap.Round
                )
                CircularProgressIndicator(
                    progress = completionPercentage,
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 8.dp,
                    strokeCap = StrokeCap.Round
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${(completionPercentage * 100).toInt()}%",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Done",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.width(24.dp))

            // Stats and Action
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Daily Progress",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                StatRow("Completed", "$completed", MaterialTheme.colorScheme.primary)
                StatRow("Remaining", "${total - completed}", Color.Gray)
                StatRow("Total Target", "$total", MaterialTheme.colorScheme.onPrimaryContainer)

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onAnalyticsClick,
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("View Analytics", fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
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
