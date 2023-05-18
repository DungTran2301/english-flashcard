package com.dungtran.android.core.englishflashcard.core.data.db.dao

import androidx.room.*
import com.dungtran.android.core.englishflashcard.core.data.model.CardDB
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("select * from tblLocalCard")
    fun getAllCard(): Flow<List<CardDB>>

    @Query("SELECT * FROM tblLocalCard WHERE set_id = :setId")
    fun getCardsBySet(setId: Long): Flow<List<CardDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<CardDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cardDB: CardDB): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(cardDB: CardDB)

    @Delete
    suspend fun delete(cardDB: CardDB)

    @Query("DELETE FROM tblLocalCard WHERE set_id = :setId")
    suspend fun deleteCardsBySetId(setId: Long)
}