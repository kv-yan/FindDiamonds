package am.audio.game.common.common_presentation.niavigation

import am.audio.game.R
import am.audio.game.common.common_domain.GameMode
import am.audio.game.game.presentation.GameScreen
import am.audio.game.settings.presentation.GameSettingsScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Destination.Settings,
        modifier = modifier
    ) {
        composable<Destination.Settings> {
            GameSettingsScreen { selectedMode ->
                navController.navigate(
                    context.getString(
                        R.string.game_destination,
                        selectedMode.ordinal.toString()
                    )
                )
            }
        }

        composable(context.getString(R.string.game_mode)) { backStackEntry ->
            val modeOrdinal = backStackEntry.arguments?.getString(stringResource(R.string.mode))?.toIntOrNull() ?: 0
            val mode = GameMode.entries[modeOrdinal]

            GameScreen(
                modifier = Modifier,
                selectedMode = mode,
            )
        }
    }
}


sealed class Destination {
    @Serializable
    data object Settings : Destination()
}