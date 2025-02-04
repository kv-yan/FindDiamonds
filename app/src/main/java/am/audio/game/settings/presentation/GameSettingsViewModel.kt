package am.audio.game.settings.presentation

import am.audio.game.common.common_domain.GameMode
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameSettingsViewModel : ViewModel() {

    private val _gameListOf =
        MutableStateFlow(listOf(GameMode.Easy, GameMode.Medium, GameMode.Hard))
    val gameListOf = _gameListOf.asStateFlow()

    private val _selectedMode = MutableStateFlow(GameMode.Hard)
    val selectedMode = _selectedMode.asStateFlow()

    fun setSelectedMode(gameMode: GameMode) {
        _selectedMode.value = gameMode
    }

}