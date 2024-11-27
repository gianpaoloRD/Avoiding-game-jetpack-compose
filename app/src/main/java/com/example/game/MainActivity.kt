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
        setContent {
            GameTheme {
                Surface {
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Avoiding Game", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            context.startActivity(Intent(context, GameActivity::class.java))
        }) {
            Text("Start Game")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            context.startActivity(Intent(context, ScoreboardActivity::class.java))
        }) {
            Text("View Scoreboard")
        }

        Button(
            onClick = {
                val intent = Intent(context, HowToPlayActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("How to Play")
        }

    }
}
