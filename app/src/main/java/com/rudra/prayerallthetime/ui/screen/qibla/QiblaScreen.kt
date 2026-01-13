package com.rudra.prayerallthetime.ui.screen.qibla

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QiblaScreen(navController: NavController, prayerViewModel: PrayerViewModel) {
    val context = LocalContext.current
    val qiblaDirection by prayerViewModel.qiblaDirection.collectAsState()
    
    var currentAzimuth by remember { mutableStateOf(0f) }
    
    // Sensor Logic
    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        
        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                    val rotationMatrix = FloatArray(9)
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(rotationMatrix, orientation)
                    // Azimuth is orientation[0], convert to degrees
                    currentAzimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        
        sensorManager.registerListener(sensorEventListener, rotationVectorSensor, SensorManager.SENSOR_DELAY_UI)
        
        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    val animatedAzimuth by animateFloatAsState(targetValue = -currentAzimuth)
    val animatedQibla by animateFloatAsState(targetValue = qiblaDirection - currentAzimuth)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Qibla Compass") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            // Compass UI
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .rotate(animatedAzimuth),
                contentAlignment = Alignment.Center
            ) {
                // Circle
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color.Gray,
                        style = Stroke(width = 4.dp.toPx())
                    )
                }
                
                // Cardinal Directions
                Text("N", modifier = Modifier.align(Alignment.TopCenter).padding(8.dp), fontWeight = FontWeight.Bold)
                Text("S", modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp), fontWeight = FontWeight.Bold)
                Text("E", modifier = Modifier.align(Alignment.CenterEnd).padding(8.dp), fontWeight = FontWeight.Bold)
                Text("W", modifier = Modifier.align(Alignment.CenterStart).padding(8.dp), fontWeight = FontWeight.Bold)
            }

            // Qibla Needle (Moves independently relative to North)
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Placeholder for needle
                contentDescription = "Qibla Needle",
                modifier = Modifier
                    .size(100.dp)
                    .rotate(animatedQibla + 90f), // Adjust icon orientation
                tint = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "Kaaba Direction: ${qiblaDirection.toInt()}°",
                modifier = Modifier.align(Alignment.BottomCenter).padding(32.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
