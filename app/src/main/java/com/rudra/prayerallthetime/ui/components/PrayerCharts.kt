package com.rudra.prayerallthetime.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudra.prayerallthetime.ui.PrayerViewModel
import com.rudra.prayerallthetime.ui.TimeRange

@Composable
fun PremiumPrayerChartsCard(
    prayerViewModel: PrayerViewModel,
    timeRange: TimeRange = TimeRange.WEEKLY,
    onTimeRangeChange: (TimeRange) -> Unit = {},
    onViewDetails: () -> Unit = {}
) {
    val prayerStats by prayerViewModel.prayerStats.collectAsState()
    val completionRate by prayerViewModel.completionRate.collectAsState()

    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 12.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF0F4FF),
                            Color(0xFFE8F0FF)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            // Header with interactive controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Prayer Charts",
                            tint = Color(0xFF8B4513),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "📊 Prayer Analytics",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C3E50)
                        )
                    }
                    Text(
                        text = "Track your spiritual progress visually",
                        fontSize = 12.sp,
                        color = Color(0xFF2C3E50).copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                IconButton(
                    onClick = onViewDetails,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF8B4513).copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "View Details",
                        tint = Color(0xFF8B4513),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Time Range Selector
            TimeRangeSelector(
                selectedRange = timeRange,
                onRangeSelected = onTimeRangeChange
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Main Chart Visualization
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.5f))
                    .padding(16.dp)
            ) {
                // Simulated Bar Chart for Prayer Completion
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Y-axis labels
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("100%", fontSize = 10.sp, color = Color(0xFF2C3E50).copy(alpha = 0.6f))
                        Text("75%", fontSize = 10.sp, color = Color(0xFF2C3E50).copy(alpha = 0.6f))
                        Text("50%", fontSize = 10.sp, color = Color(0xFF2C3E50).copy(alpha = 0.6f))
                        Text("25%", fontSize = 10.sp, color = Color(0xFF2C3E50).copy(alpha = 0.6f))
                        Text("0%", fontSize = 10.sp, color = Color(0xFF2C3E50).copy(alpha = 0.6f))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Chart Bars
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha").forEach { prayerName ->
                            val percentage = prayerStats[prayerName] ?: 0f
                            ChartBar(
                                label = prayerName,
                                percentage = percentage,
                                color = when (prayerName) {
                                    "Fajr" -> Color(0xFF4ECDC4)
                                    "Dhuhr" -> Color(0xFF45B7D1)
                                    "Asr" -> Color(0xFF96CEB4)
                                    "Maghrib" -> Color(0xFFFFD93D)
                                    else -> Color(0xFFFF6B6B)
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // X-axis labels
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha").forEach { prayer ->
                            Text(
                                text = prayer,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF2C3E50),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats Summary
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF2C3E50).copy(alpha = 0.05f))
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PrayerStat(
                    title = "Avg. Completion",
                    value = "${(completionRate * 100).toInt()}%",
                    change = "",
                    icon = Icons.Default.TrendingUp,
                    color = Color(0xFF4ECDC4)
                )
                
                val bestPrayer = prayerStats.maxByOrNull { it.value }
                PrayerStat(
                    title = "Best Prayer",
                    value = bestPrayer?.key ?: "None",
                    subValue = if (bestPrayer != null) "${(bestPrayer.value * 100).toInt()}%" else "",
                    icon = Icons.Default.ShowChart,
                    color = Color(0xFFD4AF37)
                )
                PrayerStat(
                    title = "Today",
                    value = "${prayerViewModel.prayers.collectAsState().value.count { it.isPrayed }}",
                    subValue = "/ 5",
                    icon = Icons.Default.CalendarMonth,
                    color = Color(0xFF45B7D1)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Pie Chart Section
            Text(
                text = "Prayer Distribution",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2C3E50),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Simulated Pie Chart
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .drawBehind {
                            val total = prayerStats.values.sum()
                            if (total > 0) {
                                val colors = listOf(
                                    Color(0xFF4ECDC4),
                                    Color(0xFF45B7D1),
                                    Color(0xFF96CEB4),
                                    Color(0xFFFFD93D),
                                    Color(0xFFFF6B6B)
                                )
                                var startAngle = 0f
                                listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha").forEachIndexed { i, name ->
                                    val sweepAngle = ((prayerStats[name] ?: 0f) / total) * 360f
                                    if (sweepAngle > 0) {
                                        drawArc(
                                            color = colors[i % colors.size],
                                            startAngle = startAngle,
                                            sweepAngle = sweepAngle,
                                            useCenter = true
                                        )
                                        startAngle += sweepAngle
                                    }
                                }
                            } else {
                                drawCircle(color = Color.LightGray.copy(alpha = 0.3f))
                            }

                            // Draw center circle
                            drawCircle(
                                color = Color.White,
                                radius = size.minDimension * 0.3f,
                                center = Offset(size.width / 2, size.height / 2)
                            )
                        }
                )

                // Legend
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val total = prayerStats.values.sum()
                    listOf(
                        "Fajr" to Color(0xFF4ECDC4),
                        "Dhuhr" to Color(0xFF45B7D1),
                        "Asr" to Color(0xFF96CEB4),
                        "Maghrib" to Color(0xFFFFD93D),
                        "Isha" to Color(0xFFFF6B6B)
                    ).forEach { (name, color) ->
                        val percent = if (total > 0) (prayerStats[name] ?: 0f) / total else 0f
                        LegendRow(LegendItem("$name (${(percent * 100).toInt()}%)", color))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Insight Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF8B4513).copy(alpha = 0.1f))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PieChart,
                        contentDescription = "Insight",
                        tint = Color(0xFF8B4513),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Insight",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF8B4513)
                        )
                        val best = prayerStats.maxByOrNull { it.value }
                        val insightText = if (best != null && best.value > 0) {
                            "Your ${best.key} prayers are most consistent. Keep maintaining the discipline!"
                        } else {
                            "Start logging your prayers to see insights here."
                        }
                        Text(
                            text = insightText,
                            fontSize = 12.sp,
                            color = Color(0xFF2C3E50).copy(alpha = 0.8f),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TimeRangeSelector(
    selectedRange: TimeRange,
    onRangeSelected: (TimeRange) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2C3E50).copy(alpha = 0.05f))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TimeRange.values().forEach { range ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (range == selectedRange) Color(0xFF8B4513)
                        else Color.Transparent
                    )
                    .clickable { onRangeSelected(range) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = range.displayName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (range == selectedRange) Color.White else Color(0xFF2C3E50).copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun ChartBar(
    label: String,
    percentage: Float,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(40.dp)
    ) {
        // Bar
        Box(
            modifier = Modifier
                .width(24.dp)
                .height(100.dp * percentage.coerceAtLeast(0.05f))
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            color,
                            color.copy(alpha = 0.8f)
                        )
                    )
                )
                .drawBehind {
                    // Inner highlight
                    drawRect(
                        color = Color.White.copy(alpha = 0.3f),
                        topLeft = Offset(0f, 0f),
                        size = androidx.compose.ui.geometry.Size(size.width, 4f)
                    )
                }
        )

        // Percentage label
        Text(
            text = "${(percentage * 100).toInt()}%",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun PrayerStat(
    title: String,
    value: String,
    subValue: String = "",
    change: String = "",
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color.copy(alpha = 0.2f))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )
            if (subValue.isNotEmpty()) {
                Text(
                    text = " $subValue",
                    fontSize = 12.sp,
                    color = Color(0xFF2C3E50).copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
        if (change.isNotEmpty()) {
            Text(
                text = change,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (change.startsWith("+")) Color(0xFF4ECDC4) else Color(0xFFFF6B6B)
            )
        }
        Text(
            text = title,
            fontSize = 11.sp,
            color = Color(0xFF2C3E50).copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LegendRow(item: LegendItem) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(item.color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = item.label,
            fontSize = 12.sp,
            color = Color(0xFF2C3E50).copy(alpha = 0.8f)
        )
    }
}

data class LegendItem(
    val label: String,
    val color: Color
)

@Composable
fun PrayerCharts(prayerViewModel: PrayerViewModel) {
    PremiumPrayerChartsCard(prayerViewModel = prayerViewModel)
}
