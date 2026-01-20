package com.rudra.prayerallthetime.ui.screen.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavController) {
    var hasUnreadNotifications by remember { mutableStateOf(true) }

    val notifications = listOf(
        NotificationData(
            title = "Fajr Athan",
            message = "It's time for Fajr prayer in Gurgaon. Don't miss your morning prayers.",
            time = "Today, 5:00 AM",
            isRead = false,
            type = NotificationType.PRAYER_TIME,
            iconColor = 0xFF4CAF50
        ),
        NotificationData(
            title = "Daily Quran Verse",
            message = "Read Surah Al-Kahf today. Friday reminder for spiritual growth.",
            time = "Today, 9:00 AM",
            isRead = true,
            type = NotificationType.REMINDER,
            iconColor = 0xFF2196F3
        ),
        NotificationData(
            title = "Prayer Times Updated",
            message = "Prayer times have been updated for your current location in Dhaka.",
            time = "Yesterday, 3:45 PM",
            isRead = true,
            type = NotificationType.UPDATE,
            iconColor = 0xFF9C27B0
        ),
        NotificationData(
            title = "Dhuhr Prayer",
            message = "Dhuhr prayer time starting in 15 minutes. Prepare for prayer.",
            time = "Yesterday, 12:45 PM",
            isRead = true,
            type = NotificationType.PRAYER_TIME,
            iconColor = 0xFF4CAF50
        ),
        NotificationData(
            title = "Tasbih Counter",
            message = "You've completed 500 tasbih today. Keep up the good work!",
            time = "Oct 25, 8:30 PM",
            isRead = true,
            type = NotificationType.ACHIEVEMENT,
            iconColor = 0xFFFF9800
        ),
        NotificationData(
            title = "App Update Available",
            message = "New version 2.1.0 is available with improved features.",
            time = "Oct 24, 11:00 AM",
            isRead = true,
            type = NotificationType.UPDATE,
            iconColor = 0xFF9C27B0
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Notifications",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            "${notifications.count { !it.isRead }} unread",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (hasUnreadNotifications) {
                        BadgedBox(
                            badge = {
                                Badge {
                                    Text(notifications.count { !it.isRead }.toString())
                                }
                            }
                        ) {
                            IconButton(onClick = { /* Mark all as read */ }) {
                                Icon(
                                    Icons.Outlined.NotificationsActive,
                                    contentDescription = "Notifications"
                                )
                            }
                        }
                    } else {
                        IconButton(onClick = { /* Toggle notifications */ }) {
                            Icon(
                                Icons.Outlined.NotificationsNone,
                                contentDescription = "No notifications"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            // Stats Section
            StatsSection(notifications)

            // Notifications List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notifications) { notification ->
                    NotificationItem(notification = notification)
                }
            }
        }
    }
}

@Composable
fun StatsSection(notifications: List<NotificationData>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatItem(
                count = notifications.count { !it.isRead },
                label = "Unread",
                color = MaterialTheme.colorScheme.primary
            )
            Divider(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
            StatItem(
                count = notifications.count { it.type == NotificationType.PRAYER_TIME },
                label = "Prayer Times",
                color = Color(0xFF4CAF50)
            )
            Divider(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
            StatItem(
                count = notifications.size,
                label = "Total",
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun StatItem(count: Int, label: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$count",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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

@Composable
fun NotificationItem(notification: NotificationData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
            }
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (notification.isRead) 0.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Unread indicator
            if (!notification.isRead) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.width(12.dp))
            } else {
                Spacer(modifier = Modifier.width(20.dp))
            }

            // Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(notification.iconColor).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getIconForType(notification.type),
                    contentDescription = null,
                    tint = Color(notification.iconColor),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontWeight = if (notification.isRead) FontWeight.Medium else FontWeight.Bold,
                        fontSize = 16.sp,
                        color = if (notification.isRead) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = notification.time,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.message,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Type badge
                Text(
                    text = notification.type.name.replace("_", " "),
                    fontSize = 10.sp,
                    color = Color(notification.iconColor),
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(notification.iconColor).copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun getIconForType(type: NotificationType): ImageVector {
    return when (type) {
        NotificationType.PRAYER_TIME -> Icons.Default.Schedule
        NotificationType.REMINDER -> Icons.Default.Notifications
        NotificationType.UPDATE -> Icons.Default.Notifications
        NotificationType.ACHIEVEMENT -> Icons.Default.Notifications
    }
}

// Optional: Empty State
@Composable
fun EmptyNotificationsState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.NotificationsOff,
            contentDescription = "No notifications",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Notifications",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "You're all caught up! Check back later for prayer reminders and updates.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}