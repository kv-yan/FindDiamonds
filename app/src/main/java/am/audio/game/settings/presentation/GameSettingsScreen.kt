package am.audio.game.settings.presentation

import am.audio.game.common.common_domain.GameMode
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameSettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: GameSettingsViewModel = koinViewModel(),
    onStartGame: (GameMode) -> Unit = {},
) {
    val gameListOf by viewModel.gameListOf.collectAsState()
    val selectedMode by viewModel.selectedMode.collectAsState()
    val selectedIndex = gameListOf.indexOf(selectedMode)

    // Screen width and calculated indicator width (1/3 of screen width)
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val indicatorWidth = screenWidth / 3

    val animatedOffset by animateDpAsState(
        targetValue = selectedIndex * indicatorWidth,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "highlighterAnimation"
    )

    Column(
        modifier = modifier
            .background(Color.Black)
            .fillMaxSize()
            .systemBarsPadding(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Please choose the game level",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            // Animated Highlight Bar
            Box(
                modifier = Modifier
                    .offset(x = animatedOffset)
                    .width(indicatorWidth) // Fixed width of the indicator (1/3 of screen width)
                    .height(5.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
            )

            // Game Mode Options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                gameListOf.forEachIndexed { index, gameMode ->
                    Column(
                        modifier = Modifier
                            .weight(1f) // Equal weight for each item
                            .clip(CircleShape)
                            .clickable { viewModel.setSelectedMode(gameMode) }
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${gameMode.height} x ${gameMode.width}", color = Color.White
                        )
                        Text(
                            text = gameMode.name, color = Color.White
                        )
                    }
                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = { onStartGame(selectedMode) },
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Start the game",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
        }
    }
}

