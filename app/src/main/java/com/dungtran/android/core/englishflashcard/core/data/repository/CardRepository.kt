package com.dungtran.android.core.englishflashcard.core.data.repository

import com.dungtran.android.core.englishflashcard.core.data.api.DiscoveryService
import com.dungtran.android.core.englishflashcard.core.data.db.dao.CardDao
import com.dungtran.android.core.englishflashcard.core.data.db.dao.SetDao
import com.dungtran.android.core.englishflashcard.core.data.model.CardDB
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class CardRepository @Inject constructor(
    private val cardDao: CardDao,
    private val setDao: SetDao,
    private val discoveryService: DiscoveryService
) {

    suspend fun getAll(): Result<List<CardDB>> {
        return try {

            val cards = cardDao.getAllCard().firstOrNull() ?: emptyList()
            Result.success(cards)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun getCardsBySetId(setId: Long, isRemote: Boolean = false): Result<List<CardDB>> {
        return try {
            if (isRemote) {
                val cards = discoveryService.getCardsBySet(setId) ?: emptyList()
                Result.success(cards)
            } else {
                val cards = cardDao.getCardsBySet(setId).firstOrNull() ?: emptyList()
                Result.success(cards)
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun insert(card: CardDB): Result<Long> {
        return try {
            val insertedItemId = cardDao.insert(card) ?: 0L
            val set = setDao.getSetById(card.setID).firstOrNull()
            if (set != null) {
                set.count = setDao.getNumberOfCardBySetId(card.setID).firstOrNull() ?: 0
                setDao.update(set)
                Result.success(insertedItemId)
            }
            else {
                Result.failure(Exception("Set not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun insert(cards: List<CardDB>): Result<Unit> {
        return try {
            val res = cardDao.insert(cards)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    suspend fun update(card: CardDB) {
        cardDao.update(card)
    }

    suspend fun delete(card: CardDB) {
        cardDao.delete(card)
    }

}