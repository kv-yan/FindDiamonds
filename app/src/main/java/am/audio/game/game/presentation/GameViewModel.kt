package am.audio.game.game.presentation

import am.audio.game.common.common_domain.GameMode
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel(
    private val mode: GameMode,
) : ViewModel() {
    private val _gameDescList = MutableStateFlow<List<GameCardItem>>(listOf())
    val gameDescList = _gameDescList.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    private val _isGameOver = MutableStateFlow(false)
    val isGameOver = _isGameOver.asStateFlow()

    private val _isGameWon = MutableStateFlow(false)
    val isGameWon = _isGameWon.asStateFlow()

    init {
        resetGame()
    }

    fun resetGame() {
        _score.value = 0
        _isGameOver.value = false
        _isGameWon.value = false
        prepareGameDesc(mode)
    }

    private fun prepareGameDesc(mode: GameMode) {
        val itemsCount = mode.width * mode.height
        val bombIndices = (0 until itemsCount).shuffled().take(mode.bombQuantity)

        val newDesc = List(itemsCount) { index ->
            GameCardItem(
                gameCardType = if (bombIndices.contains(index)) GameCardType.Bomb else GameCardType.Diamond
            )
        }

        _gameDescList.value = newDesc
    }

    fun updateGameDesc(gameCardItem: GameCardItem) {
        if (gameCardItem.isShowingCardType) return

        _gameDescList.value = _gameDescList.value.map { item ->
            if (item === gameCardItem) item.copy(isShowingCardType = true) else item
        }

        if (gameCardItem.gameCardType == GameCardType.Bomb) {
            _isGameOver.value = true
        } else {
            _score.value += 10
            checkWinCondition()
        }
    }

    private fun checkWinCondition() {
        val allDiamondsFound =
            _gameDescList.value.all { it.gameCardType == GameCardType.Bomb || it.isShowingCardType }
        if (allDiamondsFound) {
            _isGameWon.value = true
        }
    }
}
