package com.example.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.game.ui.theme.GameTheme

// Activity for the "How to Play" screen
class HowToPlayActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view for the activity using Jetpack Compose
        setContent {
            // Apply the GameTheme for consistent app styling
            GameTheme {
                // Call the HowToPlayScreen composable and handle the back action
                HowToPlayScreen(onBack = { finish() }) // Finish the activity when the "Got It!" button is clicked
            }
        }
    }
}

// Composable function for the "How to Play" screen
@Composable
fun HowToPlayScreen(onBack: () -> Unit) {
    // Surface provides a container with a background color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(), // Fill the entire screen
        color = MaterialTheme.colorScheme.background // Use the background color from the theme
    ) {
        // Column layout to arrange items vertically
        Column(
            modifier = Modifier.padding(16.dp), // Add padding around the content
            verticalArrangement = Arrangement.Center, // Center items vertically
            horizontalAlignment = Alignment.CenterHorizontally // Center items horizontally
        ) {
            // Title of the screen
            Text(
                text = "How to Play", // Display "How to Play" text
                style = MaterialTheme.typography.headlineMedium // Apply a medium headline style
            )

            Spacer(modifier = Modifier.height(20.dp)) // Add vertical spacing

            // Instructions for the game
            Text(
                text = "1. Tilt your device to move the character.\n" +
                        "2. Avoid the enemies as they move toward you.\n" +
                        "3. Survive as long as you can to score points!", // Display gameplay instructions
                style = MaterialTheme.typography.bodyLarge // Apply a large body text style
            )

            Spacer(modifier = Modifier.height(40.dp)) // Add vertical spacing

            // Button to close the "How to Play" screen
            Button(onClick = onBack) { // Handle the back action when clicked
                Text("Got It!") // Label for the button
            }
        }
    }
}


