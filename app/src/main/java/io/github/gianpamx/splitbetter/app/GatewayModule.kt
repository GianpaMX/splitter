package io.github.gianpamx.splitbetter.app

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.gianpamx.splitbetter.domain.gateway.UniqueIdGateway
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GatewayModule {
    @Provides
    @Singleton
    fun provideUniqueIdGateway() = object : UniqueIdGateway {
        override fun getUniqueId() = UUID.randomUUID().toString()
    }
}
