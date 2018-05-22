package io.github.gianpamx.splitter.gateway.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.reactivex.Flowable

@Dao
interface DatabaseDao {
    @Insert
    fun insert(payer: PayerDBModel)

    @Query("SELECT * FROM Payer")
    fun retriveAllPayers(): List<PayerDBModel>

    @Query("SELECT * FROM Payer")
    fun allPayers(): Flowable<List<PayerDBModel>>

    @Update
    fun update(payer: PayerDBModel)
}
