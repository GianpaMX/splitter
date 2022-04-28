package io.github.gianpamx.splitbetter.app

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideMutableAppActionFlow(): MutableAppActionFlow = MutableSharedFlow()

    @Provides
    @Singleton
    fun provideAppActionFlow(flow: MutableAppActionFlow): AppActionFlow = flow
}
