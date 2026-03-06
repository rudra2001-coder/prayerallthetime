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
import androidx.compose.material.icons.filled.*
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
import com.rudra.prayerallthetime.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.sqrt
import kotlin.math.tan

// Calculate distance to Mecca using Haversine formula
private fun calculateDistanceToMecca(latitude: Double, longitude: Double): String {
    val meccaLat = 21.4225
    val meccaLon = 39.8262
    
    val earthRadius = 6371.0 // km
    
    val lat1 = Math.toRadians(latitude)
    val lat2 = Math.toRadians(meccaLat)
    val dLat = Math.toRadians(meccaLat - latitude)
    val dLon = Math.toRadians(meccaLon - longitude)
    
    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(lat1) * cos(lat2) *
            sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * asin(sqrt(a))
    
    val distance = earthRadius * c
    return "${distance.toInt()} km"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QiblaScreen(navController: NavController, prayerViewModel: PrayerViewModel) {
    val context = LocalContext.current
    val qiblaDirection by prayerViewModel.qiblaDirection.collectAsState()
    var currentAzimuth by remember { mutableStateOf(0f) }
    var sensorAccuracy by remember { mutableStateOf(SensorManager.SENSOR_STATUS_ACCURACY_LOW) }
    var isLoading by remember { mutableStateOf(true) }
    
    // Get user location for distance calculation
    val userLocation by prayerViewModel.userLocation.collectAsState()
    val distanceToMecca = remember(userLocation) {
        userLocation?.let { (lat, lon) ->
            calculateDistanceToMecca(lat, lon)
        } ?: "Calculating..."
    }

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
                title = {
                    Text(
                        "Qibla Finder",
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
                actions = {
                    IconButton(
                        onClick = { prayerViewModel.refreshLocation() },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(IslamicGold.copy(alpha = 0.15f))
                            .padding(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh Location",
                            tint = IslamicGold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MidnightBlue)
            )
        },
        containerColor = MidnightBlue
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(MidnightBlue),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (isLoading) {
                LoadingView()
            } else {
                QiblaInfo(angleToQibla, qiblaDirection, distanceToMecca)
                
                CompassWidget(smoothAzimuth, qiblaDirection)
                
                AccuracyIndicator(sensorAccuracy)
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = IslamicGold,
                strokeWidth = 3.dp
            )
            Text(
                "Calibrating Compass...",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = TextSecondaryDark
                )
            )
        }
    }
}

@Composable
fun QiblaInfo(angleToQibla: Float, qiblaDirection: Float, distanceToMecca: String = "Calculating...") {
    val isAligned = angleToQibla.toInt() in 358..360 || angleToQibla.toInt() in 0..2
    
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Distance to Mecca
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = IslamicGold.copy(alpha = 0.15f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = IslamicGold,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Distance to Kaaba: $distanceToMecca",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = IslamicGold,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            "Align your phone",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = TextSecondaryDark
            )
        )
        Text(
            text = "${angleToQibla.toInt()}°",
            style = ExtendedTypography.statLarge.copy(
                fontWeight = FontWeight.Black,
                color = if (isAligned) SuccessColor else IslamicGold
            )
        )
        Text(
            text = "towards the Qibla",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = TextSecondaryDark
            )
        )
        
        if (isAligned) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SuccessColor.copy(alpha = 0.15f)
            ) {
                Text(
                    "Perfectly Aligned!",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = SuccessColor,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
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
                    colors = listOf(MidnightBlueCard, MidnightBlueLight),
                    radius = size.minDimension / 2
                ),
                style = Stroke(width = 8.dp.toPx())
            )
            
            // Draw cardinal points (static)
            val radius = size.minDimension / 2 - 20.dp.toPx()
            val centerX = size.width / 2
            val centerY = size.height / 2
            
            // North
            drawLine(
                color = ErrorColor,
                start = Offset(centerX, centerY - radius + 10.dp.toPx()),
                end = Offset(centerX, centerY - radius + 30.dp.toPx()),
                strokeWidth = 3.dp.toPx()
            )
            
            // Other cardinal points
            listOf(90f, 180f, 270f).forEach { angle ->
                rotate(angle, Offset(centerX, centerY)) {
                    drawLine(
                        color = TextTertiaryDark,
                        start = Offset(centerX, centerY - radius + 10.dp.toPx()),
                        end = Offset(centerX, centerY - radius + 20.dp.toPx()),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            }
        }
        
        // Rotating Compass Rose
        Box(
            modifier = Modifier
                .fillMaxSize(0.85f)
                .rotate(rotation)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val centerX = size.width / 2
                val centerY = size.height / 2
                
                // Draw N
                drawLine(
                    color = IslamicGold,
                    start = Offset(centerX, centerY),
                    end = Offset(centerX, 20.dp.toPx()),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
                
                // Draw tick marks
                for (i in 0 until 360 step 30) {
                    rotate(i.toFloat(), Offset(centerX, centerY)) {
                        drawLine(
                            color = TextSecondaryDark,
                            start = Offset(centerX, 10.dp.toPx()),
                            end = Offset(centerX, 20.dp.toPx()),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                }
            }
        }
        
        // Static Qibla Indicator
        QiblaArrow(qiblaAngle)
        
        // Center point
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(IslamicGold)
                .border(4.dp, MidnightBlue, CircleShape)
        )
    }
}

@Composable
fun QiblaArrow(qiblaAngle: Float) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize(0.9f)) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = size.minDimension / 2 - 10.dp.toPx()
            
            // Calculate Qibla position on circle
            val angleRad = Math.toRadians((qiblaAngle - 90).toDouble())
            val arrowX = centerX + (radius * cos(angleRad)).toFloat()
            val arrowY = centerY + (radius * sin(angleRad)).toFloat()
            
            // Draw arrow to Qibla
            drawLine(
                color = SuccessColor,
                start = Offset(centerX, centerY),
                end = Offset(arrowX, arrowY),
                strokeWidth = 4.dp.toPx(),
                cap = StrokeCap.Round
            )
            
            // Draw arrow head
            drawCircle(
                color = SuccessColor,
                radius = 8.dp.toPx(),
                center = Offset(arrowX, arrowY)
            )
        }
        
        // Kaaba icon at top
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 16.dp)
                .clip(CircleShape)
                .background(IslamicGold.copy(alpha = 0.2f))
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Navigation,
                contentDescription = "Qibla Direction",
                tint = IslamicGold,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun AccuracyIndicator(accuracy: Int) {
    val (color, text) = when (accuracy) {
        SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> SuccessColor to "High Accuracy"
        SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> WarningColor to "Medium Accuracy"
        SensorManager.SENSOR_STATUS_ACCURACY_LOW -> TextTertiaryDark to "Low Accuracy"
        else -> TextTertiaryDark to "Calibrating..."
    }
    
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = color,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
