package com.example.mymovieapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mymovieapp.model.Keyword
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert
    suspend fun insert(keyword: Keyword): Long

    @Query("SELECT * FROM keywords ORDER BY id DESC LIMIT 10")
    fun getAllKeywords(): Flow<List<Keyword>>

    @Query("DELETE FROM keywords")
    suspend fun clearKeywords()

    @Delete
    suspend fun deleteKeyword(keyword: Keyword)
}