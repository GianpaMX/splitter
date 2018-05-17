package io.github.gianpamx.splitter.app

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.gianpamx.splitter.core.CoreModule
import javax.inject.Singleton

@Module(includes = [
    Binder::class,
    CoreModule::class
])
class AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application
}
