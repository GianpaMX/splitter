package io.github.gianpamx.splitter.core

import io.github.gianpamx.splitter.core.entity.Person
import io.reactivex.Completable
import io.reactivex.Observable

class SaveReceiver(private val persistenceGateway: PersistenceGateway) {
  operator fun invoke(isChecked: Boolean, person: Person, expenseId: Long) =
    if (person.id > 0L) {
      persistenceGateway.updatePersonObservable(person)
    } else {
      persistenceGateway.createPersonObservable(person)
    }.flatMap {
      persistenceGateway.findReceiverObservable(it.id, expenseId)
    }
    .switchIfEmpty(Observable.defer {
      if (isChecked) {
        persistenceGateway.createReceiverCompletable(person.id, expenseId)
      } else {
        Completable.complete()
      }.toObservable<Pair<Long, Long>>()
    }).flatMapCompletable {
      if (!isChecked) {
        persistenceGateway.deleteReceiverCompletable(person.id, expenseId)
      } else {
        Completable.complete()
      }
    }
}
