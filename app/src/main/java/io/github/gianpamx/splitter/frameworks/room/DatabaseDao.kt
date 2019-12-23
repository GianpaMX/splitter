package io.github.gianpamx.splitter.frameworks.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.github.gianpamx.splitter.core.entity.Payment
import io.github.gianpamx.splitter.frameworks.room.model.ExpenseDBModel
import io.github.gianpamx.splitter.frameworks.room.model.PaymentDBModel
import io.github.gianpamx.splitter.frameworks.room.model.PersonDBModel
import io.github.gianpamx.splitter.frameworks.room.model.ReceiverDBModel
import io.github.gianpamx.splitter.frameworks.room.view.PayerDBView
import io.github.gianpamx.splitter.frameworks.room.view.ReceiverDBView
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

const val TRUE = 1
const val FALSE = 0

@Dao
interface DatabaseDao {
  @Query("SELECT Person.id, Person.name, Payment.cents " +
      "FROM Person " +
      "LEFT JOIN Payment ON(Payment.personId = Person.id AND Payment.expenseId = :expenseId)")
  fun observePayments(expenseId: Long): Observable<List<PayerDBView>>

  @Query("SELECT * FROM Payment WHERE personId = :personId AND expenseId = :expenseId")
  fun findPayment(personId: Long, expenseId: Long): PaymentDBModel?

  @Query("SELECT * FROM Payment WHERE personId = :personId AND expenseId = :expenseId")
  fun findPaymentObservable(personId: Long, expenseId: Long): Observable<PaymentDBModel>

  @Query("SELECT * FROM Payment WHERE expenseId = :expenseId")
  fun findPayments(expenseId: Long): List<PaymentDBModel>

  @Query("SELECT * FROM Payment WHERE personId = :personId")
  fun findPaymentsByPersonId(personId: Long): List<PaymentDBModel>

  @Insert
  fun insert(payment: PaymentDBModel)

  @Insert
  fun insertCompletable(paymentDBModel: PaymentDBModel) : Completable

  @Update
  fun update(payment: PaymentDBModel)

  @Update
  fun updateCompletable(paymentDBModel: PaymentDBModel): Completable

  @Delete
  fun deletePayment(payment: PaymentDBModel)

  @Delete
  fun deletePaymentCompletable(payment: PaymentDBModel) : Completable

  @Query("SELECT * FROM Person WHERE id = :personId")
  fun findPerson(personId: Long): PersonDBModel

  @Query("SELECT * FROM Person")
  fun findPersons(): List<PersonDBModel>

  @Update
  fun update(person: PersonDBModel)

  @Update
  fun updateCompletable(person: PersonDBModel) : Completable

  @Insert
  fun insert(person: PersonDBModel): Long

  @Insert
  fun insertCompletable(person: PersonDBModel): Completable

  @Query("SELECT DISTINCT E.* FROM Expense AS E JOIN Payment AS P ON(E.id = P.expenseId)")
  fun observeExpenses(): Flowable<List<ExpenseDBModel>>

  @Query("SELECT * FROM Expense WHERE id = :expenseId")
  fun findExpense(expenseId: Long): ExpenseDBModel?

  @Query("SELECT * FROM Expense WHERE id = :expenseId")
  fun findExpenseObservable(expenseId: Long): Maybe<ExpenseDBModel>

  @Query("SELECT DISTINCT E.* FROM Expense AS E JOIN Receiver AS R ON(E.id = R.expenseId) WHERE R.personId = :personId")
  fun findExpensesByPersonId(personId: Long): List<ExpenseDBModel>

  @Insert
  fun insert(expense: ExpenseDBModel): Long

  @Insert
  fun insertSingle(expense: ExpenseDBModel): Single<Long>

  @Update
  fun update(expense: ExpenseDBModel)

  @Update
  fun updateCompletable(expense: ExpenseDBModel): Completable

  @Delete
  fun deleteExpense(expense: ExpenseDBModel)

  @Query("SELECT " +
      "P.*, " +
      "CASE WHEN R.expenseId IS NULL THEN $FALSE ELSE $TRUE END AS checked " +
      "FROM Person AS P " +
      "LEFT JOIN Receiver AS R ON(P.id = R.personId AND expenseId = :expenseId)")
  fun observeReceivers(expenseId: Long): Flowable<List<ReceiverDBView>>

  @Query("SELECT * FROM Receiver WHERE personId = :personId AND expenseId = :expenseId")
  fun findReceiver(personId: Long, expenseId: Long): ReceiverDBModel?

  @Query("SELECT * FROM Receiver WHERE expenseId = :expenseId")
  fun findReceivers(expenseId: Long): List<ReceiverDBModel>

  @Insert
  fun insert(receiver: ReceiverDBModel)

  @Delete
  fun deleteReceiver(receiver: ReceiverDBModel)
}
