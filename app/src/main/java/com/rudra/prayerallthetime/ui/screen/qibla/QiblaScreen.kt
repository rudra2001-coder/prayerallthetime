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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerViewModel
import com.rudra.prayerallthetime.ui.theme.IslamicGold
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QiblaScreen(navController: NavController, prayerViewModel: PrayerViewModel) {
    val context = LocalContext.current
    val qiblaDirection by prayerViewModel.qiblaDirection.collectAsState()
    var currentAzimuth by remember { mutableStateOf(0f) }
    var sensorAccuracy by remember { mutableStateOf(SensorManager.SENSOR_STATUS_ACCURACY_LOW) }
    var isLoading by remember { mutableStateOf(true) }

    val angleToQibla = remember(qiblaDirection, currentAzimuth) {
        ((qiblaDirection - currentAzimuth) % 360 + 360) % 360
    }

    val smoothAzimuth by animateFloatAsState(
        targetValue = -currentAzimuth,
        animationSpec = tween(durationMillis = 200, easing = LinearEasing),
        label = "compass_rotation"
    )

    // Sensor Logic
    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        var gravity: FloatArray? = null
        var geomagnetic: FloatArray? = null

        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    gravity = event.values.clone()
                } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    geomagnetic = event.values.clone()
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
                if (sensor.type == Sensor.TYPE_MAGNETIC_FIELD) sensorAccuracy = accuracy
            }
        }

        sensorManager.registerListener(sensorEventListener, gravitySensor, SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(sensorEventListener, magneticFieldSensor, SensorManager.SENSOR_DELAY_GAME)

        onDispose { sensorManager.unregisterListener(sensorEventListener) }
    }

    LaunchedEffect(qiblaDirection) {
        if (qiblaDirection != 0f) {
            delay(800) 
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Qibla Finder", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { prayerViewModel.refreshLocation() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh Location", tint = IslamicGold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F1B4C))
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (isLoading) {
                LoadingView()
            } else {
                QiblaInfo(angleToQibla, qiblaDirection)
                
                CompassWidget(smoothAzimuth, qiblaDirection)
                
                AccuracyIndicator(sensorAccuracy)
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            CircularProgressIndicator(color = IslamicGold)
            Text("Calibrating Compass...", fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun QiblaInfo(angleToQibla: Float, qiblaDirection: Float) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Align your phone", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        Text(
            text = "${angleToQibla.toInt()}°",
            fontSize = 52.sp,
            fontWeight = FontWeight.Black,
            color = if (angleToQibla.toInt() in 358..360 || angleToQibla.toInt() in 0..2) Color(0xFF4CAF50) else IslamicGold
        )
        Text(
            text = "towards the Qibla",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
    }
}

@Composable
fun CompassWidget(rotation: Float, qiblaAngle: Float) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(24.dp)
    ) {
        // Outer decorative ring
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White, Color(0xFFE0E0E0)),
                    radius = size.minDimension / 2
                ),
                style = Stroke(width = 8.dp.toPx())
            )
            
            // Draw cardinal points (static)
            val labels = listOf("N", "E", "S", "W")
            for (i in labels.indices) {
                val angle = i * 90.0
                val rad = Math.toRadians(angle - 90.0)
                val x = (size.width / 2) + (size.width / 2 - 40.dp.toPx()) * cos(rad).toFloat()
                val y = (size.height / 2) + (size.height / 2 - 40.dp.toPx()) * sin(rad).toFloat()
                // drawing simple markers instead of text for performance in Canvas
                drawCircle(
                    color = if (i == 0) Color.Red else Color.LightGray,
                    radius = 4.dp.toPx(),
                    center = Offset(x, y)
                )
            }
        }

        // Compass dial that rotates with phone
        Canvas(modifier = Modifier
            .fillMaxSize(0.85f)
            .rotate(rotation)) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = size.minDimension / 2

            // Draw Compass Plate
            drawCircle(
                color = Color.White,
                radius = radius,
                style = Stroke(width = 2.dp.toPx())
            )

            // Ticks
            for (i in 0 until 360 step 10) {
                val tickLength = if (i % 90 == 0) 15.dp.toPx() else 8.dp.toPx()
                val strokeWidth = if (i % 90 == 0) 3.dp.toPx() else 1.dp.toPx()
                val color = if (i == 0) Color.Red else Color.LightGray
                
                rotate(i.toFloat()) {
                    drawLine(
                        color = color,
                        start = Offset(centerX, 0f),
                        end = Offset(centerX, tickLength),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
            }
            
            // North indicator
            val path = Path().apply {
                moveTo(centerX, 10.dp.toPx())
                lineTo(centerX - 8.dp.toPx(), 30.dp.toPx())
                lineTo(centerX + 8.dp.toPx(), 30.dp.toPx())
                close()
            }
            drawPath(path, Color.Red)
        }

        // Qibla needle (rotates independently of compass dial to point to Qibla)
        Canvas(modifier = Modifier
            .fillMaxSize(0.95f)
            .rotate(rotation + qiblaAngle)) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            
            // Kaaba Direction Marker
            val needlePath = Path().apply {
                moveTo(centerX, 0f) // Tip
                lineTo(centerX - 15.dp.toPx(), 40.dp.toPx())
                lineTo(centerX + 15.dp.toPx(), 40.dp.toPx())
                close()
            }
            drawPath(needlePath, IslamicGold)
            
            // Kaaba Icon Placeholder (Square)
            drawRect(
                color = Color(0xFF212121),
                topLeft = Offset(centerX - 12.dp.toPx(), 5.dp.toPx()),
                size = Size(24.dp.toPx(), 24.dp.toPx())
            )
            // Gold line on Kaaba
            drawRect(
                color = IslamicGold,
                topLeft = Offset(centerX - 12.dp.toPx(), 12.dp.toPx()),
                size = Size(24.dp.toPx(), 4.dp.toPx())
            )
        }
        
        // Center centerpiece
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(Color(0xFF0F1B4C))
                .border(2.dp, Color.White, CircleShape)
        )
    }
}

@Composable
fun AccuracyIndicator(accuracy: Int) {
    val (text, color) = when (accuracy) {
        SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> "Accuracy: High" to Color(0xFF4CAF50)
        SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> "Accuracy: Medium" to IslamicGold
        else -> "Accuracy: Low - Please Calibrate" to Color.Red
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
                Text(text, fontWeight = FontWeight.Bold, color = Color(0xFF2C3E50), fontSize = 14.sp)
            }
            if (accuracy < SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Move your phone in a figure-8 motion to improve precision.",
                    fontSize = 12.sp, 
                    color = Color.Gray, 
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
