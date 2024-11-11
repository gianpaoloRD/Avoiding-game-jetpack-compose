package com.example.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

@Composable
fun GyroscopeGame(
    xRotation: Float,
    yRotation: Float,
    onGameOver: () -> Unit,
    onScoreUpdate: (Int) -> Unit
) {
    // Get screen dimensions and orientation
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

    // Initialize character's position to the center of the screen based on orientation
    var characterXPosition by remember { mutableStateOf(screenWidth / 2) }
    var characterYPosition by remember { mutableStateOf(screenHeight / 2) }
    var enemies by remember { mutableStateOf(listOf<Enemy>()) }

    // State to track the score
    var score by remember { mutableStateOf(0) }

    // Timer effect to spawn enemies periodically
    LaunchedEffect(Unit) {
        while (true) {
            val spawnYPosition = (0..(screenHeight.value.toInt() - 50)).random().toFloat() // Random spawn within screen height
            val newEnemy = Enemy(xPosition = screenWidth.value, yPosition = spawnYPosition)
            enemies = enemies + newEnemy

            kotlinx.coroutines.delay(1500L)
        }
    }

    // Update the character's position based on gyroscope data and orientation
    LaunchedEffect(xRotation, yRotation, isPortrait) {
        if (isPortrait) {
            // Standard x and y rotation for portrait mode
            characterYPosition += xRotation * 5.dp
            characterXPosition += yRotation * 5.dp
        } else {
            // Swap axes in landscape mode
            characterYPosition += yRotation * 5.dp
            characterXPosition += xRotation * 5.dp
        }
    }

    // Update enemy positions, remove off-screen enemies, and increment score for avoided enemies
    LaunchedEffect(enemies) {
        enemies = enemies.map { enemy ->
            enemy.copy(xPosition = enemy.xPosition - enemy.speed)
        }.filter { enemy ->
            if (enemy.xPosition <= -50) {
                // Enemy avoided, increment score
                score += 1
                onScoreUpdate(score)  // Update the score in GameActivity
                false  // Remove the enemy
            } else {
                true
            }
        }
    }

    // Collision Detection with game over callback
    LaunchedEffect(enemies, characterXPosition, characterYPosition) {
        enemies.forEach { enemy ->
            if (isColliding(characterXPosition.value, characterYPosition.value, enemy.xPosition, enemy.yPosition)) {
                onGameOver()  // Trigger game over callback on collision
            }
        }
    }

    // Display character, enemies, and score
    Box(modifier = Modifier.fillMaxSize()) {
        // Character
        Box(
            modifier = Modifier
                .size(50.dp)
                .offset(
                    x = characterXPosition - 25.dp,  // Center character by adjusting for size
                    y = characterYPosition - 25.dp
                )
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🐦")
        }

        // Enemies
        enemies.forEach { enemy ->
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .offset(x = enemy.xPosition.dp, y = enemy.yPosition.dp)
                    .background(MaterialTheme.colorScheme.error)
            )
        }

        // Display the score at the top
        Text(
            text = "Score: $score",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp),
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

// Helper function to check for collision
fun isColliding(
    characterX: Float, characterY: Float,
    enemyX: Float, enemyY: Float,
    characterSize: Float = 50f, enemySize: Float = 30f
): Boolean {
    return characterX < enemyX + enemySize &&
            characterX + characterSize > enemyX &&
            characterY < enemyY + enemySize &&
            characterY + characterSize > enemyY
}
