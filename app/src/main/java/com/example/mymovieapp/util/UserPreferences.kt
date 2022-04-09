package com.example.mymovieapp.util

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mymovieapp.model.Keyword
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferences(private val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "dataStore")

    private val KEY_SAVE_SEARCH_KEYWORDS = booleanPreferencesKey("검색어 저장 여부")
    private val KEY_SEARCH_DATA = stringPreferencesKey("검색 기록 데이터")

    val saveOptionFlow: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[KEY_SAVE_SEARCH_KEYWORDS] ?: false
        }

    val searchDataFlow: Flow<MutableList<Keyword>> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            if (preferences[KEY_SEARCH_DATA].isNullOrEmpty()) {
                arrayListOf(Keyword(0,""))
            } else {
                Gson().
                fromJson(preferences[KEY_SEARCH_DATA], Array<Keyword>::class.java).
                toMutableList()
            }

        }
    suspend fun setSearchData(data: MutableList<Keyword>) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SEARCH_DATA] = Gson().toJson(data)
        }
    }

    suspend fun setSaveSearchOption(isSaveEnabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SAVE_SEARCH_KEYWORDS] = isSaveEnabled
        }
    }
}