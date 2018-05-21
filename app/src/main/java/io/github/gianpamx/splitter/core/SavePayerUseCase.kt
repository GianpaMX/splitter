package io.github.gianpamx.splitter.core

import kotlinx.coroutines.experimental.delay

interface SavePayerUseCase {
    suspend fun invoke(player: Payer): List<Payer>
}

class SavePayerUseCaseImpl() : SavePayerUseCase {
    override suspend fun invoke(payer: Payer): List<Payer> {
        delay(5_000)
        return listOf(payer)
    }
}
