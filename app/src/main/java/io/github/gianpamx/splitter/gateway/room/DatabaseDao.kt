package io.github.gianpamx.splitter.gateway.room

import android.arch.persistence.room.*
import io.github.gianpamx.splitter.gateway.room.model.ExpenseDBModel
import io.github.gianpamx.splitter.gateway.room.model.PaymentDBModel
import io.github.gianpamx.splitter.gateway.room.model.PersonDBModel
import io.github.gianpamx.splitter.gateway.room.model.ReceiverDBModel
import io.github.gianpamx.splitter.gateway.room.view.PayerDBView
import io.github.gianpamx.splitter.gateway.room.view.ReceiverDBView
import io.reactivex.Flowable

const val TRUE = 1
const val FALSE = 0

@Dao
interface DatabaseDao {
    @Query("SELECT Person.id, Person.name, Payment.cents " +
            "FROM Person " +
            "LEFT JOIN Payment ON(Payment.personId = Person.id AND Payment.expenseId = :expenseId)")
    fun observePayments(expenseId: Long): Flowable<List<PayerDBView>>

    @Query("SELECT * FROM Payment WHERE personId = :personId AND expenseId = :expenseId")
    fun findPayment(personId: Long, expenseId: Long): PaymentDBModel?

    @Insert
    fun insert(payment: PaymentDBModel)

    @Update
    fun update(payment: PaymentDBModel)


    @Delete
    fun deletePayment(payment: PaymentDBModel)

    @Query("SELECT * FROM Person WHERE id = :personId")
    fun findPerson(personId: Long): PersonDBModel

    @Update
    fun update(person: PersonDBModel)

    @Insert
    fun insert(person: PersonDBModel): Long


    @Insert
    fun insert(expense: ExpenseDBModel): Long


    @Query("SELECT " +
            "P.*, " +
            "CASE WHEN R.expenseId IS NULL THEN $FALSE ELSE $TRUE END AS checked " +
            "FROM Person AS P " +
            "LEFT JOIN Receiver AS R ON(P.id = R.personId AND expenseId = :expenseId)")
    fun observeReceivers(expenseId: Long): Flowable<List<ReceiverDBView>>

    @Query("SELECT * FROM Receiver WHERE personId = :personId AND expenseId = :expenseId")
    fun findReceiver(personId: Long, expenseId: Long): ReceiverDBModel?

    @Insert
    fun insert(receiver: ReceiverDBModel)

    @Delete
    fun deleteReceiver(receiver: ReceiverDBModel)
}
