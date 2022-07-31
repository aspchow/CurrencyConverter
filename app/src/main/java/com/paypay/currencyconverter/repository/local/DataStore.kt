package com.paypay.currencyconverter.repository.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.paypay.currencyconverter.repository.model.Rate
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStore @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "currency_rates")

    private val dataStore = context.dataStore

    private val RATE_KEY = stringPreferencesKey("RATE_KEY")

    private val RATE_LAST_MODIFIED_KEY = longPreferencesKey("RATE_LMT")


    companion object {
        const val RATE_LAST_MODIFIED_DEFAULT_VALUE = -1L
        val RATE_DEFAULT_VALUE = Rate.defaultRate()
    }


    fun getRate(): Flow<Rate> = dataStore.getValue(RATE_KEY, "").map { rateEncode ->
        if (rateEncode.isEmpty()) Rate.defaultRate()
        else gson.fromJson(rateEncode, Rate::class.java)
    }

    suspend fun saveRate(rate: Rate) {
        dataStore.saveValue(RATE_KEY, gson.toJson(rate))
        saveLmtOfRate(System.currentTimeMillis())
    }

    fun getLmtOFRate(): Flow<Long> =
        dataStore.getValue(RATE_LAST_MODIFIED_KEY, RATE_LAST_MODIFIED_DEFAULT_VALUE)

    private suspend fun saveLmtOfRate(lmt: Long) {
        dataStore.saveValue(RATE_LAST_MODIFIED_KEY, lmt)
    }


}


suspend fun <T> DataStore<Preferences>.saveValue(key: Preferences.Key<T>, value: T) =
    edit { settings ->
        settings[key] = value
    }


fun <T> DataStore<Preferences>.getValue(key: Preferences.Key<T>, defaultValue: T): Flow<T> =
    data.map { preference ->
        preference[key] ?: defaultValue
    }
