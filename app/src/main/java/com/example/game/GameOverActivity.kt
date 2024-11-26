package com.example.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.game.ui.theme.GameTheme

class GameOverActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val score = intent.getIntExtra("score", 0)  // Retrieve the score from the intent

        setContent {
            GameTheme {
                Surface {
                    GameOverScreen(score)
                }
            }
        }
    }
}

@Composable
fun GameOverScreen(score: Int) {
    val context = LocalContext.current
    val defaultName = "PlayerNone"
    var playerName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Game Over", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Your Score: $score")

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = playerName,
            onValueChange = { playerName = it },
            label = { Text("Enter Your Name") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            // Use default name if playerName is blank
            val finalName = if (playerName.isBlank()) defaultName else playerName
            saveScore(context, finalName, score)
            context.startActivity(Intent(context, MainActivity::class.java))
        }) {
            Text("Submit Score and Return to Home")
        }
    }
}

fun saveScore(context: Context, name: String, score: Int) {
    val sharedPreferences = context.getSharedPreferences("scoreboard", Context.MODE_PRIVATE)
    val scores = sharedPreferences.getString("scores", "") ?: ""

    // Append the new score entry as "name:score;"
    val updatedScores = "$scores$name:$score;"
    sharedPreferences.edit().putString("scores", updatedScores).apply()
}
