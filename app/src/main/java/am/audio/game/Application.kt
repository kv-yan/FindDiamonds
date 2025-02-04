package am.audio.game

import am.audio.game.game.presentation.di.gamePresentationModule
import am.audio.game.settings.presentation.di.gameSettingsPresentationModule
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            modules(
                gamePresentationModule,
                gameSettingsPresentationModule
            )
        }
    }
}