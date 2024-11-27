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

// Activity for displaying the scoreboard
class ScoreboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Apply the GameTheme for consistent styling
            GameTheme {
                Surface {
                    // Call the composable that defines the scoreboard screen
                    ScoreboardScreen()
                }
            }
        }
    }
}

// Composable for displaying the scoreboard screen
@Composable
fun ScoreboardScreen() {
    val context = LocalContext.current // Retrieve the current context for accessing SharedPreferences
    var scores by remember { mutableStateOf(getScores(context)) } // Get the list of scores using helper function

    // Column layout for arranging items vertically
    Column(
        modifier = Modifier
            .fillMaxSize() // Fill the entire screen
            .padding(16.dp), // Add padding around the edges
        verticalArrangement = Arrangement.Top, // Align items at the top
        horizontalAlignment = Alignment.CenterHorizontally // Center items horizontally
    ) {
        // Display the title of the screen
        Text("Scoreboard", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(20.dp)) // Add vertical spacing

        // Display each score as a "Name: Score" entry
        scores.forEach { (name, score) ->
            Text("$name: $score", style = MaterialTheme.typography.bodyLarge) // Show name and score
            Spacer(modifier = Modifier.height(10.dp)) // Add spacing between scores
        }

        Spacer(modifier = Modifier.height(20.dp)) // Add vertical spacing

        // Button to erase all scores
        Button(onClick = {
            clearScores(context) // Call the helper function to clear scores
            scores = getScores(context) // Update the scores list after clearing
        }) {
            Text("Erase All Scores") // Label for the button
        }
    }
}

// Helper function to retrieve scores from SharedPreferences
fun getScores(context: Context): List<Pair<String, Int>> {
    // Access the SharedPreferences file named "scoreboard"
    val sharedPreferences = context.getSharedPreferences("scoreboard", Context.MODE_PRIVATE)
    // Get the saved scores as a single string, or return an empty string if not found
    val scoresString = sharedPreferences.getString("scores", "") ?: ""

    // Split the string into individual entries and map each entry to a name-score pair
    return scoresString.split(";")
        .filter { it.isNotBlank() } // Ignore empty entries
        .map {
            val (name, score) = it.split(":") // Split each entry into name and score
            name to score.toInt() // Convert the score to an integer and return as a pair
        }
}

// Helper function to clear scores from SharedPreferences
fun clearScores(context: Context) {
    // Access the SharedPreferences file named "scoreboard"
    val sharedPreferences = context.getSharedPreferences("scoreboard", Context.MODE_PRIVATE)
    // Remove the "scores" entry from SharedPreferences and apply the changes
    sharedPreferences.edit().remove("scores").apply()
}
