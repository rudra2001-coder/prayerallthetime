package com.rudra.prayerallthetime.ui.screen.qibla

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QiblaScreen(navController: NavController, prayerViewModel: PrayerViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val qiblaDirection by prayerViewModel.qiblaDirection.collectAsState()
    var currentAzimuth by remember { mutableStateOf(0f) }
    var sensorAccuracy by remember { mutableStateOf(SensorManager.SENSOR_STATUS_ACCURACY_HIGH) }
    var isLoading by remember { mutableStateOf(true) }

    // Card rotation states
    var rotationAngle by remember { mutableStateOf(0f) }
    val animatedRotation by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "card_rotation"
    )

    // Calculate the difference between current direction and Qibla
    val angleToQibla = remember(qiblaDirection, currentAzimuth) {
        ((qiblaDirection - currentAzimuth) % 360 + 360) % 360
    }

    // Smooth compass rotation
    val smoothAzimuth by animateFloatAsState(
        targetValue = -currentAzimuth,
        animationSpec = tween(durationMillis = 150, easing = LinearEasing),
        label = "compass_rotation"
    )

    // Smooth Qibla needle rotation
    val smoothQiblaAngle by animateFloatAsState(
        targetValue = angleToQibla,
        animationSpec = tween(durationMillis = 200, easing = LinearEasing),
        label = "qibla_needle"
    )

    // Manage loading state
    LaunchedEffect(qiblaDirection) {
        if (qiblaDirection != 0f) {
            delay(1000) // Give sensors a moment to settle
            isLoading = false
            // Start subtle card rotation animation
            rotationAngle = 360f
        }
    }

    // Sensor Logic
    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        var gravity: FloatArray? = null
        var geomagnetic: FloatArray? = null

        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    gravity = event.values
                }
                if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    geomagnetic = event.values
                    sensorAccuracy = event.accuracy
                }

                if (gravity != null && geomagnetic != null) {
                    val r = FloatArray(9)
                    val i = FloatArray(9)
                    if (SensorManager.getRotationMatrix(r, i, gravity, geomagnetic)) {
                        val orientation = FloatArray(3)
                        SensorManager.getOrientation(r, orientation)
                        val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                        currentAzimuth = (azimuth + 360) % 360
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                if (sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    sensorAccuracy = accuracy
                }
            }
        }

        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(sensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_UI)

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Qibla Compass",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Recalibrating compass...")
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Recalibrate",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                LoadingView()
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxSize()
                ) {
                    // Header Stats
                    HeaderStats(
                        sensorAccuracy = sensorAccuracy,
                        angleToQibla = angleToQibla
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Main Compass Card
                    ElevatedCard(
                        modifier = Modifier
                            .size(340.dp)
                            .rotate(animatedRotation),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        ),
                        shape = CircleShape,
                        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Outer decorative ring
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                // Draw gradient ring
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFF8B4513).copy(alpha = 0.1f),
                                            Color(0xFFD4AF37).copy(alpha = 0.3f)
                                        ),
                                        center = center,
                                        radius = size.minDimension / 2
                                    )
                                )

                                // Draw compass ticks
                                for (angle in 0..359 step 15) {
                                    val isCardinal = angle % 90 == 0
                                    val isMajor = angle % 45 == 0
                                    val lineLength = when {
                                        isCardinal -> 30f
                                        isMajor -> 20f
                                        else -> 12f
                                    }
                                    val strokeWidth = when {
                                        isCardinal -> 3.dp.toPx()
                                        isMajor -> 2.dp.toPx()
                                        else -> 1.dp.toPx()
                                    }

                                    val radian = Math.toRadians(angle.toDouble())
                                    val startX = (size.width / 2) + (size.width / 2 - 20) * cos(radian).toFloat()
                                    val startY = (size.height / 2) + (size.height / 2 - 20) * sin(radian).toFloat()
                                    val endX = (size.width / 2) + (size.width / 2 - 20 - lineLength) * cos(radian).toFloat()
                                    val endY = (size.height / 2) + (size.height / 2 - 20 - lineLength) * sin(radian).toFloat()

                                    drawLine(
                                        color = when {
                                            isCardinal -> Color(0xFF4CAF50)
                                            isMajor -> Color(0xFF206224)
                                            else -> Color(0xFFD4AF37).copy(alpha = 0.7f)
                                        },
                                        start = Offset(startX, startY),
                                        end = Offset(endX, endY),
                                        strokeWidth = strokeWidth,
                                        cap = StrokeCap.Round
                                    )
                                }
                            }

                            // Rotating compass inner
                            Box(
                                modifier = Modifier
                                    .size(280.dp)
                                    .rotate(smoothAzimuth),
                                contentAlignment = Alignment.Center
                            ) {
                                // Cardinal directions
                                DirectionLabel("N", Alignment.TopCenter, MaterialTheme.colorScheme.primary, true)
                                DirectionLabel("S", Alignment.BottomCenter, Color(0xFFD32F2F), true)
                                DirectionLabel("E", Alignment.CenterEnd, Color(0xFF4CAF50), false)
                                DirectionLabel("W", Alignment.CenterStart, Color(0xFF2196F3), false)
                            }

                            // Qibla Needle with Kaaba symbol
                            Box(
                                modifier = Modifier
                                    .size(300.dp)
                                    .rotate(smoothQiblaAngle),
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    val centerX = size.width / 2
                                    val centerY = size.height / 2

                                    // Main needle
                                    drawLine(
                                        color = Color(0xFFD32F2F),
                                        start = Offset(centerX, centerY),
                                        end = Offset(centerX, centerY - 100f),
                                        strokeWidth = 8.dp.toPx(),
                                        cap = StrokeCap.Round
                                    )

                                    // Kaaba symbol (simplified cube)
                                    drawRect(
                                        color = Color(0xFF212121),
                                        topLeft = Offset(centerX - 12.dp.toPx(), centerY - 115f),
                                        size = androidx.compose.ui.geometry.Size(24.dp.toPx(), 24.dp.toPx())
                                    )

                                    // Kaaba decoration
                                    drawRect(
                                        color = Color(0xFFD4AF37),
                                        topLeft = Offset(centerX - 10.dp.toPx(), centerY - 113f),
                                        size = androidx.compose.ui.geometry.Size(20.dp.toPx(), 4.dp.toPx())
                                    )
                                }
                            }

                            // Center indicator
                            Box(
                                modifier = Modifier.size(24.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.White)
                                )
                            }
                        }
                    }

                    // Bottom Info Cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        InfoCard(
                            title = "Qibla Angle",
                            value = "${qiblaDirection.toInt()}°",
                            icon = Icons.Default.LocationOn,
                            iconColor = Color(0xFF4CAF50),
                            modifier = Modifier.weight(1f)
                        )

                        InfoCard(
                            title = "Heading",
                            value = "${currentAzimuth.toInt()}°",
                            icon = Icons.Default.Refresh,
                            iconColor = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Accuracy Indicator
                    AccuracyIndicator(sensorAccuracy)
                }
            }
        }
    }
}

