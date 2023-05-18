package com.dungtran.android.core.englishflashcard.core.data.db.dao

import androidx.room.*
import com.dungtran.android.core.englishflashcard.core.data.model.SetDB
import kotlinx.coroutines.flow.Flow

@Dao
interface SetDao {
    @Query("select * from tblSet")
    fun getAllSet(): Flow<List<SetDB>>

    @Query("SELECT * FROM tblSet WHERE id = :setId")
    fun getSetById(setId: Long): Flow<SetDB>

    @Query("SELECT COUNT(*) FROM tblLocalCard WHERE set_id = :setId")
    fun getNumberOfCardBySetId(setId: Long): Flow<Int>

    @Insert
    suspend fun insert(set: SetDB): Long

    @Update
    suspend fun update(set: SetDB)

    @Delete
    suspend fun delete(set: SetDB)

    @Query("DELETE FROM tblSet WHERE id = :setId")
    fun deleteById(setId: Long)

    @Transaction
    suspend fun updateSetStatsInfo(set: SetDB, studyDuration: Long, countRight: Int, countWrong: Int) {
        set.studyDuration += studyDuration
        set.numberOfRightAnswers += countRight
        set.numberOfWrongAnswers += countWrong
        update(set)
    }
}