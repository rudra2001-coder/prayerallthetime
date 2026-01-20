package com.rudra.prayerallthetime.ui.screen.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudra.prayerallthetime.ui.theme.PrayerAllTheTimeTheme

@Composable
fun QuickInsights(
    insights: List<InsightData>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Quick Insights",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        insights.forEach { insight ->
            InsightCard(insight)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun InsightCard(insight: InsightData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = insight.backgroundColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = insight.icon,
                    contentDescription = null,
                    tint = insight.tintColor
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = insight.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.8f)
                )
                Text(
                    text = insight.description,
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.6f),
                    lineHeight = 16.sp
                )
            }
        }
    }
}

data class InsightData(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val backgroundColor: Color,
    val tintColor: Color
)

@Composable
fun SampleInsights() : List<InsightData> {
    return listOf(
        InsightData(
            title = "Performance Insight",
            description = "You're most consistent with Fajr prayers this week. Keep it up!",
            icon = Icons.Default.TipsAndUpdates,
            backgroundColor = Color(0xFFE3F2FD),
            tintColor = Color(0xFF1976D2)
        ),
        InsightData(
            title = "Improvement Suggestion",
            description = "Try to log your Dhuhr prayer immediately after performing it to maintain accuracy.",
            icon = Icons.Default.Lightbulb,
            backgroundColor = Color(0xFFFFF3E0),
            tintColor = Color(0xFFF57C00)
        )
    )
}

@Preview(showBackground = true)
@Composable
fun QuickInsightsPreview() {
    PrayerAllTheTimeTheme {
        QuickInsights(insights = SampleInsights())
    }
}

@Preview(showBackground = true)
@Composable
fun InsightCardPreview() {
    PrayerAllTheTimeTheme {
        InsightCard(
            insight = InsightData(
                title = "Test Insight",
                description = "This is a test insight description.",
                icon = Icons.Default.TipsAndUpdates,
                backgroundColor = Color(0xFFE3F2FD),
                tintColor = Color(0xFF1976D2)
            )
        )
    }
}