@Composable
fun DirectionLabel(text: String, alignment: Alignment, color: Color, isCardinal: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize().padding(if (isCardinal) 32.dp else 24.dp),
        contentAlignment = alignment
    ) {
        Text(
            text = text,
            color = color,
            fontSize = if (isCardinal) 28.sp else 22.sp,
            fontWeight = if (isCardinal) FontWeight.ExtraBold else FontWeight.Bold
        )
    }
}

@Composable
fun AccuracyIndicator(accuracy: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Compass Accuracy",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = when (accuracy) {
                        SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> "Excellent"
                        SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> "Good"
                        else -> "Low - Calibrate"
                    },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (accuracy >= SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM) Color(0xFF4CAF50) else Color.Red
                )
            }

            LinearProgressIndicator(
                progress = {
                    when (accuracy) {
                        SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> 1f
                        SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> 0.6f
                        else -> 0.3f
                    }
                },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                color = if (accuracy >= SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM) Color(0xFF4CAF50) else Color.Red
            )

            Text(
                text = "Move your phone in a figure-8 motion to improve accuracy",
                fontSize = 11.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun LoadingView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text("Calibrating Compass...", fontWeight = FontWeight.Medium)
    }
}

@Composable
fun HeaderStats(sensorAccuracy: Int, angleToQibla: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            title = "Angle to Qibla",
            value = "${angleToQibla.toInt()}°",
            color = Color(0xFF4CAF50),
            modifier = Modifier.weight(1f)
        )

        StatCard(
            title = "Sensor Status",
            value = if (sensorAccuracy >= SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM) "Stable" else "Unstable",
            color = if (sensorAccuracy >= SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM) Color(0xFF4CAF50) else Color.Red,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 12.sp, color = Color.Gray)
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun InfoCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, iconColor: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor)
            Column {
                Text(text = title, fontSize = 12.sp, color = Color.Gray)
                Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
