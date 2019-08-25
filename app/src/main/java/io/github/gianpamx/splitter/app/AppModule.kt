package io.github.gianpamx.splitter.app

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import java.text.NumberFormat
import javax.inject.Singleton

@Module(includes = [
    Binder::class,
    CoreModule::class
])
class AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    fun provideCurrencyFormat() = NumberFormat.getCurrencyInstance()
}
