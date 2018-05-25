package io.github.gianpamx.splitter.gateway.room

import android.arch.persistence.room.*
import io.github.gianpamx.splitter.gateway.room.model.ExpenseDBModel
import io.github.gianpamx.splitter.gateway.room.model.PaymentDBModel
import io.github.gianpamx.splitter.gateway.room.model.PersonDBModel
import io.reactivex.Flowable

@Dao
interface DatabaseDao {
    @Query("SELECT Payment.* FROM Payment JOIN Person ON(Payment.personId = Person.id) WHERE Payment.expenseId = :expenseId")
    fun observePayments(expenseId: Long): Flowable<List<PaymentDBModel>>

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
    fun insert(person: PersonDBModel) : Long


    @Insert
    fun insert(expense: ExpenseDBModel) : Long
}
