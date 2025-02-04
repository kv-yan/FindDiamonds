package am.audio.game.game.presentation.di

import am.audio.game.common.common_domain.GameMode
import am.audio.game.game.presentation.GameViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val gamePresentationModule = module {
    viewModel<GameViewModel> { (mode: GameMode) ->
        GameViewModel(
            mode = mode
        )

    }
}