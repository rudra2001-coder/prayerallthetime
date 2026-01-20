package com.rudra.prayerallthetime.ui.screen.qibla

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
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
    var sensorAccuracy by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    val locationAvailable = qiblaDirection != 0f

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
        }
    }

    // Sensor Logic
    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        val rotationMatrix = FloatArray(9)
        val orientation = FloatArray(3)
        val accelerometerReading = FloatArray(3)
        val magnetometerReading = FloatArray(3)

        var hasAccelerometer = false
        var hasMagnetometer = false

        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_ROTATION_VECTOR -> {
                        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                        SensorManager.getOrientation(rotationMatrix, orientation)
                        val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                        currentAzimuth = (currentAzimuth * 0.7f + azimuth * 0.3f)
                    }
                    Sensor.TYPE_ACCELEROMETER -> {
                        System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
                        hasAccelerometer = true
                    }
                    Sensor.TYPE_MAGNETIC_FIELD -> {
                        System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
                        hasMagnetometer = true
                    }
                }

                if (!hasAccelerometer || !hasMagnetometer) return
                
                if (rotationVectorSensor == null) {
                    SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
                    SensorManager.getOrientation(rotationMatrix, orientation)
                    val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                    currentAzimuth = (currentAzimuth * 0.7f + azimuth * 0.3f)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                sensorAccuracy = accuracy
            }
        }

        listOfNotNull(rotationVectorSensor, accelerometerSensor, magnetometerSensor).forEach { sensor ->
            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI)
        }

        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Qibla Compass", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { scope.launch { snackbarHostState.showSnackbar("Recalibrating...") } }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Recalibrate")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceVariant))),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    CircularProgressIndicator()
                    Text("Calibrating sensors...")
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Accuracy Indicator
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Compass Accuracy", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = when (sensorAccuracy) {
                                        SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> "High"
                                        SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> "Medium"
                                        else -> "Low/Calibrating"
                                    },
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (sensorAccuracy >= 2) Color(0xFF4CAF50) else Color(0xFFF44336)
                                )
                            }
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (locationAvailable) Color(0xFF4CAF50) else Color.Gray
                            )
                        }
                    }

                    // Compass UI
                    Box(
                        modifier = Modifier.size(320.dp).shadow(16.dp, CircleShape).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawCircle(brush = Brush.radialGradient(listOf(Color(0xFF8B4513), Color(0xFFD4AF37))))
                            for (angle in 0..359 step 10) {
                                val isMajor = angle % 30 == 0
                                val lineLength = if (isMajor) 20f else 10f
                                val radian = Math.toRadians(angle.toDouble())
                                val startX = (size.width / 2) + (size.width / 2 - 40) * cos(radian).toFloat()
                                val startY = (size.height / 2) + (size.height / 2 - 40) * sin(radian).toFloat()
                                val endX = (size.width / 2) + (size.width / 2 - 40 - lineLength) * cos(radian).toFloat()
                                val endY = (size.height / 2) + (size.height / 2 - 40 - lineLength) * sin(radian).toFloat()
                                drawLine(
                                    color = if (isMajor) Color(0xFF206224) else Color(0xFFD4AF37),
                                    start = Offset(startX, startY),
                                    end = Offset(endX, endY),
                                    strokeWidth = if (isMajor) 2.dp.toPx() else 1.dp.toPx()
                                )
                            }
                        }

                        Box(modifier = Modifier.size(260.dp).rotate(smoothAzimuth), contentAlignment = Alignment.Center) {
                            listOf(
                                Triple("N", Alignment.TopCenter, MaterialTheme.colorScheme.primary),
                                Triple("S", Alignment.BottomCenter, Color(0xFFD32F2F)),
                                Triple("E", Alignment.CenterEnd, Color(0xFFD4AF37)),
                                Triple("W", Alignment.CenterStart, MaterialTheme.colorScheme.secondary)
                            ).forEach { (text, alignment, color) ->
                                Text(
                                    text = text,
                                    color = color,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(alignment).padding(20.dp)
                                )
                            }
                        }

                        // Needle
                        Canvas(modifier = Modifier.size(280.dp).rotate(smoothQiblaAngle)) {
                            val centerX = size.width / 2
                            val centerY = size.height / 2
                            drawLine(color = Color(0xFFD32F2F), start = Offset(centerX, centerY), end = Offset(centerX, centerY - 110f), strokeWidth = 8.dp.toPx(), cap = StrokeCap.Round)
                            drawCircle(color = Color(0xFFD32F2F), center = Offset(centerX, centerY), radius = 10.dp.toPx())
                            drawCircle(color = Color(0xFF212121), center = Offset(centerX, centerY - 110f), radius = 15.dp.toPx()) // Kaaba Symbol
                        }
                    }

                    // Stats
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(buildAnnotatedString { withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Qibla: ") }; append("${qiblaDirection.toInt()}°") })
                            Text(buildAnnotatedString { withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Heading: ") }; append("${currentAzimuth.toInt()}°") })
                        }
                    }

                    Text("Hold your device flat for the most accurate reading", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                }
            }
        }
    }
}
