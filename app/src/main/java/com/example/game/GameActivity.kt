package com.example.game


import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.game.ui.theme.GameTheme

class GameActivity : ComponentActivity() {

    private lateinit var sensorManager: SensorManager
    private var gyroscopeSensor: Sensor? = null

    // State variables for gyroscope data
    private var _xRotation by mutableStateOf(0f)
    private var _yRotation by mutableStateOf(0f)

    // Score variable to track the player's score
    private var score by mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GameTheme {
                Surface {
                    GyroscopeGame(
                        xRotation = _xRotation,
                        yRotation = _yRotation,
                        onGameOver = {
                            // Pass the score to GameOverActivity when game over occurs
                            val intent = Intent(this, GameOverActivity::class.java)
                            intent.putExtra("score", score)
                            startActivity(intent)
                            finish()
                        },
                        onScoreUpdate = { newScore ->
                            score = newScore  // Update the score in GameActivity
                        }
                    )
                }
            }
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        gyroscopeSensor?.let { sensor ->
            sensorManager.registerListener(
                gyroscopeListener,
                sensor,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    private val gyroscopeListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                _xRotation = it.values[0]
                _yRotation = it.values[1]
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(gyroscopeListener)
    }

    override fun onResume() {
        super.onResume()
        gyroscopeSensor?.also { sensor ->
            sensorManager.registerListener(
                gyroscopeListener,
                sensor,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }
}
