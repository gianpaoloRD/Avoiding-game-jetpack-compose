package com.example.game

import android.content.Context
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

class ScoreboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GameTheme {
                Surface {
                    ScoreboardScreen()
                }
            }
        }
    }
}

@Composable
fun ScoreboardScreen() {
    val context = LocalContext.current
    var scores by remember { mutableStateOf(getScores(context)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Scoreboard", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(20.dp))

        // Display each score in the list
        scores.forEach { (name, score) ->
            Text("$name: $score", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // "Erase" button to clear scores
        Button(onClick = {
            clearScores(context)
            scores = getScores(context) // Update the displayed list after clearing
        }) {
            Text("Erase All Scores")
        }
    }
}

// Helper function to retrieve scores from SharedPreferences
fun getScores(context: Context): List<Pair<String, Int>> {
    val sharedPreferences = context.getSharedPreferences("scoreboard", Context.MODE_PRIVATE)
    val scoresString = sharedPreferences.getString("scores", "") ?: ""

    return scoresString.split(";")
        .filter { it.isNotBlank() }
        .map {
            val (name, score) = it.split(":")
            name to score.toInt()
        }
}

// Helper function to clear scores from SharedPreferences
fun clearScores(context: Context) {
    val sharedPreferences = context.getSharedPreferences("scoreboard", Context.MODE_PRIVATE)
    sharedPreferences.edit().remove("scores").apply()
}
