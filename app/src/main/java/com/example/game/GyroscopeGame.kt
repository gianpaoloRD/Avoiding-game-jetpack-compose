package com.example.game

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import android.content.res.Configuration
import android.graphics.Path
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay

@Composable
fun GyroscopeGame(
    xRotation: Float,
    yRotation: Float,
    onGameOver: () -> Unit,
    onScoreUpdate: (Int) -> Unit
) {
    // State variables
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    val density = LocalDensity.current
    val screenWidthPx = with(density) { screenWidth.toPx() }
    val screenHeightPx = with(density) { screenHeight.toPx() }

    var characterXPosition by remember { mutableStateOf(screenWidthPx / 2) }
    var characterYPosition by remember { mutableStateOf(screenHeightPx / 2) }

    var enemies by remember { mutableStateOf(listOf<Enemy>()) }
    var score by remember { mutableStateOf(0) }

    // Spawn enemies periodically
    LaunchedEffect(Unit) {
        val screenWidthPx = with(density) { screenWidth.toPx() } // Convert width to pixels
        val screenHeightPx = with(density) { screenHeight.toPx() } // Convert height to pixels

        while (true) {
            // Random Y-position within the screen height in pixels
            val spawnYPosition = (0..screenHeightPx.toInt()).random().toFloat()
            // Spawn enemies just outside the right edge of the screen
            val spawnXPosition = screenWidthPx + 50f // Adjust 50f for off-screen buffer

            val newEnemy = Enemy(xPosition = spawnXPosition, yPosition = spawnYPosition)
            enemies = enemies + newEnemy

            delay(1500L) // Delay between spawns
        }
    }

    LaunchedEffect(xRotation, yRotation) {
        val dx = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) yRotation else xRotation
        val dy = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) xRotation else yRotation

        val deltaX = with(density) { (dx * 5.dp).toPx() }
        val deltaY = with(density) { (dy * 5.dp).toPx() }

        characterXPosition += deltaX
        characterYPosition += deltaY
    }

    // Handle enemy movement and collisions
    LaunchedEffect(enemies) {
        enemies = enemies.map { enemy ->
            enemy.copy(xPosition = enemy.xPosition - enemy.speed)
        }.filter { enemy ->
            if (enemy.xPosition <= -50) {
                score += 1
                onScoreUpdate(score)
                false
            } else true
        }

        enemies.forEach { enemy ->
            if (isColliding(
                    characterXPosition,
                    characterYPosition,
                    enemy.xPosition.toFloat(),
                    enemy.yPosition.toFloat()
                )) {
                onGameOver()
            }

        }
    }

    // Main UI
    Box(modifier = Modifier.fillMaxSize()) {
        GameBackground()

        // Render character
        CharacterCanvas(characterXPosition, characterYPosition)

        // Render enemies
        EnemyCanvas(enemies)

        // Display score
        ScoreDisplay(score)
    }
}
@Composable
fun GameBackground() {
    ParallaxBackground()
}

@Composable
fun ScrollingBackground() {
    val infiniteTransition = rememberInfiniteTransition()
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f, // Exact width of your image in dp
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Render two images for seamless scrolling
        Image(
            painter = painterResource(id = R.mipmap.sky), // Your image
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()

        )
        Image(
            painter = painterResource(id = R.mipmap.sky),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .offset(x = (1000 - offset).dp) // Scrolls the second image into view
        )
    }
}





@Composable
fun ParallaxBackground() {
    val infiniteTransition = rememberInfiniteTransition()

    // Animate the offsets for each layer
    val farBackgroundOffset by infiniteTransition.animateFloat(
        initialValue = 1000f,
        targetValue = 1009f, // Adjust based on image width
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30000, easing = LinearEasing), // Slow for far layer
            repeatMode = RepeatMode.Restart
        )
    )

    val middleBackgroundOffset by infiniteTransition.animateFloat(
        initialValue = 1000f,
        targetValue = 1009f, // Adjust based on image width
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30000, easing = LinearEasing), // Medium speed for middle layer
            repeatMode = RepeatMode.Restart
        )
    )

    val foregroundOffset by infiniteTransition.animateFloat(
        initialValue = 1000f,
        targetValue = 1009f, // Adjust based on image width
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30000, easing = LinearEasing), // Fast for foreground
            repeatMode = RepeatMode.Restart
        )
    )

    // Stack layers in a Box
    Box(modifier = Modifier.fillMaxSize()) {
        // Far background (e.g., sky or mountains)
        Image(
            painter = painterResource(id = R.mipmap.sky1), // Replace with your far background image
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .offset(x = (-farBackgroundOffset).dp)
        )
        Image(
            painter = painterResource(id = R.mipmap.sky1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .offset(x = (-farBackgroundOffset + 1000).dp)
        )

        // Middle background (e.g., hills or trees)
        Image(
            painter = painterResource(id = R.mipmap.sky2), // Replace with your middle background image
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .offset(x = (-middleBackgroundOffset).dp)
        )
        Image(
            painter = painterResource(id = R.mipmap.sky2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .offset(x = (-middleBackgroundOffset + 1005).dp)
        )

        // Foreground (e.g., close-up objects like bushes or grass)
        Image(
            painter = painterResource(id = R.mipmap.sky3), // Replace with your foreground image
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .offset(x = (-foregroundOffset).dp)
        )
        Image(
            painter = painterResource(id = R.mipmap.sky3),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .offset(x = (-foregroundOffset + 1005).dp)
        )
    }
}


@Composable
fun CharacterCanvas(xPosition: Float, yPosition: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = Color.Blue,
            center = Offset(xPosition, yPosition),
            radius = 25f
        )

        // Add custom shapes or details
        drawCircle(
            color = Color.White,
            center = Offset(xPosition, yPosition - 10f), // Eye
            radius = 5f
        )

        drawPath(
            color = Color.Yellow,
            path = Path().apply {
                moveTo(xPosition - 15f, yPosition + 5f) // Beak
                lineTo(xPosition - 5f, yPosition - 5f)
                lineTo(xPosition + 5f, yPosition + 5f)
                close()
            }
        )
    }
}

@Composable
fun EnemyCanvas(enemies: List<Enemy>) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        enemies.forEach { enemy ->
            drawCircle(
                color = Color.Red,
                center = Offset(enemy.xPosition, enemy.yPosition),
                radius = 15f // Match the enemySize in collision logic
            )
        }
    }
}

@Composable
fun ScoreDisplay(score: Int) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Score: $score",
            modifier = Modifier
                .align(Alignment.TopCenter) // Use align within a Box scope
                .padding(16.dp),
            style = MaterialTheme.typography.headlineMedium
        )
    }
}


// Helper function to check for collision
fun isColliding(
    characterX: Float, characterY: Float,
    enemyX: Float, enemyY: Float,
    characterSize: Float = 50f, enemySize: Float = 20f
): Boolean {
    return characterX < enemyX + enemySize &&
            characterX + characterSize > enemyX &&
            characterY < enemyY + enemySize &&
            characterY + characterSize > enemyY
}
