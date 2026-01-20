package com.rudra.prayerallthetime.ui.screen.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudra.prayerallthetime.data.local.DuaEntity
import com.rudra.prayerallthetime.data.local.HabitEntity

@Composable
fun GoalOfTheDayCard(
    habit: HabitEntity?,
    onActionClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF4ECDC4).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFF4ECDC4)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Goal of the Day",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4ECDC4)
                )
                Text(
                    text = habit?.title ?: "Complete your prayers",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF2C3E50)
                )
                Text(
                    text = if (habit != null) "${habit.currentProgress}/${habit.goalValue} ${habit.unit}" else "Maintain your streak",
                    fontSize = 13.sp,
                    color = Color(0xFF2C3E50).copy(alpha = 0.6f)
                )
            }
            
            IconButton(
                onClick = { habit?.let { onActionClick(it.id) } },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if ((habit?.currentProgress ?: 0) >= (habit?.goalValue ?: 1)) Color(0xFF4ECDC4) else Color(0xFFF0F0F0)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Complete",
                    tint = if ((habit?.currentProgress ?: 0) >= (habit?.goalValue ?: 1)) Color.White else Color(0xFFBDBDBD)
                )
            }
        }
    }
}

@Composable
fun DuaOfTheDaySection(
    dua: DuaEntity?,
    modifier: Modifier = Modifier
) {
    if (dua == null) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Book,
                contentDescription = null,
                tint = Color(0xFF8B4513),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Dua of the Day",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFCFB)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.FormatQuote,
                    contentDescription = null,
                    tint = Color(0xFFD4AF37).copy(alpha = 0.3f),
                    modifier = Modifier.size(40.dp)
                )
                
                Text(
                    text = dua.arabicText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF2C3E50),
                    lineHeight = 32.sp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                
                Text(
                    text = dua.translation,
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF2C3E50).copy(alpha = 0.8f),
                    lineHeight = 20.sp
                )
                
                if (dua.reference != null) {
                    Text(
                        text = "— ${dua.reference}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFD4AF37),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}
