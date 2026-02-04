package com.rudra.prayerallthetime.ui.screen.notifications

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.theme.IslamicGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController) {
    val notifications = remember {
        listOf(
            NotificationData(
                "Fajr Athan",
                "It's time for Fajr prayer. Start your day with the blessing of Allah.",
                "Today, 5:00 AM",
                false,
                NotificationType.PRAYER_TIME,
                0xFF4CAF50
            ),
            NotificationData(
                "Daily Dua Reminder",
                "Today's Special Dua: 'O Allah, I ask You for beneficial knowledge...'",
                "Today, 9:00 AM",
                false,
                NotificationType.REMINDER,
                0xFFD4AF37
            ),
            NotificationData(
                "Charity Milestone",
                "MashAllah! You've contributed to 3 Sadaqah acts this month.",
                "Yesterday, 8:30 PM",
                true,
                NotificationType.ACHIEVEMENT,
                0xFFFF6B6B
            ),
            NotificationData(
                "Prayer Times Sync",
                "Prayer times have been updated for your current location.",
                "Yesterday, 3:45 PM",
                true,
                NotificationType.UPDATE,
                0xFF2196F3
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
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
                .background(Color(0xFFF8F9FA))
        ) {
            NotificationStats(notifications)

            if (notifications.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            "Recent Updates",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                        )
                    }
                    items(notifications) { notification ->
                        NotificationItemCard(notification)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationStats(notifications: List<NotificationData>) {
    val unreadCount = notifications.count { !it.isRead }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1B4C))
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = if (unreadCount > 0) "You have $unreadCount new" else "You're all caught up",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
                Text(
                    text = "Alerts & Reminders",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(IslamicGold.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (unreadCount > 0) Icons.Default.NotificationsActive else Icons.Default.NotificationsNone,
                    contentDescription = null,
                    tint = IslamicGold,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun NotificationItemCard(notification: NotificationData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) Color.White else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (notification.isRead) 1.dp else 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(notification.iconColor).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getNotificationIcon(notification.type),
                    contentDescription = null,
                    tint = Color(notification.iconColor),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (notification.isRead) FontWeight.Bold else FontWeight.Black,
                        color = Color(0xFF2C3E50)
                    )
                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(IslamicGold)
                        )
                    }
                }
                
                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                
                Text(
                    text = notification.time,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.LightGray
                )
            }
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.NotificationsOff, null, Modifier.size(64.dp), Color.LightGray)
        Spacer(Modifier.height(16.dp))
        Text("All clear!", fontWeight = FontWeight.Bold, color = Color.Gray)
        Text("No new notifications for now.", color = Color.Gray, fontSize = 14.sp)
    }
}

private fun getNotificationIcon(type: NotificationType): ImageVector {
    return when (type) {
        NotificationType.PRAYER_TIME -> Icons.Default.Mosque
        NotificationType.REMINDER -> Icons.Default.MenuBook
        NotificationType.ACHIEVEMENT -> Icons.Default.EmojiEvents
        NotificationType.UPDATE -> Icons.Default.Sync
    }
}

enum class NotificationType {
    PRAYER_TIME, REMINDER, UPDATE, ACHIEVEMENT
}

data class NotificationData(
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean,
    val type: NotificationType,
    val iconColor: Long
)
