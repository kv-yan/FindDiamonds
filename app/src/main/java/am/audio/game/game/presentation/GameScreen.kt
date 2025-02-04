package am.audio.game.game.presentation

import am.audio.game.R
import am.audio.game.common.common_domain.GameMode
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import java.util.UUID

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    selectedMode: GameMode,
    viewModel: GameViewModel = getViewModel { parametersOf(selectedMode) },
) {
    val gameDesc by viewModel.gameDescList.collectAsState()
    val score by viewModel.score.collectAsState()
    val isGameOver by viewModel.isGameOver.collectAsState()
    val isGameWon by viewModel.isGameWon.collectAsState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemSize = (screenWidth / selectedMode.width) - 10.dp

    var showGameOverDialog by remember { mutableStateOf(false) }
    var showWinDialog by remember { mutableStateOf(false) }

    // Delayed Game Over Dialog
    LaunchedEffect(isGameOver) {
        if (isGameOver) {
            delay(500) // Wait 500ms before showing the dialog
            showGameOverDialog = true
        } else {
            showGameOverDialog = false
        }
    }

    // Delayed Win Dialog
    LaunchedEffect(isGameWon) {
        if (isGameWon) {
            delay(500) // Wait 500ms before showing the dialog
            showWinDialog = true
        } else {
            showWinDialog = false
        }
    }

    Column(
        modifier = modifier
            .background(Color.Black)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display Score
        Text(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            text = "Score: $score",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White
        )

        // Game Grid
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(selectedMode.width),
        ) {
            items(gameDesc) { gameCardItem ->
                CardItem(
                    modifier = Modifier
                        .size(itemSize)
                        .padding(4.dp),
                    gameCardItem = gameCardItem
                ) {
                    viewModel.updateGameDesc(gameCardItem)
                }
            }
        }

        // Game Over Dialog
        if (showGameOverDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Game Over") },
                text = { Text("You hit a bomb! Your final score: $score") },
                confirmButton = {
                    Button(onClick = { viewModel.resetGame() }) {
                        Text("Restart")
                    }
                }
            )
        }

        // Congratulations Dialog
        if (showWinDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Congratulations!") },
                text = { Text("You found all diamonds! 🎉 Final score: $score") },
                confirmButton = {
                    Button(onClick = { viewModel.resetGame() }) {
                        Text("Play Again")
                    }
                }
            )
        }
    }
}


@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    gameCardItem: GameCardItem,
    onClick: () -> Unit = {},
) {
    val isFlipped = gameCardItem.isShowingCardType // Now directly controlled by ViewModel
    val rotationY by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing),
        label = "flipAnimation"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Gray)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .clickable {
                if (!isFlipped) {
                    onClick() // Notify ViewModel to update game state
                }
            }
            .graphicsLayer {
                this.rotationY = rotationY
                cameraDistance = 12f * density // Avoid distortion
            },
        contentAlignment = Alignment.Center
    ) {
        if (rotationY > 90f) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                painter = painterResource(
                    if (gameCardItem.gameCardType == GameCardType.Bomb) R.drawable.ic_sad
                    else R.drawable.ic_diamond
                ),
                contentDescription = null
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text("?", fontSize = 24.sp, color = Color.White)
            }
        }
    }
}

enum class GameCardType {
    Diamond, Bomb
}

data class GameCardItem(
    val id: String = UUID.randomUUID().toString(),
    val isShowingCardType: Boolean = false,
    val gameCardType: GameCardType,
)