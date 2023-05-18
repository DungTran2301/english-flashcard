package com.dungtran.android.core.englishflashcard.core.data.repository

import com.dungtran.android.core.englishflashcard.core.data.api.DiscoveryService
import com.dungtran.android.core.englishflashcard.core.data.db.dao.CardDao
import com.dungtran.android.core.englishflashcard.core.data.db.dao.SetDao
import com.dungtran.android.core.englishflashcard.core.data.model.CardDB
import com.dungtran.android.core.englishflashcard.core.data.model.SetDB
import com.dungtran.android.core.englishflashcard.core.data.model.StatsDB
import com.dungtran.android.core.englishflashcard.core.data.model.toStatsDB
import com.dungtran.android.core.englishflashcard.core.data.response.discovery.BasePagingResponse
import com.dungtran.android.core.englishflashcard.ui.model.CardView
import com.dungtran.android.core.englishflashcard.ui.model.SetView
import com.dungtran.android.core.englishflashcard.ui.model.toCardDB
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SetRepository @Inject constructor (
    private val setDao: SetDao,
    private val cardDao: CardDao,
    private val discoveryService: DiscoveryService
    ) {

    suspend fun getAllSet(isRemote: Boolean = false): Result<List<SetDB>> {
        return try {
            val cards = setDao.getAllSet().firstOrNull() ?: emptyList()
            Result.success(cards)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun getRemoteSet(page: Int, pageSize: Int): Result<BasePagingResponse<SetDB>?> {
        return try {
            val cards = discoveryService.getSet(page, pageSize)
            Result.success(cards)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }


    suspend fun insert(set: SetDB): Result<Long> {
        return try {
            val insertedItemId = setDao.insert(set) ?: 0L
            Result.success(insertedItemId)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun update(set: SetDB) {
        setDao.update(set)
    }

    suspend fun delete(set: SetDB) {
        setDao.delete(set)
    }


    suspend fun deleteSetId(setId: Long): Result<Unit> {
        return try {
            cardDao.deleteCardsBySetId(setId)
            setDao.deleteById(setId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveSetFromServer(setDB: SetDB, cards: List<CardDB>): Result<Any> {
        return try {
            val setId = setDao.insert(setDB)
            val listCardDB = cards.map { card ->
                card.setID = setId
                card
            }
            cardDao.insert(listCardDB)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveSetStatsInfo(setId: Long, duration: Long, countRight: Int = 0, countWrong: Int = 0): Result<Any> {
        return try {
            val set = setDao.getSetById(setId).firstOrNull()
            if (set != null) {
                setDao.updateSetStatsInfo(set, duration, countRight, countWrong)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Set not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSetStats(): Result<List<StatsDB>> {
        return try {
            val sets = setDao.getAllSet().firstOrNull() ?: emptyList()
            val statsDBs = sets.map { set: SetDB ->
                set.toStatsDB()
            }
            Result.success(statsDBs)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}