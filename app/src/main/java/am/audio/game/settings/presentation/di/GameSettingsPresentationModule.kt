package am.audio.game.settings.presentation.di

import am.audio.game.settings.presentation.GameSettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val gameSettingsPresentationModule = module {
    viewModelOf(::GameSettingsViewModel)
}