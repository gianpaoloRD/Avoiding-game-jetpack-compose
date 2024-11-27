package com.example.game

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.game.ui.theme.GameTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view for the activity using Jetpack Compose
        setContent {
            // Apply the custom GameTheme for consistent styling
            GameTheme {
                // Surface provides a container with a background color from the theme
                Surface {
                    // Call the HomeScreen composable to display the main menu
                    HomeScreen()
                }
            }
        }
    }
}

// HomeScreen is the main menu of the app, allowing users to navigate to different parts of the game
@Composable
fun HomeScreen() {
    val context = LocalContext.current // Get the current context for navigation

    Column(
        modifier = Modifier.fillMaxSize(), // Fill the entire screen
        verticalArrangement = Arrangement.Center, // Center items vertically
        horizontalAlignment = Alignment.CenterHorizontally // Center items horizontally
    ) {
        // Display the game title
        Text(text = "Avoiding Game", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(20.dp)) // Add spacing between elements

        // Button to start the game
        Button(onClick = {
            context.startActivity(Intent(context, GameActivity::class.java)) // Navigate to GameActivity
        }) {
            Text("Start Game") // Label for the button
        }

        Spacer(modifier = Modifier.height(20.dp)) // Add spacing between elements

        // Button to view the scoreboard
        Button(onClick = {
            context.startActivity(Intent(context, ScoreboardActivity::class.java)) // Navigate to ScoreboardActivity
        }) {
            Text("View Scoreboard") // Label for the button
        }

        // Button to view the "How to Play" screen
        Button(
            onClick = {
                val intent = Intent(context, HowToPlayActivity::class.java) // Create an Intent for HowToPlayActivity
                context.startActivity(intent) // Start HowToPlayActivity
            },
            modifier = Modifier.padding(16.dp) // Add padding around the button
        ) {
            Text("How to Play") // Label for the button
        }
    }
}
