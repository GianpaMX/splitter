package io.github.gianpamx.splitter.core

import io.github.gianpamx.splitter.core.entity.Payment
import io.github.gianpamx.splitter.core.entity.Person
import io.reactivex.Observable

class SavePayment(private val persistenceGateway: PersistenceGateway) {
  operator fun invoke(cents: Int, person: Person, expenseId: Long) =
    if (person.id > 0L) {
      persistenceGateway.updatePersonObservable(person)
    } else {
      persistenceGateway.createPersonObservable(person)
    }.map { it ->
      Payment(expenseId, it, cents)
    }.filter {
      it.cents > 0
    }.switchIfEmpty(Observable.defer {
      persistenceGateway
          .deletePaymentCompletable(expenseId, person.id)
          .toObservable<Payment>()
    }).flatMap { payment ->
      persistenceGateway
          .findPaymentObservable(person, expenseId)
          .switchIfEmpty(Observable.defer { persistenceGateway.createPaymentObservable(payment) })
          .flatMap {
            persistenceGateway.updatePaymentObservable(payment)
          }
    }
}
