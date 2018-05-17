package io.github.gianpamx.splitter.core

interface SavePayerUseCase : (Payer, (List<Payer>) -> Unit, (Exception) -> Unit) -> Unit

class SavePayerUseCaseImpl : SavePayerUseCase {
    override fun invoke(payer: Payer, success: (List<Payer>) -> Unit, failure: (Exception) -> Unit) {
        success.invoke(listOf(payer))
    }
}
